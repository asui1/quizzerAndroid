package com.asu1.quiz.ui.textStyleManager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.asu1.resources.getFontFamily


data class TextStyleExtra(
    val addColor: Color,
    val contourStyle: Int,
)

abstract class BaseTextStyleManager {
    var extra: TextStyleExtra by mutableStateOf(
        TextStyleExtra(Color.Black, 0)
    )
    var textStyle: TextStyle by mutableStateOf(TextStyle())
    var borderModifier: Modifier by mutableStateOf(Modifier)

    fun update(style: List<Int>, colorScheme: ColorScheme, isDark: Boolean = false) {
        updateFontFamily(style[0])
        updateTextColor(style[1], colorScheme)
        updateBorderStyle(style[0], style[2], colorScheme)

        val bgColor = getColor(colorScheme, style[1]).first()
        updateContourStyle(style[4], bgColor, isDark)
    }

    fun update(style: List<Int>, colorScheme: ColorScheme, targetIndex: Int){
        when(targetIndex){
            0 -> updateFontFamily(style[0])
            1 -> updateTextColor(style[1], colorScheme)
            2 -> updateBorderStyle(style[0], style[2], colorScheme)
            4 -> {
                val bgColor = getColor(colorScheme, style[1]).first()
                updateContourStyle(style[4], bgColor, false)
            }
        }
    }

    fun updateFontFamily(index: Int) {
        val fontFamily = getFontFamily(index)
        textStyle = textStyle.copy(fontFamily = fontFamily)
    }

    fun updateTextColor(index: Int, colorScheme: ColorScheme) {
        val (_, contentColor) = getColor(colorScheme, index)
        textStyle = textStyle.copy(color = contentColor)
    }

    fun updateContourStyle(index: Int, bgColor: Color, isDark: Boolean) {
        val addColor = when (index) {
            0 -> Color.Transparent
            1 -> if (bgColor == Color.Transparent) textStyle.color else bgColor
            2 -> if (bgColor == Color.Transparent) if (isDark) Color.Black else Color.White else bgColor
            else -> Color.Transparent
        }
        extra = TextStyleExtra(addColor = addColor, contourStyle = index)
    }

    fun updateBorderStyle(colorIndex: Int, index: Int, colorScheme: ColorScheme) {
        val (backgroundColor, _) = getColor(colorScheme, colorIndex)
        val brush = Brush.linearGradient(
            listOf(colorScheme.primary, colorScheme.secondary)
        )
        borderModifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
            .getBorder(index, colorScheme.outline, brush)
            .padding(8.dp)
    }

    @Composable
    fun GetTextComposable(text: String, modifier: Modifier = Modifier){
        GetTextStyle(
            text = text,
            modifier = modifier.then(borderModifier),
            myTextStyleExtra = extra,
            textStyle = textStyle,
        )
    }
}