package de.bascurt.almancaokuyucu

import androidx.compose.foundation.layout.height as foundationHeight
import androidx.compose.foundation.layout.heightIn as foundationHeightIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Reader-screen sizing compatibility layer.
 *
 * MainActivity currently uses 350.dp for the meaning panel and 275.dp for
 * its scrollable content area. Remap only those exact values so the reader
 * keeps roughly a 30/70 explanation-to-story balance on a phone screen.
 */
fun Modifier.height(height: Dp): Modifier =
    this.foundationHeight(if (height == 350.dp) 230.dp else height)

fun Modifier.heightIn(
    min: Dp = Dp.Unspecified,
    max: Dp = Dp.Unspecified
): Modifier = this.foundationHeightIn(
    min = min,
    max = if (max == 275.dp) 155.dp else max
)
