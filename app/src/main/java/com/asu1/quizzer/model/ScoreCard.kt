package com.asu1.quizzer.model

import androidx.compose.material3.ColorScheme
import com.asu1.quizzer.ui.theme.LightColorScheme
import com.asu1.quizzer.util.basicShader
import com.asu1.quizzer.util.leftBottomShader
import com.asu1.quizzer.util.bottomShader
import com.asu1.quizzer.util.centerShader
import com.asu1.quizzer.util.horizontalWaveShader
import com.asu1.quizzer.util.repeatShader
import com.asu1.quizzer.util.verticalWaveShader

enum class ShaderType(val shaderName: String) {
    Brush1("No Shader"),
    Brush2("Left Bottom"),
    Brush3("Bottom"),
    Brush4("Center"),
    Brush5("Repeat"),
    Brush6("Vertical Wave"),
    Brush7("Horizontal Wave");

    fun getShader(): String {
        return when(this){
            Brush1 -> basicShader
            Brush2 -> leftBottomShader
            Brush3 -> bottomShader
            Brush4 -> centerShader
            Brush5 -> repeatShader
            Brush6 -> verticalWaveShader
            Brush7 -> horizontalWaveShader
        }
    }
}

data class ScoreCard (
    var title: String = "",
    var creator: String = "",
    var score: Int = 100,
    var background: ImageColor = ImageColor(LightColorScheme.primary, ByteArray(0), LightColorScheme.secondary, ImageColorState.COLOR2),
    var size: Float = 0.3f,
    var xRatio: Float = 0.5f,
    var yRatio: Float = 0.5f,
    var imageStateval : Int = 0,
    var colorScheme: ColorScheme = LightColorScheme,
    var shaderType: ShaderType = ShaderType.Brush1,
){

}