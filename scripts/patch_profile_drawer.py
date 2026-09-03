from pathlib import Path
import re

path = Path('app/src/main/java/de/bascurt/almancaokuyucu/MainActivity.kt')
text = path.read_text()

# Imports needed by the profile view and drawer avatar.
text = text.replace('import android.app.Activity\n', 'import android.app.Activity\nimport android.content.Intent\nimport android.net.Uri\nimport android.widget.ImageView\n')
text = text.replace('import androidx.compose.ui.Alignment\n', 'import androidx.compose.ui.Alignment\nimport androidx.compose.ui.draw.clip\n')
text = text.replace('import androidx.compose.ui.unit.sp\n', 'import androidx.compose.ui.unit.sp\nimport androidx.compose.ui.viewinterop.AndroidView\n')

# Drawer header becomes the single profile entry point; remove the duplicate Profile menu row.
old_header_call = 'DrawerHeader(preferences, saved.size, completedLessonIds.size)'
new_header_call = 'DrawerHeader(preferences, saved.size, completedLessonIds.size) { navigate(AppPage.PROFILE); scope.launch { drawerState.close() } }'
if old_header_call not in text:
    raise SystemExit('DrawerHeader call not found')
text = text.replace(old_header_call, new_header_call, 1)

profile_item_pattern = re.compile(r'\n\s*DrawerItem\("●", uiText\(lang, "Profilim"\), page == AppPage\.PROFILE\) \{ navigate\(AppPage\.PROFILE\); scope\.launch \{ drawerState\.close\(\) \} \}\s*')
text, count = profile_item_pattern.subn('\n', text, count=1)
if count != 1:
    raise SystemExit(f'Profile drawer item removal count={count}')

header_pattern = re.compile(r'@Composable private fun DrawerHeader\(prefs: UserPreferences, savedCount: Int, completedCount: Int\) \{.*?\n\}\n\n@Composable private fun DrawerItem', re.S)
header_replacement = '''@Composable private fun DrawerHeader(prefs: UserPreferences, savedCount: Int, completedCount: Int, onProfile: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(22.dp)) {
        Box(Modifier.size(58.dp).clickable(onClick = onProfile)) {
            if (prefs.profilePhotoUri.isNotBlank()) {
                AndroidView(
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            setImageURI(Uri.parse(prefs.profilePhotoUri))
                        }
                    },
                    update = { image -> image.setImageURI(Uri.parse(prefs.profilePhotoUri)) },
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            } else {
                Surface(shape = CircleShape, color = Turquoise.copy(alpha = .16f), modifier = Modifier.fillMaxSize()) {
                    Box(contentAlignment = Alignment.Center) { Text(prefs.name.take(1).ifBlank { "Z" }.uppercase(), color = Turquoise, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(prefs.name, fontSize = 21.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onProfile))
        Text("@${prefs.username.ifBlank { "zeynep" }}", color = Turquoise, fontSize = 12.sp, modifier = Modifier.clickable(onClick = onProfile))
        Text("Almanca ${prefs.germanLevel}  •  $savedCount kelime  •  $completedCount tamamlandı", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
    }
}

@Composable private fun DrawerItem'''
text, count = header_pattern.subn(header_replacement, text, count=1)
if count != 1:
    raise SystemExit(f'DrawerHeader replacement count={count}')

profile_pattern = re.compile(r'@Composable\nprivate fun ProfileScreen\(prefs: UserPreferences, onSave: \(UserPreferences\) -> Unit, savedCount: Int, completedCount: Int, stats: LearningStats\) \{.*?\n\}\n\n@Composable private fun SmallStatCard', re.S)
profile_replacement = '''@Composable
private fun ProfileScreen(prefs: UserPreferences, onSave: (UserPreferences) -> Unit, savedCount: Int, completedCount: Int, stats: LearningStats) {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ElevatedCard(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(92.dp)) {
                    if (prefs.profilePhotoUri.isNotBlank()) {
                        AndroidView(
                            factory = { ctx ->
                                ImageView(ctx).apply {
                                    scaleType = ImageView.ScaleType.CENTER_CROP
                                    setImageURI(Uri.parse(prefs.profilePhotoUri))
                                }
                            },
                            update = { image -> image.setImageURI(Uri.parse(prefs.profilePhotoUri)) },
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    } else {
                        Surface(shape = CircleShape, color = Turquoise.copy(alpha = .16f), modifier = Modifier.fillMaxSize()) {
                            Box(contentAlignment = Alignment.Center) { Text(prefs.name.take(1).ifBlank { "Z" }.uppercase(), color = Turquoise, fontSize = 36.sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(prefs.name.ifBlank { "Zeynep" }, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text("@${prefs.username.ifBlank { "zeynep" }}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.width(10.dp))
                    Surface(
                        color = Turquoise.copy(alpha = .12f),
                        contentColor = Turquoise,
                        shape = CircleShape,
                        modifier = Modifier.size(42.dp).clickable {
                            context.startActivity(Intent(context, ProfileSetupActivity::class.java).putExtra("edit_profile", true))
                        }
                    ) { Box(contentAlignment = Alignment.Center) { Text("✎", fontSize = 20.sp, fontWeight = FontWeight.Bold) } }
                }
                if (prefs.bio.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text(prefs.bio, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        }

        ElevatedCard(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text("Profil bilgileri", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text("Almanca seviyesi: ${prefs.germanLevel}")
                Text("Öğrenme amacı: ${prefs.learningReason}")
                Text("Günlük hedef: ${prefs.dailyGoal} soru")
                Text("Veriler bu cihazda yerel olarak saklanıyor.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SmallStatCard("$savedCount", "Kelime", Modifier.weight(1f))
            SmallStatCard("$completedCount", "Hikâye", Modifier.weight(1f))
            SmallStatCard("${stats.studySessions}", "Çalışma", Modifier.weight(1f))
        }
    }
}

@Composable private fun SmallStatCard'''
text, count = profile_pattern.subn(profile_replacement, text, count=1)
if count != 1:
    raise SystemExit(f'ProfileScreen replacement count={count}')

path.write_text(text)
print('MainActivity profile/drawer patch applied')
