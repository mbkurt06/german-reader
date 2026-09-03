package de.bascurt.almancaokuyucu

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import de.bascurt.almancaokuyucu.data.UserPreferences

internal val AppTurquoise = Color(0xFF1FA7A5)
internal val AppDark = Color(0xFF102F3C)
internal val AppSoftBackground = Color(0xFFF4F7F8)

@Composable
internal fun GermanReaderTheme(
    preferences: UserPreferences,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val dark = when (preferences.themeMode) {
        "dark" -> true
        "light" -> false
        else -> systemDark
    }
    val scheme = if (dark) {
        darkColorScheme(primary = AppTurquoise, secondary = AppTurquoise)
    } else {
        lightColorScheme(
            primary = AppTurquoise,
            secondary = AppTurquoise,
            background = AppSoftBackground
        )
    }
    MaterialTheme(colorScheme = scheme, content = content)
}
