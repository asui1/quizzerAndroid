package com.asu1.quizzer.quizCreateUtils.quizTheme

import androidx.compose.ui.graphics.Color
import com.asu1.imagecolor.BackgroundBase
import com.asu1.imagecolor.Effect
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.scorecard.ScoreCard

fun hexColor(hex: String): Color =
    Color(android.graphics.Color.parseColor("#$hex"))

val codingScoreCard = ScoreCard(
    textColor = hexColor("32479d"),
    background = ImageColor(
        backgroundBase = BackgroundBase.LAB_COZY,
        color = hexColor("000000"),
        color2 = hexColor("cfe5ff"),
        effect = Effect.NONE,
    ),
)

val codingQuizTheme = QuizTheme(
    backgroundImage = ImageColor(
        color         = hexColor("3A53D3"),    // primaryColor
        color2        = hexColor("000000"),    // backgroundColorFilter
        colorGradient = hexColor("cfe5ff"),    // effectColor
        state         = ImageColorState.COLOR
    ),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle     = listOf(3, 0, 0),
    answerTextStyle   = listOf(1, 6, 0),
    colorScheme       = com.asu1.resources.LightColorScheme.copy(
        primary   = hexColor("3A53D3"),
        onPrimary = hexColor("32479d"),
        background = hexColor("000000"),
        secondaryContainer = hexColor("cfe5ff")
    )
)

val androidQuizTheme = QuizTheme(
    backgroundImage = ImageColor(
        color         = hexColor("ace1af"),    // primaryColor
        color2        = hexColor("93E9BE"),    // backgroundColorFilter
        colorGradient = hexColor("cfe5ff"),    // effectColor
        state         = ImageColorState.COLOR
    ),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle     = listOf(3, 0, 0),
    answerTextStyle   = listOf(1, 5, 0),
    colorScheme       = com.asu1.resources.LightColorScheme.copy(
        primary   = hexColor("ace1af"),
        onPrimary = hexColor("F5E6DA"),
        background = hexColor("93E9BE"),
        secondaryContainer = hexColor("cfe5ff")
    )
)

val androidScoreCard = ScoreCard(
    textColor = hexColor("F5E6DA"),
    background = ImageColor(
        backgroundBase = BackgroundBase.LAB_COZY,
        color = hexColor("93E9BE"),
        color2 = hexColor("cfe5ff"),
        effect = Effect.NONE,
    ),
)

