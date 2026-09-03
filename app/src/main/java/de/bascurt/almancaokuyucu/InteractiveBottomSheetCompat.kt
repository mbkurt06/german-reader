package de.bascurt.almancaokuyucu

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlin.math.roundToInt

/**
 * MainActivity imports Material3 with a star import. This same-package overload is intentionally
 * narrower than Material3's full ModalBottomSheet signature, so the existing calls resolve here.
 *
 * The dark dictionary sheet is rendered as a non-focusable popup: taps outside the panel continue
 * to reach the story, allowing another word to be selected without closing the dictionary.
 * Other sheets (for example reader display controls) are delegated to Material3 unchanged.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
    onDismissRequest: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    scrimColor: Color,
    tonalElevation: Dp,
    shape: Shape,
    dragHandle: @Composable (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit
) {
    val isInteractiveDictionary = containerColor == Color(0xFF102F3C)

    if (!isInteractiveDictionary) {
        androidx.compose.material3.ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            containerColor = containerColor,
            contentColor = contentColor,
            scrimColor = scrimColor,
            tonalElevation = tonalElevation,
            shape = shape,
            dragHandle = dragHandle,
            content = content
        )
        return
    }

    var dragOffset by remember { mutableFloatStateOf(0f) }
    val dismissThresholdPx = with(LocalDensity.current) { 72.dp.toPx() }

    Popup(
        alignment = Alignment.BottomCenter,
        properties = PopupProperties(
            focusable = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            clippingEnabled = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, dragOffset.roundToInt()) },
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation
        ) {
            Column(Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(onDismissRequest) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { _, dragAmount ->
                                    dragOffset = (dragOffset + dragAmount).coerceAtLeast(0f)
                                },
                                onDragEnd = {
                                    if (dragOffset >= dismissThresholdPx) onDismissRequest()
                                    else dragOffset = 0f
                                },
                                onDragCancel = { dragOffset = 0f }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    dragHandle?.invoke()
                }
                content()
            }
        }
    }
}
