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
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
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

/**
 * Daha sade, okuma odaklı slider. Material3'ün kalın track/tick görünümü yerine
 * ince bir çizgi ve küçük yuvarlak thumb kullanır. `colors` parametresi mevcut
 * çağrılarla API uyumluluğunu korumak için alınır; okuma ekranının turkuaz
 * aksanı bilinçli olarak sabittir.
 */
@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    colors: SliderColors = SliderDefaults.colors()
) {
    @Suppress("UNUSED_VARIABLE")
    val apiCompatibilityColors = colors
    var trackWidthPx by remember { mutableFloatStateOf(1f) }
    val density = LocalDensity.current
    val thumbSize = 16.dp
    val trackHeight = 4.dp
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
            .height(40.dp)
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
                .height(trackHeight)
                .onSizeChanged { trackWidthPx = it.width.coerceAtLeast(1).toFloat() }
                .background(Color(0xFF82979D).copy(alpha = .28f), RoundedCornerShape(99.dp))
        )
        if (fraction > 0f) {
            Box(
                Modifier
                    .fillMaxWidth(fraction)
                    .height(trackHeight)
                    .background(ReaderAccent, RoundedCornerShape(99.dp))
            )
        }
        Box(
            Modifier
                .offset {
                    val thumbPx = with(density) { thumbSize.toPx() }
                    val rawX = trackWidthPx * fraction - thumbPx / 2f
                    val maxX = (trackWidthPx - thumbPx).coerceAtLeast(0f)
                    IntOffset(rawX.coerceIn(0f, maxX).roundToInt(), 0)
                }
                .width(thumbSize)
                .height(thumbSize)
                .shadow(2.dp, CircleShape)
                .background(ReaderAccent, CircleShape)
        )
    }
}

private val ReaderAccent = Color(0xFF1FA7A5)
