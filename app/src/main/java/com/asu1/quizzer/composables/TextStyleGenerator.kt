package com.asu1.quizzer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.ui.theme.getFontFamily

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

    Text(
        text = text,
        style = TextStyle(
            fontFamily = fontFamily,
            color = contentColor,
            fontSize = fontSize
        ),
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
            .then(borderModifier)
            .padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewGetTextStyle(){
    QuizzerAndroidTheme {
        GetTextStyle(
            text = "Hello World",
            style = listOf(0, 0, 2, 0),
            colorScheme = MaterialTheme.colorScheme
        )
    }
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

fun Color.invert(): Color {
    return Color(
        red = 1f - this.red,
        green = 1f - this.green,
        blue = 1f - this.blue,
        alpha = this.alpha
    )
}