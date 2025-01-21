package com.asu1.quizcardmodel

import kotlinx.serialization.Serializable

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

