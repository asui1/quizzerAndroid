package com.asu1.models.scorecard

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.imagecolor.BackgroundBase
import com.asu1.imagecolor.ColorSchemeSerializer
import com.asu1.imagecolor.Effect
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.resources.LightColorScheme
import com.asu1.resources.ShaderType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

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