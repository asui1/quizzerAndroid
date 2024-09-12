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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.ui.theme.getFontFamily

@Composable
fun GetTextStyle(text: String, style: List<Int>, colorScheme: ColorScheme) {
    // -> FONT FAMILY, Color, BoderStyle, FontWeight
    val fontFamily = getFontFamily(style[0])
    val color = style[1]
    val borderStyle = style[2]
    var (backgroundColor, contentColor) = getColor(colorScheme, color)
    if(backgroundColor == null) backgroundColor = Color.Transparent
    if(contentColor == null) contentColor = Color.Black
    val borderModifier = when (borderStyle) {
        1 -> Modifier.border(width = 2.dp, brush = SolidColor(colorScheme.outline), shape = RoundedCornerShape(0.dp))
        2 -> Modifier.border(width = 2.dp, brush = SolidColor(colorScheme.outline), shape = RoundedCornerShape(4.dp))
        else -> Modifier
    }

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
        modifier = Modifier
            .background(color = backgroundColor)
            .then(borderModifier)
            .padding(4.dp)
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

fun getColor(colorScheme: ColorScheme, color: Int): List<Color?> {
    return when (color) {
        0 -> listOf(null, colorScheme.primary)
        1 -> listOf(null, colorScheme.secondary)
        2 -> listOf(null, colorScheme.tertiary)
        3 -> listOf(null, colorScheme.onSurface)
        4 -> listOf(colorScheme.primary, colorScheme.onPrimary)
        5 -> listOf(colorScheme.secondary, colorScheme.onSecondary)
        6 -> listOf(colorScheme.tertiary, colorScheme.onTertiary)
        7 -> listOf(colorScheme.primaryContainer, colorScheme.onPrimaryContainer)
        8 -> listOf(colorScheme.secondaryContainer, colorScheme.onSecondaryContainer)
        9 -> listOf(colorScheme.tertiaryContainer, colorScheme.onTertiaryContainer)
        else -> listOf(null, colorScheme.primary)
    }
}