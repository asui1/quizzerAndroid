package com.asu1.quiz.ui.textStyleManager

import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.getFontFamily

@Composable
fun GetTextStyle(text: String,
                 modifier: Modifier = Modifier,
                 myTextStyleExtra: TextStyleExtra,
                 textStyle: TextStyle,
) {
    when (myTextStyleExtra.contourStyle) {
        0 -> Text(
            text = text,
            style =
                textStyle,
            modifier = modifier
        )
        1 -> {
            TextWithBorder(
                text = text,
                fontSize = textStyle.fontSize,
                textColor = textStyle.color,
                containerColor = myTextStyleExtra.addColor,
                borderWidth = 6f,
                modifier = modifier
            )
        }
        2 -> {
            TextWithContour(
                text = text,
                textColor = myTextStyleExtra.addColor,
                fontSize = textStyle.fontSize,
                contourColor = textStyle.color,
                contourWidth = 8f,
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGetTextStyle(){
    QuizzerAndroidTheme {
        GetTextStyle(
            text = "Hello World",
            modifier = Modifier,
            myTextStyleExtra = TextStyleExtra(
                addColor = Color.Black,
                contourStyle = 0
            ),
            textStyle =TextStyle(
                fontFamily = getFontFamily(0),
                color = Color.Black,
                fontSize = 24.sp,
            ),
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewGetTextStyleWithBorder(){
    QuizzerAndroidTheme {
        GetTextStyle(
            text = "Hello World, 가나다라마바사",
            myTextStyleExtra = TextStyleExtra(
                addColor = MaterialTheme.colorScheme.primaryContainer,
                contourStyle = 1
            ),
            textStyle =TextStyle(
                fontFamily = getFontFamily(0),
                color = Color.Black,
                fontSize = 24.sp,
            ),
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewGetTextStyleWithContour(){
    QuizzerAndroidTheme {
        GetTextStyle(
            text = "Hello World",
            myTextStyleExtra = TextStyleExtra(
                addColor = Color.White,
                contourStyle = 2
            ),
            textStyle =TextStyle(
                fontFamily = getFontFamily(0),
                color = Color.Black,
                fontSize = 24.sp,
            ),
        )
    }
}

fun Color.flipAlpha(): Color {
    val newAlpha = when {
        this.alpha < 0.5f -> this.alpha + 0.5f
        else -> this.alpha - 0.5f
    }
    return this.copy(alpha = newAlpha)
}

fun Modifier.getBorder(borderStyle: Int, borderColor1: Color, colorBrush: Brush? = null): Modifier {
    return when (borderStyle) {
        1 -> this.then(Modifier.drawBehind {
            val strokeWidth = 2.dp.toPx()
            val y = size.height - strokeWidth / 2
            drawLine(
                color = borderColor1,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth
            )
        })
        2 -> this.then(Modifier.border(width = 2.dp, color = borderColor1, shape = RoundedCornerShape(4.dp)))
        3 -> {
            if(colorBrush == null) this
            else
                this.then(Modifier.border(width = 2.dp, brush = colorBrush, shape = RoundedCornerShape(4.dp)))
        }
        else -> this
    }
}

fun getColor(colorScheme: ColorScheme, color: Int): List<Color> {
    return when (color) {
        0 -> listOf(Color.Transparent, colorScheme.primary)
        1 -> listOf(Color.Transparent, colorScheme.secondary)
        2 -> listOf(Color.Transparent, colorScheme.tertiary)
        3 -> listOf(Color.Transparent, colorScheme.onSurface)
        4 -> listOf(Color.Transparent, colorScheme.error)
        5 -> listOf(colorScheme.primaryContainer, colorScheme.onPrimaryContainer)
        6 -> listOf(colorScheme.secondaryContainer, colorScheme.onSecondaryContainer)
        7 -> listOf(colorScheme.tertiaryContainer, colorScheme.onTertiaryContainer)
        8 -> listOf(colorScheme.errorContainer, colorScheme.onErrorContainer)
        9 -> listOf(colorScheme.surfaceDim, colorScheme.onSurface)
        else -> listOf(colorScheme.background, colorScheme.primary)
    }
}

@Composable
fun TextWithBorder(
    text: String,
    textColor: Color,
    containerColor: Color,
    borderWidth: Float,
    fontSize: TextUnit = 24.sp,
    modifier: Modifier = Modifier
) {
    val outlineColor = Color.Red
    BasicText(
        text = text,
        style = TextStyle(
            color = Color.Transparent,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default
        ),
        modifier = modifier
            .drawTextWithShadow(
                text,
                textColor = containerColor,
                contourColor = textColor,
                outlineColor = outlineColor,
                borderWidth,
                fontSize = fontSize,
            )
    )
}

fun Modifier.drawTextWithShadow(
    text: String,
    textColor: Color,
    contourColor: Color,
    outlineColor: Color,
    contourWidth: Float,
    fontSize: TextUnit = 24.sp,
):Modifier = this.then(
    Modifier.drawWithCache {
        // Setup only once per input change
        val textPx = fontSize.toPx()
        val layoutWidth = size.width.toInt()

        val contourPaint = TextPaint().apply {
            color = textColor.toArgb()
            style = Paint.Style.STROKE
            strokeWidth = contourWidth
            this.textSize = textPx
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }

        val fillPaint = TextPaint().apply {
            color = contourColor.toArgb()
            this.textSize = textPx
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }

        val shadowPaint = TextPaint().apply {
            color = outlineColor.toArgb()
            strokeWidth = contourWidth + 2
            this.textSize = textPx
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }

        val contourLayout =
            StaticLayout.Builder.obtain(text, 0, text.length, contourPaint, layoutWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)
                .build()

        val fillLayout = StaticLayout.Builder.obtain(text, 0, text.length, fillPaint, layoutWidth)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()

        val shadowLayout =
            StaticLayout.Builder.obtain(text, 0, text.length, shadowPaint, layoutWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)
                .build()

        // Draw only when needed
        onDrawBehind {
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.apply {
                    save()
                    shadowLayout.draw(this)
                    restore()

                    save()
                    contourLayout.draw(this)
                    restore()

                    save()
                    fillLayout.draw(this)
                    restore()
                }
            }
        }
    }
)

@Composable
fun TextWithContour(
    text: String,
    textColor: Color,
    fontSize: TextUnit = 24.sp,
    contourColor: Color,
    contourWidth: Float,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .drawTextWithContour(text, textColor, contourColor, contourWidth,
                fontSize = fontSize,)
    ){
        Text(
            text = text,
            style = TextStyle(
                color = Color.Transparent,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default
            ),
        )
    }
}

fun Modifier.drawTextWithContour(
    text: String,
    textColor: Color,
    contourColor: Color,
    contourWidth: Float,
    fontSize: TextUnit = 24.sp,
): Modifier = this.then(
    Modifier.drawWithCache {
        val textPx = fontSize.toPx()

        val contourPaint = TextPaint().apply {
            color = contourColor.toArgb()
            style = Paint.Style.STROKE
            strokeWidth = contourWidth
            textSize = textPx
            isAntiAlias = true
        }

        val fillPaint = TextPaint().apply {
            color = textColor.toArgb()
            textSize = textPx
            isAntiAlias = true
        }

        lateinit var strokeLayout: StaticLayout
        lateinit var fillLayout: StaticLayout

        onDrawBehind {
            // You can only access `size.width` here
            val layoutWidth = size.width.toInt()

            strokeLayout = StaticLayout.Builder.obtain(text, 0, text.length, contourPaint, layoutWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)
                .build()

            fillLayout = StaticLayout.Builder.obtain(text, 0, text.length, fillPaint, layoutWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)
                .build()

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.apply {
                    save()
                    strokeLayout.draw(this)
                    restore()

                    save()
                    fillLayout.draw(this)
                    restore()
                }
            }
        }
    }
)
