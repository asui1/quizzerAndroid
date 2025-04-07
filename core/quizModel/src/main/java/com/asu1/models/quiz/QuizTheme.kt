package com.asu1.models.quiz

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.imagecolor.ColorSchemeSerializer
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import kotlinx.serialization.Serializable

interface QuizThemeInterface {
    val backgroundImage: ImageColor
    val questionTextStyle: List<Int>
    val bodyTextStyle: List<Int>
    val answerTextStyle: List<Int>
    val colorScheme: ColorScheme
}

@Serializable
data class QuizTheme(
    override var backgroundImage: ImageColor = ImageColor(
        color = Color.White,
        color2 = Color.White,
        colorGradient = Color.White,
        state = ImageColorState.COLOR
    ),
    override var questionTextStyle: List<Int> = listOf(0, 5, 1, 0, 0),
    override var bodyTextStyle: List<Int> = listOf(0, 9, 3, 1, 0),
    override var answerTextStyle: List<Int> = listOf(0, 0, 0, 2, 0),
    @Serializable(with = ColorSchemeSerializer::class) override var colorScheme: ColorScheme = com.asu1.resources.LightColorScheme,
) : QuizThemeInterface