package com.asu1.quizzer.model

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.quizzer.R
import com.asu1.quizzer.data.ColorSchemeSerializer
import com.asu1.quizzer.ui.theme.LightColorScheme
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

enum class ShaderType(val shaderName: Int, val index: Int) {
    Brush1(R.string.left_bottom, 0),
    Brush2(R.string.left, 1),
    Brush3(R.string.top, 2),
    Brush4(R.string.diagonal, 3),
    Brush5(R.string.horizontal, 4),
    Brush6(R.string.vertical, 5),
    Brush7(R.string.top_down, 6);
}

@Serializable
data class ScoreCard (
    var quizUuid: String? = null,
    var title: String = "",
    var solver: String = "Nickname",
    var score: Float = 100f,
    var background: ImageColor = ImageColor(
        imageData = ByteArray(0),
        color = Color.White,
        color2 = Color.White,
        colorGradient = Color.White,
        state = ImageColorState.COLOR,
        backgroundBase = BackgroundBase.SKY,
        effect = Effect.SNOWFLAKES,
        shaderType = ShaderType.Brush1
    ),
    var imageStateval : Int = 0,
    @Contextual var textColor: Color = Color.Black,
    @Serializable(with = ColorSchemeSerializer::class) var colorScheme: ColorScheme = LightColorScheme,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScoreCard) return false

        if (title != other.title) return false
        if (solver != other.solver) return false
        if (score != other.score) return false
        if(background != other.background) return false
        if (imageStateval != other.imageStateval) return false
        if (colorScheme != other.colorScheme) return false
        if (textColor != other.textColor) return false
        if (quizUuid != other.quizUuid) return false

        return true
    }
}

val sampleScoreCard = ScoreCard(
    title = "Sample Quiz",
    solver = "John Doe",
    score = 80f,
    background = ImageColor(
        imageData = ByteArray(0),
        color = Color.White,
        color2 = Color.Black,
        colorGradient = Color.White,
        state = ImageColorState.COLOR,
        backgroundBase = BackgroundBase.SKY,
        effect = Effect.SNOWFLAKES,
        shaderType = ShaderType.Brush1
    ),
    imageStateval = 0,
    textColor = Color.Black,
    colorScheme = LightColorScheme
)