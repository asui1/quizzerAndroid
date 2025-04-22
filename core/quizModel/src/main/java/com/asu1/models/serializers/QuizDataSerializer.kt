package com.asu1.models.serializers

import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.scorecard.ScoreCard
import kotlinx.serialization.Serializable

@Serializable
data class QuizDataSerializer(
    val shuffleQuestions: Boolean = false,
    val title: String,
    val creator: String = "Guest",
    val titleImage: String = "", //BASE 64 ENCODED STRING
    val uuid: String,
    val tags: Set<String> = emptySet(),
    val quizzes: List<Quiz>,
    val description: String,
)

@Serializable
data class QuizLayoutSerializer(
    val quizData: QuizDataSerializer,
    val quizTheme: QuizTheme,
    val scoreCard: ScoreCard
)




