package com.asu1.quizzer.model

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme

data class QuizLayout(
    var quizTitle: String = "",
    var quizImage: ByteArray? = null,
    var quizDescription: String = "",
    var quizTags: List<String> = emptyList(),
    var flipStyle: Int = 0,
    var colorScheme: ColorScheme,
    var backgroundImage: ImageColor? = null,
    var shuffleQuestions: Boolean = false,
    var questionTextStyle: List<Int> = listOf(0, 0, 1, 0),
    var bodyTextStyle: List<Int> = listOf(0, 0, 2, 1),
    var answerTextStyle: List<Int> = listOf(0, 0, 0, 2),
    var creator: String = "GUEST",
    var uuid: String? = null,
    var quizzes: List<Quiz> = emptyList(),
)
