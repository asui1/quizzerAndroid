package com.asu1.models.quiz

import androidx.compose.runtime.Immutable
import com.asu1.models.scorecard.ScoreCard
import com.asu1.utils.ImmutableListSerializer
import com.google.common.collect.ImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class SendQuizResult(
    val email: String,
    val quizUuid: String,
    val score: Float,
    val correction: List<Boolean>,
)

@Immutable
@Serializable
data class QuizResult(
    val score: Float,
    @Serializable(with = ImmutableListSerializer::class) val correction: ImmutableList<Boolean>,
    @Serializable(with = ImmutableListSerializer::class) val distribution: ImmutableList<Int>,
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
    correction = ImmutableList.of(true, true, false, false, true, false, true, false, true, false),
    distribution = ImmutableList.of(1, 3, 5, 7, 10, 13, 17, 21, 26, 31, 38, 31, 26, 21, 17, 13, 10, 7, 5, 3, 1), // Make list of 0~4, 5~9, 10 ~ 14, 15 ~ 19, ... 95 ~ 99, 100
    percent = 10.5f,
    nickname = "GUEST",
)