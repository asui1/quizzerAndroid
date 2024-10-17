package com.asu1.quizzer.model

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.quizzer.ui.theme.LightColorScheme

data class ScoreCard (
    var title: String = "",
    var creator: String = "",
    var score: Int = 100,
    var background: ImageColor = ImageColor(Color.White, ByteArray(0), Color.White, ImageColorState.COLOR),
    var size: Float = 0.3f,
    var xRatio: Float = 0.5f,
    var yRatio: Float = 0.5f,
    var imageStateval : Int = 0,
    var colorScheme: ColorScheme = LightColorScheme,
){

}