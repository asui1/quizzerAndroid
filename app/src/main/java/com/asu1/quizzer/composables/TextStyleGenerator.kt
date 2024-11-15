package com.asu1.quizzer.composables

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.ui.theme.getFontFamily

@Composable
fun GetTextStyle(text: String, modifier: Modifier = Modifier, textStyle: com.asu1.quizzer.model.TextStyle) {
    when (textStyle.contourStyle) {
        0 -> Text(
            text = text,
            style = TextStyle(
                fontFamily = textStyle.fontFamily,
                color = textStyle.contentColor,
                fontSize = textStyle.fontSize
            ),
            modifier = modifier
                .then(textStyle.borderModifier)
                .padding(8.dp)
        )
        1 -> {
            TextWithBorder(
                text = text,
                textColor = textStyle.contentColor,
                borderColor = textStyle.addColor,
                borderWidth = 4f,
                modifier = modifier
                    .then(textStyle.borderModifier)
                    .padding(8.dp)
            )
        }
        2 -> {
            TextWithContour(
                text = text,
                textColor = textStyle.addColor,
                contourColor = textStyle.contentColor,
                contourWidth = 8f,
                modifier = modifier
                    .then(textStyle.borderModifier)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun GetTextStyle(text: String, style: List<Int>, colorScheme: ColorScheme, modifier: Modifier = Modifier) {
    // -> FONT FAMILY, Color, BoderStyle, FontWeight
    val fontFamily = getFontFamily(style[0])
    val color = style[1]
    val borderStyle = style[2]
    val (backgroundColor, contentColor) = getColor(colorScheme, color)
    val borderModifier = Modifier.getBorder(borderStyle)

    val fontSize = when(style[3]){
        0 -> 24.sp // question
        1 -> 20.sp // answer
        2 -> 16.sp // body
        else -> 16.sp
    }
    val contourStyle = style[4]
    val addColor = when(contourStyle){
        0 -> Color.Transparent
        1 -> if(backgroundColor == Color.Transparent) contentColor.flipAlpha() else backgroundColor
        2 -> if(backgroundColor == Color.Transparent) if(isSystemInDarkTheme()) Color.Black else Color.White else backgroundColor
        else -> Color.Transparent
    }
    when (contourStyle) {
        0 -> Text(
            text = text,
            style = TextStyle(
                fontFamily = fontFamily,
                color = contentColor,
                fontSize = fontSize
            ),
            modifier = modifier
                .then(borderModifier)
                .padding(8.dp)
        )
        1 -> {
            TextWithBorder(
                text = text,
                textColor = contentColor,
                borderColor = addColor,
                borderWidth = 4f,
                modifier = modifier
                    .then(borderModifier)
                    .padding(8.dp)
            )
        }
        2 -> {
            TextWithContour(
                text = text,
                textColor = addColor,
                contourColor = contentColor,
                contourWidth = 8f,
                modifier = modifier
                    .then(borderModifier)
                    .padding(8.dp)
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
            textStyle = com.asu1.quizzer.model.TextStyle(
                fontFamily = getFontFamily(0),
                contentColor = Color.Black,
                backgroundColor = Color.Transparent,
                addColor = Color.Black,
                borderModifier = Modifier,
                fontSize = 24.sp,
                contourStyle = 0
            )
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewGetTextStyleWithBorder(){
    QuizzerAndroidTheme {
        GetTextStyle(
            text = "Hello World",
            textStyle = com.asu1.quizzer.model.TextStyle(
                fontFamily = getFontFamily(0),
                contentColor = Color.Black,
                backgroundColor = Color.Transparent,
                addColor = Color.Red,
                borderModifier = Modifier,
                fontSize = 24.sp,
                contourStyle = 1
            )
        )    }
}
@Preview(showBackground = true)
@Composable
fun PreviewGetTextStyleWithContour(){
    QuizzerAndroidTheme {
        GetTextStyle(
            text = "Hello World",
            textStyle = com.asu1.quizzer.model.TextStyle(
                fontFamily = getFontFamily(0),
                contentColor = Color.Black,
                backgroundColor = Color.Transparent,
                addColor = Color.White,
                borderModifier = Modifier,
                fontSize = 24.sp,
                contourStyle = 2
            )
        )    }
}

fun Color.flipAlpha(): Color {
    val newAlpha = when {
        this.alpha < 0.5f -> this.alpha + 0.5f
        else -> this.alpha - 0.5f
    }
    return this.copy(alpha = newAlpha)
}

fun Modifier.getBorder(borderStyle: Int): Modifier {
    return when (borderStyle) {
        1 -> this.then(Modifier.drawBehind {
            val strokeWidth = 2.dp.toPx()
            val y = size.height - strokeWidth / 2
            drawLine(
                color = Color.Black,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth
            )
        })
        2 -> this.then(Modifier.border(width = 2.dp, brush = SolidColor(Color.Black), shape = RoundedCornerShape(4.dp)))
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
    borderColor: Color,
    borderWidth: Float,
    modifier: Modifier = Modifier
) {
    BasicText(
        text = text,
        style = TextStyle(
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default
        ),
        modifier = modifier
            .drawBehind {
                drawTextWithContour(
                    text, textColor, borderColor,
                    borderWidth,
                    maxWidth = size.width,
                    withoutDescent = false)
            }
    )
}

@Composable
fun TextWithContour(
    text: String,
    textColor: Color,
    contourColor: Color,
    contourWidth: Float,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .drawBehind {
                drawTextWithContour(text, textColor, contourColor, contourWidth,
                    maxWidth = size.width,
                    withoutDescent = true)
            }
    ){
        Text(
            text = text,
            style = TextStyle(
                color = Color.Transparent,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default
            ),
        )
    }
}

fun DrawScope.drawTextWithContour(
    text: String, textColor: Color,
    contourColor: Color, contourWidth: Float,
    maxWidth: Float,
    withoutDescent: Boolean = true,
    paddingSize: Dp = 4.dp) {
    drawIntoCanvas { canvas ->
        val textSizePx = 24.sp.toPx()
        val paint = android.graphics.Paint().apply {
            this.color = contourColor.toArgb()
            this.style = android.graphics.Paint.Style.STROKE
            this.strokeWidth = contourWidth
            this.textSize = 24.sp.toPx()
            this.isAntiAlias = true
        }
        val textPaint = android.graphics.Paint().apply {
            this.color = textColor.toArgb()
            this.textSize = 24.sp.toPx()
            this.isAntiAlias = true
        }
        val textLayout = StaticLayout.Builder.obtain(text, 0, text.length, TextPaint(paint), maxWidth.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()
        val x = 0f
        val y = if(withoutDescent) textSizePx - paint.descent() else textSizePx

        canvas.nativeCanvas.save()
        textLayout.draw(canvas.nativeCanvas)
        canvas.nativeCanvas.restore()

        val textLayoutPaint = StaticLayout.Builder.obtain(text, 0, text.length, TextPaint(textPaint), maxWidth.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()

        canvas.nativeCanvas.save()
        textLayoutPaint.draw(canvas.nativeCanvas)
        canvas.nativeCanvas.restore()
    }
}
