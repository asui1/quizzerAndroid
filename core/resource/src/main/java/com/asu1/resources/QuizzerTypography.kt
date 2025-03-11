package com.asu1.resources

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

data class QuizzerTypography(
    val quizzerTopBarTitle: TextStyle,
    val quizzerIconLabel: TextStyle,
    val quizzerQuizCardListTitle: TextStyle,
    val quizzerQuizCardTitle: TextStyle,
    val quizzerQuizCardCreator: TextStyle,
    val quizzerQuizCardTags: TextStyle,
    val quizzerQuizCardDescription: TextStyle,
    val quizzerRoundTab: TextStyle,
    val quizzerUI: TextStyle,
    val quizzerBodyMedium: TextStyle,
    val quizzerHeadlineMedium: TextStyle,
    val quizzerTitleMedium: TextStyle,
    val quizzerListItemTitle: TextStyle,
    val quizzerListItemSub: TextStyle,
    val quizzerBodySmall: TextStyle,
)

// NOTICE: WHEN THIS IS UPDATED SHOULD UPDATE QUIZZER TYPOGRAPHYPREVIEW AS WELL
val QuizzerTypographyDefaults = QuizzerTypography(
    quizzerTopBarTitle = Typography.headlineSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Normal
    ),
    quizzerIconLabel = Typography.labelSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Medium
    ),
    quizzerQuizCardListTitle = Typography.titleMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerQuizCardTitle = Typography.titleSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Medium
    ),
    quizzerQuizCardCreator = Typography.labelSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Light
    ),
    quizzerQuizCardTags = Typography.bodySmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerQuizCardDescription = Typography.bodySmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Normal
    ),
    quizzerRoundTab = Typography.bodyMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerUI = Typography.labelMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Medium
    ),
    quizzerBodyMedium = Typography.bodyMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Normal
    ),
    quizzerHeadlineMedium = Typography.headlineMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerTitleMedium = Typography.titleMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Medium
    ),
    quizzerListItemTitle = Typography.headlineSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerListItemSub = Typography.bodySmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Light
    ),
    quizzerBodySmall = Typography.bodySmall.copy(
        fontFamily = GothicA1,
    ),
)
