package com.asu1.quizzer.data

import com.asu1.quizzer.model.ScoreCard
import kotlinx.serialization.Serializable

@Serializable
data class SendQuizResult(
    val email: String,
    val quizUuid: String,
    val score: Float,
    val correction: List<Boolean>,
)

@Serializable
data class QuizResult(
    val score: Float,
    val correction: List<Boolean>,
    val distribution: List<Int>,
    val percent: Float,
    val nickname: String,
)

@Serializable
data class GetQuizResult(
    val quizResult: QuizResult,
    val scoreCard: ScoreCard,
)

val sampleResult = QuizResult(
    score = 87.5f,
    correction = listOf(true, true, false, false, true, false, true, false, true, false),
    distribution = listOf(1, 3, 5, 7, 10, 13, 17, 21, 26, 31, 38, 31, 26, 21, 17, 13, 10, 7, 5, 3, 1), // Make list of 0~4, 5~9, 10 ~ 14, 15 ~ 19, ... 95 ~ 99, 100
    percent = 10.5f,
    nickname = "GUEST",
)