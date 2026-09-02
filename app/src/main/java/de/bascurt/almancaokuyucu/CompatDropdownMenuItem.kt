package de.bascurt.almancaokuyucu

import androidx.compose.runtime.Composable

/**
 * Compatibility wrapper for the Material3 version used by this project.
 * The current API calls this slot `leadingIcon`; MainActivity uses the
 * clearer `leadingContent` name.
 */
@Composable
fun DropdownMenuItem(
    text: @Composable () -> Unit,
    leadingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    androidx.compose.material3.DropdownMenuItem(
        text = text,
        leadingIcon = leadingContent,
        onClick = onClick
    )
}
