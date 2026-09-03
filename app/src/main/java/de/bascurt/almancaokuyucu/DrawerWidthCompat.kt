package de.bascurt.almancaokuyucu

import androidx.compose.foundation.layout.width as foundationWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Keeps the current UI code stable while making the main navigation drawer
 * less dominant on compact phone screens. The old drawer requested 310.dp,
 * which occupied about 86% of a typical 360.dp-wide phone. 288.dp is close
 * to an 80% drawer while leaving enough of the underlying screen visible.
 */
internal fun Modifier.width(width: Dp): Modifier =
    this.foundationWidth(if (width == 310.dp) 288.dp else width)
