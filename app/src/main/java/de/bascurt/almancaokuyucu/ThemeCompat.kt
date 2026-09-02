package de.bascurt.almancaokuyucu

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.LocalContentColor

/**
 * App-level Material theme wrapper.
 *
 * The Material 3 theme changes its ColorScheme correctly in light/dark mode,
 * but plain layout containers such as Column/Box do not provide a new
 * LocalContentColor by themselves. This wrapper makes unspecified text/icon
 * colors inherit the active theme's onBackground color across the app.
 * Material components (cards, buttons, navigation, etc.) still provide their
 * own content colors as usual.
 */
@Composable
fun MaterialTheme(
    colorScheme: ColorScheme,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.MaterialTheme(colorScheme = colorScheme) {
        CompositionLocalProvider(
            LocalContentColor provides colorScheme.onBackground,
            content = content
        )
    }
}
