package com.asu1.models.quiz

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.imagecolor.ColorSchemeSerializer
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import kotlinx.serialization.Serializable

@Serializable
data class QuizTheme(
    var backgroundImage: ImageColor = ImageColor(
        imageData = byteArrayOf(),
        color = Color.White,
        color2 = Color.White,
        colorGradient = Color.White,
        state = ImageColorState.COLOR
    ),
    var questionTextStyle: List<Int> = listOf(0, 0, 1, 0, 2),
    var bodyTextStyle: List<Int> = listOf(0, 0, 2, 1, 0),
    var answerTextStyle: List<Int> = listOf(0, 0, 0, 2, 0),
    @Serializable(with = ColorSchemeSerializer::class) var colorScheme: ColorScheme = com.asu1.resources.LightColorScheme,
)