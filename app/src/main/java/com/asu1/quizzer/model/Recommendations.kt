package com.asu1.quizzer.model

import kotlinx.serialization.Serializable
import com.asu1.quizcardmodel.QuizCard

@Serializable
data class RecommendationList(
    val searchResult: List<Recommendations>
)

@Serializable
data class Recommendations(
    val key: String,
    val items: List<QuizCard>
)

@Serializable
data class QuizCardList(
    val searchResult: List<QuizCard>
)

@Serializable
data class UserRankList(
    val searchResult: List<UserRank>
)