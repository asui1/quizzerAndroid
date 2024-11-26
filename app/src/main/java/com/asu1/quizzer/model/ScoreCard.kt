package com.asu1.quizzer.model

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import cloudShader
import com.asu1.quizzer.R
import com.asu1.quizzer.data.ColorSchemeSerializer
import com.asu1.quizzer.data.ShaderTypeSerializer
import com.asu1.quizzer.ui.theme.LightColorScheme
import com.asu1.quizzer.util.basicShader
import com.asu1.quizzer.util.bottomShader
import com.asu1.quizzer.util.centerShader
import com.asu1.quizzer.util.horizontalWaveShader
import com.asu1.quizzer.util.repeatShader
import com.asu1.quizzer.util.verticalWaveShader
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

enum class ShaderType(val shaderName: Int, val index: Int) {
    Brush1(R.string.no_shader, 0),
    Brush2(R.string.left_bottom, 1),
    Brush3(R.string.bottom, 2),
    Brush4(R.string.center, 3),
    Brush5(R.string.repeat, 4),
    Brush6(R.string.vertical_wave, 5),
    Brush7(R.string.horizontal_wave, 6);

    fun getShader(): String {
        return when(this){
            Brush1 -> basicShader
            Brush2 -> cloudShader
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
    var imageStateval : Int = 0,
    @Contextual var textColor: Color = Color.Black,
    @Serializable(with = ColorSchemeSerializer::class) var colorScheme: ColorScheme = LightColorScheme,
    @Serializable(with = ShaderTypeSerializer::class) var shaderType: ShaderType = ShaderType.Brush1,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScoreCard) return false

        if (title != other.title) return false
        if (solver != other.solver) return false
        if (score != other.score) return false
        if (background != other.background) return false
        if (imageStateval != other.imageStateval) return false
        if (colorScheme != other.colorScheme) return false
        if (shaderType != other.shaderType) return false
        if (textColor != other.textColor) return false
        if (quizUuid != other.quizUuid) return false

        return true
    }
}