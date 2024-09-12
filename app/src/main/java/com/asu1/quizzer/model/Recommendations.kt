package com.asu1.quizzer.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class Recommendations(
    @JsonAdapter(QuizCardListDeserializer::class)
    @SerializedName("most_viewed")
    val mostViewed: List<QuizCard>,
    @JsonAdapter(QuizCardListDeserializer::class)
    @SerializedName("similar_items")
    val similarItems: List<QuizCard>,
    @JsonAdapter(QuizCardListDeserializer::class)
    @SerializedName("most_recent_items")
    val recentItems: List<QuizCard>
)

data class QuizCardList(
    @JsonAdapter(QuizCardListDeserializer::class)
    @SerializedName("searchResult")
    val quizCards: List<QuizCard>
)