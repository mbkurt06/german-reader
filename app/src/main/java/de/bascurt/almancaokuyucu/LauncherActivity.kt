package de.bascurt.almancaokuyucu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AuthTurquoise = Color(0xFF1FA7A5)
private val AuthDark = Color(0xFF102F3C)
private const val LOCAL_USERNAME = "zeynep"
private const val LOCAL_PASSWORD = "zeynep123"

class LauncherActivity : ComponentActivity() {
    private val authPrefs by lazy { getSharedPreferences("local_auth", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (authPrefs.getBoolean("signed_in", false)) {
            openAfterLogin()
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
                LocalAccountEntryScreen(
                    onLogin = { username, password ->
                        if (username.trim().lowercase() == LOCAL_USERNAME && password == LOCAL_PASSWORD) {
                            authPrefs.edit().putBoolean("signed_in", true).apply()
                            openAfterLogin()
                            null
                        } else {
                            "Kullanıcı adı veya şifre yanlış."
                        }
                    }
                )
            }
        }
    }

    private fun openAfterLogin() {
        val profileConfigured = authPrefs.getBoolean("profile_configured", false)
        val target = if (profileConfigured) MainActivity::class.java else ProfileSetupActivity::class.java
        startActivity(Intent(this, target))
        finish()
    }
}

@Composable
private fun LocalAccountEntryScreen(
    onLogin: (String, String) -> String?
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(22.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Almanca Okuyucu", color = AuthDark, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(
            "Şimdilik lokal kullanıcı hesabıyla giriş yapıyoruz. Gerçek hesap sistemi daha sonra eklenecek.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Giriş", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; errorText = null },
                    label = { Text("Kullanıcı adı") },
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

                errorText?.let {
                    Spacer(Modifier.height(10.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { errorText = onLogin(username, password) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text("Giriş yap", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = AuthTurquoise.copy(alpha = .10f))
        ) {
            Column(Modifier.padding(15.dp)) {
                Text("Test hesabı", color = AuthDark, fontWeight = FontWeight.Bold)
                Text("Kullanıcı adı: zeynep", fontSize = 13.sp)
                Text("Şifre: zeynep123", fontSize = 13.sp)
            }
        }
    }
}
