package de.bascurt.almancaokuyucu

import androidx.compose.runtime.Composable

/**
 * Keeps theme detection available without requiring every screen file to import
 * the foundation helper directly.
 */
@Composable
fun isSystemInDarkTheme(): Boolean = androidx.compose.foundation.isSystemInDarkTheme()
