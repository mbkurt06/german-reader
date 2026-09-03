package de.bascurt.almancaokuyucu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import de.bascurt.almancaokuyucu.data.UserPreferences
import de.bascurt.almancaokuyucu.data.UserPreferencesStore

class ProfileSetupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = UserPreferencesStore(this)
        val initial = store.load()
        val editingExistingProfile = intent.getBooleanExtra("edit_profile", false)

        setContent {
            GermanReaderTheme(initial) {
                LocalProfileEditor(
                    initial = initial,
                    editingExistingProfile = editingExistingProfile,
                    onCancel = { finish() },
                    onSave = { value ->
                        store.save(value)
                        getSharedPreferences("local_auth", MODE_PRIVATE)
                            .edit()
                            .putBoolean("profile_configured", true)
                            .apply()

                        startActivity(
                            Intent(this, MainActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
private fun LocalProfileEditor(
    initial: UserPreferences,
    editingExistingProfile: Boolean,
    onCancel: () -> Unit,
    onSave: (UserPreferences) -> Unit
) {
    val context = LocalContext.current
    var displayName by remember { mutableStateOf(initial.name.ifBlank { "Zeynep" }) }
    var bio by remember { mutableStateOf(initial.bio) }
    var photoUri by remember { mutableStateOf(initial.profilePhotoUri) }
    var level by remember { mutableStateOf(initial.germanLevel) }
    var goal by remember { mutableFloatStateOf(initial.dailyGoal.toFloat()) }
    var learningReason by remember { mutableStateOf(initial.learningReason) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    val cleanedName = displayName.trim()
    val canSave = cleanedName.isNotBlank() && cleanedName.length <= 30 && bio.length <= 140
    val dirty = cleanedName != initial.name ||
        bio.trim() != initial.bio ||
        photoUri != initial.profilePhotoUri ||
        level != initial.germanLevel ||
        goal.toInt() != initial.dailyGoal ||
        learningReason != initial.learningReason

    fun requestClose() {
        if (editingExistingProfile && dirty) showDiscardDialog = true else onCancel()
    }

    BackHandler(enabled = editingExistingProfile) { requestClose() }

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            photoUri = uri.toString()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    if (editingExistingProfile) "Profilini düzenle" else "Profilini oluştur",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Profil bilgilerin şimdilik yalnızca bu cihazda saklanır.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
            if (editingExistingProfile) {
                TextButton(onClick = ::requestClose) { Text("Vazgeç") }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfilePhoto(photoUri, displayName)
                Text(
                    if (photoUri.isBlank()) "Profil fotoğrafı ekle" else "Profil fotoğrafını değiştir",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { photoPicker.launch(arrayOf("image/*")) }.padding(8.dp)
                )
                if (photoUri.isNotBlank()) {
                    Text(
                        "Fotoğrafı kaldır",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { photoUri = "" }.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text("@zeynep", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            }
        }

        OutlinedTextField(
            value = displayName,
            onValueChange = { if (it.length <= 30) displayName = it },
            label = { Text("Profil adı") },
            supportingText = { Text("${displayName.length}/30") },
            isError = cleanedName.isBlank(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = "zeynep",
            onValueChange = {},
            readOnly = true,
            label = { Text("Kullanıcı adı") },
            supportingText = { Text("Lokal kullanıcı adı bu sürümde sabittir.") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { if (it.length <= 140) bio = it },
            label = { Text("Kısa profil açıklaması") },
            placeholder = { Text("Örn. Almancamı günlük hayatta daha rahat kullanmak istiyorum.") },
            supportingText = { Text("${bio.length}/140") },
            minLines = 3,
            maxLines = 4,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Almanca seviyesi", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            listOf("A1", "A2", "B1", "B2", "C1").forEach { value ->
                FilterChip(selected = level == value, onClick = { level = value }, label = { Text(value) })
            }
        }

        Text("Öğrenme amacı", fontWeight = FontWeight.Bold)
        Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
            listOf("Günlük Almanca", "İş / kariyer", "Okul / sınav", "Seyahat").forEach { value ->
                FilterChip(selected = learningReason == value, onClick = { learningReason = value }, label = { Text(value) })
            }
        }

        Text("Günlük hedef", fontWeight = FontWeight.Bold)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface, shape = CircleShape, modifier = Modifier.size(54.dp).clickable(enabled = goal > 5f) { goal = (goal - 5f).coerceAtLeast(5f) }) {
                Box(contentAlignment = Alignment.Center) { Text("−", fontSize = 24.sp, fontWeight = FontWeight.SemiBold) }
            }
            Spacer(Modifier.weight(1f))
            Surface(color = ProfileTurquoise.copy(alpha = .13f), shape = RoundedCornerShape(11.dp)) {
                Text("${goal.toInt()} soru", Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = ProfileTurquoise, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.weight(1f))
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface, shape = CircleShape, modifier = Modifier.size(54.dp).clickable(enabled = goal < 50f) { goal = (goal + 5f).coerceAtMost(50f) }) {
                Box(contentAlignment = Alignment.Center) { Text("+", fontSize = 24.sp, fontWeight = FontWeight.SemiBold) }
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = .10f),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Profil adı, fotoğraf, bio ve öğrenme tercihlerinin tamamı cihazda saklanır ve tekrar düzenlenebilir.",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                textAlign = TextAlign.Start
            )
        }

        Button(
            onClick = {
                onSave(
                    initial.copy(
                        name = cleanedName,
                        username = "zeynep",
                        bio = bio.trim(),
                        profilePhotoUri = photoUri,
                        learningReason = learningReason,
                        germanLevel = level,
                        dailyGoal = goal.toInt()
                    )
                )
            },
            enabled = canSave && (!editingExistingProfile || dirty),
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                if (editingExistingProfile) "Değişiklikleri kaydet" else "Profili kaydet ve devam et",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(12.dp))
    }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Değişiklikler kaydedilmedi") },
            text = { Text("Profilde yaptığın değişiklikleri kaydetmeden çıkmak istiyor musun?") },
            confirmButton = { TextButton(onClick = onCancel) { Text("Kaydetmeden çık") } },
            dismissButton = { TextButton(onClick = { showDiscardDialog = false }) { Text("Düzenlemeye devam et") } }
        )
    }
}

@Composable
private fun ProfilePhoto(uri: String, displayName: String) {
    if (uri.isNotBlank()) {
        AndroidView(
            factory = { ctx ->
                ImageView(ctx).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setImageURI(Uri.parse(uri))
                }
            },
            update = { image -> image.setImageURI(Uri.parse(uri)) },
            modifier = Modifier.size(104.dp).clip(CircleShape)
        )
    } else {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = .16f),
            modifier = Modifier.size(104.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    displayName.trim().take(1).ifBlank { "Z" }.uppercase(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
