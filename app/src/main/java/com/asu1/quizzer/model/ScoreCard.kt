package com.asu1.quizzer.model

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.quizzer.ui.theme.LightColorScheme
import com.asu1.quizzer.util.basicShader
import com.asu1.quizzer.util.customShader

enum class ShaderBrush {
    Brush1,
    Brush2,
    Brush3;

    fun next(): ShaderBrush {
        return when(this){
            Brush1 -> Brush2
            Brush2 -> Brush3
            Brush3 -> Brush1
        }
    }
    fun getShader(): String {
        return when(this){
            Brush1 -> customShader
            Brush2 -> customShader
            Brush3 -> customShader
        }
    }
}

enum class ColorGradient{
    PrimarySecondary,
    PrimaryTertiary,
    SecondaryTertiary,
    SecondaryPrimary,
    TertiaryPrimary,
    TertiarySecondary;

    fun next(): ColorGradient {
        return when(this){
            PrimarySecondary -> PrimaryTertiary
            PrimaryTertiary -> SecondaryTertiary
            SecondaryTertiary -> SecondaryPrimary
            SecondaryPrimary -> TertiaryPrimary
            TertiaryPrimary -> TertiarySecondary
            TertiarySecondary -> PrimarySecondary
        }
    }
    fun getColors(colorScheme: ColorScheme): Pair<Color, Color> {
        return when(this){
            PrimarySecondary -> Pair(colorScheme.primary, colorScheme.secondary)
            PrimaryTertiary -> Pair(colorScheme.primary, colorScheme.tertiary)
            SecondaryTertiary -> Pair(colorScheme.secondary, colorScheme.tertiary)
            SecondaryPrimary -> Pair(colorScheme.secondary, colorScheme.primary)
            TertiaryPrimary -> Pair(colorScheme.tertiary, colorScheme.primary)
            TertiarySecondary -> Pair(colorScheme.tertiary, colorScheme.secondary)
        }
    }
}

data class ScoreCard (
    var title: String = "",
    var creator: String = "",
    var score: Int = 100,
    var background: ImageColor = ImageColor(Color.White, ByteArray(0), Color.White, ImageColorState.COLOR2),
    var size: Float = 0.3f,
    var xRatio: Float = 0.5f,
    var yRatio: Float = 0.5f,
    var imageStateval : Int = 0,
    var colorScheme: ColorScheme = LightColorScheme,
    var colorGradient: ColorGradient = ColorGradient.PrimarySecondary,
    var shaderBrush: ShaderBrush = ShaderBrush.Brush1,
){

}