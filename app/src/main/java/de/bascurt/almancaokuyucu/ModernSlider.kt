package de.bascurt.almancaokuyucu

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

private object ReaderControlBridge {
    var decreaseText: (() -> Unit)? = null
    var increaseText: (() -> Unit)? = null
    var decreaseBrightness: (() -> Unit)? = null
    var increaseBrightness: (() -> Unit)? = null
    var nightMode by mutableStateOf(false)
    private var initialized = false

    fun ensureInitialized(context: Context) {
        if (initialized) return
        val prefs = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        nightMode = prefs.getBoolean("reader_night_mode", false)
        initialized = true
        applyNightFilter(context, nightMode)
    }

    fun setNightMode(context: Context, enabled: Boolean) {
        nightMode = enabled
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("reader_night_mode", enabled)
            .apply()
        applyNightFilter(context, enabled)
    }

    private fun applyNightFilter(context: Context, enabled: Boolean) {
        val activity = context as? Activity ?: return
        activity.window.decorView.foreground = if (enabled) {
            ColorDrawable(android.graphics.Color.argb(52, 42, 25, 8))
        } else {
            null
        }
    }
}

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
    val thumbSize = 16.dp
    val range = (valueRange.endInclusive - valueRange.start).takeIf { it > 0f } ?: 1f
    val fraction = ((value - valueRange.start) / range).coerceIn(0f, 1f)

    val isReaderTextSlider = valueRange.start == 18f && valueRange.endInclusive == 30f
    val isReaderBrightnessSlider = valueRange.start == .08f && valueRange.endInclusive == 1f
    if (isReaderTextSlider) {
        ReaderControlBridge.decreaseText = { onValueChange((value - 2f).coerceAtLeast(18f)) }
        ReaderControlBridge.increaseText = { onValueChange((value + 2f).coerceAtMost(30f)) }
    }
    if (isReaderBrightnessSlider) {
        ReaderControlBridge.decreaseBrightness = { onValueChange((value - .10f).coerceAtLeast(.08f)) }
        ReaderControlBridge.increaseBrightness = { onValueChange((value + .10f).coerceAtMost(1f)) }
    }

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
            .height(30.dp)
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
                .height(4.dp)
                .onSizeChanged { trackWidthPx = it.width.coerceAtLeast(1).toFloat() }
                .background(Color(0xFF91A5AA).copy(alpha = .25f), RoundedCornerShape(99.dp))
        )
        if (fraction > 0f) {
            Box(
                Modifier
                    .fillMaxWidth(fraction)
                    .height(4.dp)
                    .background(ReaderAccent, RoundedCornerShape(99.dp))
            )
        }
        Box(
            Modifier
                .offset {
                    val thumbPx = with(density) { thumbSize.toPx() }
                    IntOffset(((trackWidthPx * fraction) - thumbPx / 2f).roundToInt(), 0)
                }
                .size(thumbSize)
                .shadow(2.dp, CircleShape)
                .background(Color.White, CircleShape)
        )
    }
}

@Composable
fun ModalBottomSheet(
    onDismissRequest: () -> Unit,
    containerColor: Color,
    tonalElevation: Dp,
    shape: Shape,
    dragHandle: @Composable (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit
) {
    val context = LocalContext.current
    ReaderControlBridge.ensureInitialized(context)

    androidx.compose.material3.ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = containerColor.copy(alpha = .72f),
        scrimColor = Color.Transparent,
        tonalElevation = 0.dp,
        shape = shape,
        dragHandle = dragHandle
    ) {
        content()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color.White.copy(alpha = .06f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "☾",
                    fontSize = 25.sp,
                    color = if (ReaderControlBridge.nightMode) Color(0xFFFFC857) else MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.weight(1f))
            Switch(
                checked = ReaderControlBridge.nightMode,
                onCheckedChange = { ReaderControlBridge.setNightMode(context, it) }
            )
        }
    }
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = androidx.compose.material3.LocalTextStyle.current
) {
    val context = LocalContext.current
    ReaderControlBridge.ensureInitialized(context)

    if (text == "Okuma görünümü" ||
        text == "Yazıyı ve ekran ışığını okurken anlık olarak ayarla." ||
        text == "Yazı boyutu" ||
        text == "Aydınlatma"
    ) return

    var effectiveModifier = modifier
    val sizeValue = if (fontSize == TextUnit.Unspecified) -1f else fontSize.value
    effectiveModifier = when {
        text == "A" && sizeValue in 16f..19f -> effectiveModifier.clickable { ReaderControlBridge.decreaseText?.invoke() }
        text == "A" && sizeValue >= 24f -> effectiveModifier.clickable { ReaderControlBridge.increaseText?.invoke() }
        text == "☼" && sizeValue >= 19f -> effectiveModifier.clickable { ReaderControlBridge.decreaseBrightness?.invoke() }
        text == "☀" && sizeValue >= 22f -> effectiveModifier.clickable { ReaderControlBridge.increaseBrightness?.invoke() }
        else -> effectiveModifier
    }

    androidx.compose.material3.Text(
        text = text,
        modifier = effectiveModifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style
    )
}

private val ReaderAccent = Color(0xFF1FA7A5)
