package de.bascurt.almancaokuyucu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val AuthTurquoise = Color(0xFF1FA7A5)
private val AuthDark = Color(0xFF102F3C)

class LauncherActivity : ComponentActivity() {
    private lateinit var authGateway: FirebaseAuthGateway

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authGateway = FirebaseAuthGateway(this)

        if (authGateway.currentAccount() != null) {
            openReader()
            return
        }

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = AuthTurquoise,
                    secondary = AuthTurquoise,
                    background = Color(0xFFF4F7F8)
                )
            ) {
                AccountEntryScreen(
                    gateway = authGateway,
                    onContinue = ::openReader
                )
            }
        }
    }

    private fun openReader() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

private enum class AccountMode { LOGIN, REGISTER }

@Composable
private fun AccountEntryScreen(
    gateway: FirebaseAuthGateway,
    onContinue: () -> Unit
) {
    var mode by remember { mutableStateOf(AccountMode.LOGIN) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun validate(): String? {
        if (!email.contains('@') || email.substringAfter('@').isBlank()) return "Geçerli bir e-posta adresi yazın."
        if (password.length < 6) return "Şifre en az 6 karakter olmalı."
        if (mode == AccountMode.REGISTER && password != passwordAgain) return "Şifreler aynı değil."
        return null
    }

    fun handleResult(result: Result<AppAccount>) {
        busy = false
        result.onSuccess { onContinue() }
            .onFailure { errorText = it.localizedMessage ?: "İşlem tamamlanamadı." }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(22.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Almanca Okuyucu", color = AuthDark, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(
            if (mode == AccountMode.LOGIN) "Hesabınla giriş yap ve çalışmalarına devam et."
            else "Yeni bir hesap oluştur.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 15.sp
        )
        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(18.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { mode = AccountMode.LOGIN; errorText = null },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mode == AccountMode.LOGIN) AuthTurquoise else Color(0xFFE8EFF1),
                            contentColor = if (mode == AccountMode.LOGIN) Color.White else AuthDark
                        )
                    ) { Text("Giriş") }
                    Button(
                        onClick = { mode = AccountMode.REGISTER; errorText = null },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mode == AccountMode.REGISTER) AuthTurquoise else Color(0xFFE8EFF1),
                            contentColor = if (mode == AccountMode.REGISTER) Color.White else AuthDark
                        )
                    ) { Text("Kayıt ol") }
                }

                Spacer(Modifier.height(18.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; errorText = null },
                    label = { Text("E-posta") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorText = null },
                    label = { Text("Şifre") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (mode == AccountMode.REGISTER) {
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = passwordAgain,
                        onValueChange = { passwordAgain = it; errorText = null },
                        label = { Text("Şifre tekrar") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                errorText?.let {
                    Spacer(Modifier.height(10.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        val validation = validate()
                        if (validation != null) {
                            errorText = validation
                            return@Button
                        }
                        busy = true
                        errorText = null
                        if (mode == AccountMode.LOGIN) {
                            gateway.signInWithEmail(email, password, ::handleResult)
                        } else {
                            gateway.registerWithEmail(email, password, ::handleResult)
                        }
                    },
                    enabled = !busy,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(if (busy) "Bekleyin…" else if (mode == AccountMode.LOGIN) "Giriş yap" else "Hesap oluştur", fontWeight = FontWeight.SemiBold)
                }

                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(Modifier.weight(1f))
                    Text("  veya  ", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    HorizontalDivider(Modifier.weight(1f))
                }
                Spacer(Modifier.height(14.dp))

                OutlinedButton(
                    onClick = {
                        busy = true
                        errorText = null
                        scope.launch { gateway.signInWithGoogle(::handleResult) }
                    },
                    enabled = !busy,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(15.dp)
                ) { Text("G  Google ile devam et") }
                Spacer(Modifier.height(9.dp))
                OutlinedButton(
                    onClick = {
                        busy = true
                        errorText = null
                        gateway.signInWithApple(::handleResult)
                    },
                    enabled = !busy,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(15.dp)
                ) { Text("  Apple ile devam et") }
            }
        }

        if (!gateway.isConfigured) {
            Spacer(Modifier.height(14.dp))
            Text(
                "Hesap sistemi kod olarak hazır. Firebase bağlantısı yapılana kadar e-posta, Google ve Apple girişi çalışmaz; uygulamayı misafir olarak kullanabilirsin.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
            Text("Şimdilik misafir olarak devam et")
        }
    }
}
