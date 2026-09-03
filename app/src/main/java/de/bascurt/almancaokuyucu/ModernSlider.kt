package de.bascurt.almancaokuyucu

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0
) {
    var trackWidthPx by remember { mutableFloatStateOf(1f) }
    val density = LocalDensity.current
    val thumbSize = 18.dp
    val range = (valueRange.endInclusive - valueRange.start).takeIf { it > 0f } ?: 1f
    val fraction = ((value - valueRange.start) / range).coerceIn(0f, 1f)

    fun snap(raw: Float): Float {
        val clamped = raw.coerceIn(valueRange.start, valueRange.endInclusive)
        if (steps <= 0) return clamped
        val intervals = steps + 1
        val stepSize = range / intervals
        val index = ((clamped - valueRange.start) / stepSize).roundToInt()
        return (valueRange.start + index * stepSize).coerceIn(valueRange.start, valueRange.endInclusive)
    }

    fun valueForX(x: Float): Float {
        val f = (x / trackWidthPx).coerceIn(0f, 1f)
        return snap(valueRange.start + f * range)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .pointerInput(valueRange, steps) {
                detectTapGestures { offset -> onValueChange(valueForX(offset.x)) }
            }
            .pointerInput(valueRange, steps) {
                detectDragGestures { change, _ ->
                    change.consume()
                    onValueChange(valueForX(change.position.x))
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(5.dp)
                .onSizeChanged { trackWidthPx = it.width.coerceAtLeast(1).toFloat() }
                .background(Color(0xFF91A5AA).copy(alpha = .30f), RoundedCornerShape(99.dp))
        )
        if (fraction > 0f) {
            Box(
                Modifier
                    .fillMaxWidth(fraction)
                    .height(5.dp)
                    .background(ReaderAccent, RoundedCornerShape(99.dp))
            )
        }
        Box(
            Modifier
                .offset {
                    val thumbPx = with(density) { thumbSize.toPx() }
                    IntOffset(((trackWidthPx * fraction) - thumbPx / 2f).roundToInt(), 0)
                }
                .width(thumbSize)
                .height(thumbSize)
                .shadow(3.dp, CircleShape)
                .background(ReaderAccent, CircleShape)
        )
    }
}

private val ReaderAccent = Color(0xFF1FA7A5)
