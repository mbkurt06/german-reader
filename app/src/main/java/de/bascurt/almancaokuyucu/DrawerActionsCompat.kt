package de.bascurt.almancaokuyucu

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Keeps the drawer content scrollable on shorter screens and places sign-out
 * after the normal navigation items. Profile access lives only in DrawerHeader.
 */
@Composable
internal fun ModalDrawerSheet(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val context = LocalContext.current
    androidx.compose.material3.ModalDrawerSheet(modifier = modifier) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            content()
            HorizontalDivider(Modifier.padding(top = 8.dp, bottom = 6.dp))
            NavigationDrawerItem(
                label = { Text("Çıkış yap", fontWeight = FontWeight.SemiBold) },
                icon = { Text("⇥", color = Color(0xFFB3261E)) },
                selected = false,
                onClick = {
                    context.getSharedPreferences("local_auth", Activity.MODE_PRIVATE)
                        .edit()
                        .putBoolean("signed_in", false)
                        .apply()
                    context.startActivity(
                        Intent(context, LauncherActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 2.dp)
                    .navigationBarsPadding()
            )
        }
    }
}
