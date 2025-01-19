package com.asu1.quizzer.model

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.asu1.quizzer.composables.GetTextStyle
import com.asu1.quizzer.composables.flipAlpha
import com.asu1.quizzer.composables.getBorder
import com.asu1.quizzer.composables.getColor
import com.asu1.resources.LightColorScheme
import com.asu1.resources.getFontFamily

enum class TextStyles{
    QUESTION,
    ANSWER,
    BODY,
}

data class TextStyle(
    val fontFamily: FontFamily,
    val contentColor: Color,
    val backgroundColor: Color,
    val addColor: Color,
    val borderModifier: Modifier,
    val fontSize: TextUnit,
    val contourStyle: Int,
)

class TextStyleManager {
    private var questionStyle: TextStyle = TextStyle(FontFamily.Default, Color.Black, Color.Transparent, Color.Black, Modifier, 24.sp, 0)
    private var bodyStyle: TextStyle = TextStyle(FontFamily.Default, Color.Black, Color.Transparent, Color.Black, Modifier, 24.sp, 0)
    private var answerStyle: TextStyle = TextStyle(FontFamily.Default, Color.Black, Color.Transparent, Color.Black, Modifier, 24.sp, 0)

    private var currentColorScheme: ColorScheme = com.asu1.resources.LightColorScheme
    private var colorBrush: Brush = Brush.linearGradient(colors = listOf())

    fun initTextStyleManager(colorScheme: ColorScheme, questionStyle: List<Int>, answerStyle: List<Int>, bodyStyle: List<Int>){
        currentColorScheme = colorScheme
        updateStyle(questionStyle, TextStyles.QUESTION)
        updateStyle(answerStyle, TextStyles.ANSWER)
        updateStyle(bodyStyle, TextStyles.BODY)
    }

    fun updateStyle(style: List<Int>, textStyles: TextStyles, isDark: Boolean = false){
        val fontFamily = com.asu1.resources.getFontFamily(style[0])
        val color = style[1]
        val borderStyle = style[2]
        val (backgroundColor, contentColor) = getColor(currentColorScheme, color)
        colorBrush = Brush.linearGradient(colors = listOf(currentColorScheme.primary, currentColorScheme.secondary))
        val borderModifier = Modifier.getBorder(borderStyle, currentColorScheme.outline, colorBrush)

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
            2 -> if(backgroundColor == Color.Transparent) if(isDark) Color.Black else Color.White else backgroundColor
            else -> Color.Transparent
        }

        when(textStyles){
            TextStyles.QUESTION -> questionStyle = TextStyle(fontFamily, contentColor, backgroundColor, addColor, borderModifier, fontSize, contourStyle)
            TextStyles.ANSWER -> answerStyle = TextStyle(fontFamily, contentColor, backgroundColor, addColor, borderModifier, fontSize, contourStyle)
            TextStyles.BODY -> bodyStyle = TextStyle(fontFamily, contentColor, backgroundColor, addColor, borderModifier, fontSize, contourStyle)
        }
    }

    @Composable
    fun GetTextComposable(textStyles: TextStyles, text: String, modifier: Modifier = Modifier){
        when(textStyles){
            TextStyles.QUESTION -> GetTextStyle(text, modifier, questionStyle)
            TextStyles.BODY -> GetTextStyle(text, modifier, bodyStyle)
            TextStyles.ANSWER -> GetTextStyle(text, modifier, answerStyle)
        }
    }
}