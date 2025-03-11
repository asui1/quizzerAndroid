package com.asu1.resources

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

data class QuizzerTypography(
    val quizzerHeadlineSmallNormal: TextStyle,
    val quizzerLabelSmallMedium: TextStyle,
    val quizzerTitleMediumBold: TextStyle,
    val quizzerTitleSmallMedium: TextStyle,
    val quizzerLabelSmallLight: TextStyle,
    val quizzerBodySmallBold: TextStyle,
    val quizzerBodySmallNormal: TextStyle,
    val quizzerBodyMediumBold: TextStyle,
    val quizzerLabelMediumMedium: TextStyle,
    val quizzerBodyMediumNormal: TextStyle,
    val quizzerHeadlineMediumBold: TextStyle,
    val quizzerTitleMediumMedium: TextStyle,
    val quizzerHeadlineSmallBold: TextStyle,
    val quizzerBodySmallLight: TextStyle,
    val quizzerBodySmall: TextStyle,
    val quizzerHeadlineMedium: TextStyle,
    val quizzerBodyLarge: TextStyle,
)

// NOTICE: WHEN THIS IS UPDATED SHOULD UPDATE QUIZZER TYPOGRAPHYPREVIEW AS WELL
val QuizzerTypographyDefaults = QuizzerTypography(
    quizzerHeadlineSmallNormal = Typography.headlineSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Normal
    ),
    quizzerLabelSmallMedium = Typography.labelSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Medium
    ),
    quizzerTitleMediumBold = Typography.titleMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerTitleSmallMedium = Typography.titleSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Medium
    ),
    quizzerLabelSmallLight = Typography.labelSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Light
    ),
    quizzerBodySmallBold = Typography.bodySmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerBodySmallNormal = Typography.bodySmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Normal
    ),
    quizzerBodyMediumBold = Typography.bodyMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerLabelMediumMedium = Typography.labelMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Medium
    ),
    quizzerBodyMediumNormal = Typography.bodyMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Normal
    ),
    quizzerHeadlineMediumBold = Typography.headlineMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerTitleMediumMedium = Typography.titleMedium.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Medium
    ),
    quizzerHeadlineSmallBold = Typography.headlineSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizzerBodySmallLight = Typography.bodySmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Light
    ),
    quizzerBodySmall = Typography.bodySmall.copy(
        fontFamily = GothicA1,
    ),
    quizzerHeadlineMedium = Typography.headlineMedium.copy(
        fontFamily = GothicA1,
    ),
    quizzerBodyLarge = Typography.bodyLarge.copy(
        fontFamily = GothicA1,
    ),
)
