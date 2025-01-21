package com.asu1.quizzer.composables

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
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

@Composable
fun GetTextStyle(text: String, modifier: Modifier = Modifier, textStyle: com.asu1.quizzer.model.TextStyle) {
    when (textStyle.contourStyle) {
        0 -> Text(
            text = text,
            style = TextStyle(
                fontFamily = textStyle.fontFamily,
                color = textStyle.contentColor,
                fontSize = textStyle.fontSize,
            ),
            modifier = modifier
                .background(textStyle.backgroundColor, shape = RoundedCornerShape(4.dp))
                .then(textStyle.borderModifier)
                .padding(8.dp)
        )
        1 -> {
            TextWithBorder(
                text = text,
                fontFamily = textStyle.fontFamily,
                fontSize = textStyle.fontSize,
                textColor = textStyle.contentColor,
                containerColor = textStyle.addColor,
                borderWidth = 6f,
                modifier = modifier
                    .then(textStyle.borderModifier)
                    .padding(8.dp)
            )
        }
        2 -> {
            TextWithContour(
                text = text,
                textColor = textStyle.addColor,
                fontSize = textStyle.fontSize,
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
    val fontFamily = com.asu1.resources.getFontFamily(style[0])
    val color = style[1]
    val borderStyle = style[2]
    val (backgroundColor, contentColor) = getColor(colorScheme, color)
    val colorBrush = remember(colorScheme.primary, colorScheme.secondary){ Brush.linearGradient(colors = listOf(colorScheme.primary, colorScheme.secondary))}
    val borderModifier = Modifier.getBorder(borderStyle, MaterialTheme.colorScheme.outline, colorBrush)

    val fontSize = when(style[3]){
        0 -> 24.sp // question
        1 -> 20.sp // answer
        2 -> 16.sp // body
        else -> 16.sp
    }
    val contourStyle = style[4]
    val addColor = when(contourStyle){
        0 -> Color.Transparent
        1 -> if(backgroundColor == Color.Transparent) if(isSystemInDarkTheme()) Color.Black else Color.White else backgroundColor
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
                .background(backgroundColor, shape = RoundedCornerShape(4.dp))
                .then(borderModifier)
                .padding(8.dp)
        )
        1 -> {
            TextWithBorder(
                text = text,
                textColor = contentColor,
                containerColor = addColor,
                fontSize = fontSize,
                borderWidth = 8f,
                fontFamily = fontFamily,
                modifier = modifier
                    .then(borderModifier)
                    .padding(8.dp)
            )
        }
        2 -> {
            TextWithContour(
                text = text,
                textColor = addColor,
                fontSize = fontSize,
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
    com.asu1.resources.QuizzerAndroidTheme {
        GetTextStyle(
            text = "Hello World",
            textStyle = com.asu1.quizzer.model.TextStyle(
                fontFamily = com.asu1.resources.getFontFamily(0),
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
    com.asu1.resources.QuizzerAndroidTheme {
        GetTextStyle(
            text = "Hello World, 가나다라마바사",
            textStyle = com.asu1.quizzer.model.TextStyle(
                fontFamily = com.asu1.resources.getFontFamily(0),
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                backgroundColor = Color.Transparent,
                addColor = MaterialTheme.colorScheme.primaryContainer,
                borderModifier = Modifier,
                fontSize = 24.sp,
                contourStyle = 1
            )
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewGetTextStyleWithContour(){
    com.asu1.resources.QuizzerAndroidTheme {
        GetTextStyle(
            text = "Hello World",
            textStyle = com.asu1.quizzer.model.TextStyle(
                fontFamily = com.asu1.resources.getFontFamily(0),
                contentColor = Color.Black,
                backgroundColor = Color.Transparent,
                addColor = Color.White,
                borderModifier = Modifier,
                fontSize = 24.sp,
                contourStyle = 2
            )
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
    fontFamily: FontFamily,
    containerColor: Color,
    borderWidth: Float,
    fontSize: TextUnit = 24.sp,
    modifier: Modifier = Modifier
) {
    val outlineColor = MaterialTheme.colorScheme.outline
    BasicText(
        text = text,
        style = TextStyle(
            color = Color.Transparent,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default
        ),
        modifier = modifier
            .drawBehind {
                drawTextWithShadow(
                    text,
                    textColor = containerColor,
                    contourColor = textColor,
                    outlineColor = outlineColor,
                    borderWidth,
                    fontFamily = fontFamily,
                    fontSize = fontSize,
                    maxWidth = size.width,)
            }
    )
}

fun DrawScope.drawTextWithShadow(
    text: String,
    textColor: Color,
    contourColor: Color,
    outlineColor: Color,
    contourWidth: Float,
    maxWidth: Float,
    fontFamily: FontFamily,
    fontSize: TextUnit = 24.sp,
) {
    drawIntoCanvas { canvas ->
        val contourPaint = android.graphics.Paint().apply {
            this.color = textColor.toArgb()
            this.style = android.graphics.Paint.Style.STROKE
            this.strokeWidth = contourWidth
            this.textSize = fontSize.toPx()
            this.isAntiAlias = true
        }
        val textPaint = android.graphics.Paint().apply {
            this.color = contourColor.toArgb()
            this.textSize = fontSize.toPx()
            this.isAntiAlias = true
        }
        val shadowPaint = android.graphics.Paint().apply {
            this.color = outlineColor.toArgb()
            this.strokeWidth = contourWidth + 2
            this.textSize = fontSize.toPx()
            this.isAntiAlias = true
        }

        val contourLayout = StaticLayout.Builder.obtain(text, 0, text.length, TextPaint(contourPaint), maxWidth.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()

        val textLayout = StaticLayout.Builder.obtain(text, 0, text.length, TextPaint(textPaint), maxWidth.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()

        val shadowLayout = StaticLayout.Builder.obtain(text, 0, text.length, TextPaint(shadowPaint), maxWidth.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()

        canvas.nativeCanvas.save()
        shadowLayout.draw(canvas.nativeCanvas)
        canvas.nativeCanvas.restore()

        canvas.nativeCanvas.save()
        contourLayout.draw(canvas.nativeCanvas)
        canvas.nativeCanvas.restore()

        canvas.nativeCanvas.save()
        textLayout.draw(canvas.nativeCanvas)
        canvas.nativeCanvas.restore()
    }
}

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
            .drawBehind {
                drawTextWithContour(text, textColor, contourColor, contourWidth,
                    fontSize = fontSize,
                    maxWidth = size.width,)
            }
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

fun DrawScope.drawTextWithContour(
    text: String, textColor: Color,
    contourColor: Color, contourWidth: Float,
    maxWidth: Float,
    fontSize: TextUnit = 24.sp,
) {
    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint().apply {
            this.color = contourColor.toArgb()
            this.style = android.graphics.Paint.Style.STROKE
            this.strokeWidth = contourWidth
            this.textSize = fontSize.toPx()
            this.isAntiAlias = true
        }
        val textPaint = android.graphics.Paint().apply {
            this.color = textColor.toArgb()
            this.textSize = fontSize.toPx()
            this.isAntiAlias = true
        }
        val textLayout = StaticLayout.Builder.obtain(text, 0, text.length, TextPaint(paint), maxWidth.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()

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
