package com.asu1.resources

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp

data class QuizzerTypography(
    val quizTitleLarge: TextStyle,
    val quizTitleMedium: TextStyle,
    val quizTitleSmall: TextStyle,
    val quizCreatorLarge: TextStyle,
    val quizCreatorMedium: TextStyle,
    val quizCreatorSmall: TextStyle,
    val quizTags: TextStyle,
    val quizExplanation: TextStyle,
    val quizzerUILarge: TextStyle,
    val quizzerUIMedium: TextStyle,
    val quizzerUISmall: TextStyle,
    val quizzerLabelLarge: TextStyle,
    val quizzerLabelMedium: TextStyle,
    val quizzerLabelSmall: TextStyle,
    val quizzerBodySmall: TextStyle,
)

// TODO: Manage all typography in one place
val QuizzerTypographyDefaults = QuizzerTypography(
    quizTitleLarge = Typography.titleSmall.copy(
        fontFamily = GothicA1,
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium
    ),
    quizTitleMedium = Typography.titleSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Medium,
    ),
    quizTitleSmall = Typography.titleSmall.copy(
        fontFamily = GothicA1,
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium
    ),
    quizCreatorLarge = TextStyle.Default,
    quizCreatorMedium = TextStyle.Default,
    quizCreatorSmall = Typography.labelSmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Light
    ),
    quizTags = Typography.bodySmall.copy(
        fontFamily = GothicA1,
        fontWeight = FontWeight.Bold
    ),
    quizExplanation = TextStyle.Default,
    quizzerUILarge = TextStyle.Default,
    quizzerUIMedium = TextStyle.Default,
    quizzerUISmall = TextStyle.Default,
    quizzerLabelLarge = TextStyle.Default,
    quizzerLabelMedium = TextStyle.Default,
    quizzerLabelSmall = TextStyle.Default,
    quizzerBodySmall = Typography.bodySmall.copy(
        fontFamily = GothicA1,
    ),
)
