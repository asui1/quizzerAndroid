package com.asu1.models.quiz

import androidx.compose.runtime.Immutable
import com.asu1.models.scorecard.ScoreCard
import com.asu1.utils.ImmutableListSerializer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
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
    @Serializable(with = ImmutableListSerializer::class) val correction: PersistentList<Boolean>,
    @Serializable(with = ImmutableListSerializer::class) val distribution: PersistentList<Int>,
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
    correction = listOf(true, true, false, false, true, false, true, false, true, false).toPersistentList(),
    distribution = listOf(1, 3, 5, 7, 10, 13, 17, 21, 26, 31, 38, 31, 26, 21, 17, 13, 10, 7, 5, 3, 1).toPersistentList(),
    percent = 10.5f,
    nickname = "GUEST",
)