package com.asu1.quizzer.model

import androidx.compose.material3.ColorScheme
import com.asu1.quizzer.ui.theme.LightColorScheme
import com.asu1.quizzer.util.basicShader
import com.asu1.quizzer.util.customShader

enum class ShaderType(val shaderName: String) {
    Brush1("No Shader"),
    Brush2("Forth and Back"),
    Brush3("Wave");

    fun getShader(): String {
        return when(this){
            Brush1 -> basicShader
            Brush2 -> customShader
            Brush3 -> basicShader
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