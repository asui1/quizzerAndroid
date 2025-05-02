package com.asu1.quizzer.quizCreateUtils

import com.asu1.models.quiz.QuizData
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.scorecard.ScoreCard

data class QuizBundle(
    val data: QuizData,
    val theme: QuizTheme,
    val quizzes: List<Quiz>,
    val scoreCard: ScoreCard,
    val titleImage: Int,
    val overlayImage: Int = 0,
)