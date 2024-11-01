package com.asu1.quizzer.model

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.quizzer.data.ShaderTypeSerializer
import com.asu1.quizzer.ui.theme.LightColorScheme
import com.asu1.quizzer.util.basicShader
import com.asu1.quizzer.util.bottomShader
import com.asu1.quizzer.util.centerShader
import com.asu1.quizzer.util.horizontalWaveShader
import com.asu1.quizzer.util.leftBottomShader
import com.asu1.quizzer.util.repeatShader
import com.asu1.quizzer.util.verticalWaveShader
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

enum class ShaderType(val shaderName: String, val index: Int) {
    Brush1("No Shader", 0),
    Brush2("Left Bottom", 1),
    Brush3("Bottom", 2),
    Brush4("Center", 3),
    Brush5("Repeat", 4),
    Brush6("Vertical Wave", 5),
    Brush7("Horizontal Wave", 6);

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

@Serializable
data class ScoreCard (
    var quizUuid: String? = null,
    var title: String = "",
    var solver: String = "Nickname",
    var score: Float = 100f,
    var background: ImageColor = ImageColor(LightColorScheme.primary, ByteArray(0), LightColorScheme.secondary, ImageColorState.COLOR2),
    var size: Float = 0.3f,
    var xRatio: Float = 0.5f,
    var yRatio: Float = 0.5f,
    var imageStateval : Int = 0,
    @Contextual var textColor: Color = Color.Black,
    @Contextual var colorScheme: ColorScheme = LightColorScheme,
    @Serializable(with = ShaderTypeSerializer::class) var shaderType: ShaderType = ShaderType.Brush1,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScoreCard) return false

        if (title != other.title) return false
        if (solver != other.solver) return false
        if (score != other.score) return false
        if (background != other.background) return false
        if (size != other.size) return false
        if (xRatio != other.xRatio) return false
        if (yRatio != other.yRatio) return false
        if (imageStateval != other.imageStateval) return false
        if (colorScheme != other.colorScheme) return false
        if (shaderType != other.shaderType) return false
        if (textColor != other.textColor) return false
        if (quizUuid != other.quizUuid) return false

        return true
    }
}