package com.asu1.models.quiz

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.asu1.imagecolor.ColorSchemeSerializer
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.utils.ImmutableListSerializer
import kotlinx.collections.immutable.PersistentList
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
    override var questionTextStyle: List<Int> = listOf(0, 0, 1, 0, 0),
    override var bodyTextStyle: List<Int> = listOf(0, 0, 2, 1, 0),
    override var answerTextStyle: List<Int> = listOf(0, 0, 0, 2, 0),
    @Serializable(with = ColorSchemeSerializer::class) override var colorScheme: ColorScheme = com.asu1.resources.LightColorScheme,
) : QuizThemeInterface

@Immutable
@Serializable
data class QuizThemeViewer(
    override val backgroundImage: ImageColor,
    @Serializable(with = ImmutableListSerializer::class) override val questionTextStyle: PersistentList<Int>,
    @Serializable(with = ImmutableListSerializer::class) override val bodyTextStyle: PersistentList<Int>,
    @Serializable(with = ImmutableListSerializer::class) override val answerTextStyle: PersistentList<Int>,
    @Serializable(with = ColorSchemeSerializer::class) override val colorScheme: ColorScheme,
): QuizThemeInterface