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
    val correctProb: Int,
    val correction: List<Boolean>,
)

@Immutable
@Serializable
data class QuizResult(
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
    correction = listOf(true, true, false, false, true, false, true, false, true, false).toPersistentList(),
    distribution = listOf(1, 3, 5, 7, 10, 13, 10, 7, 5, 3, 1).toPersistentList(),
    percent = 50f,
    nickname = "GUEST",
)