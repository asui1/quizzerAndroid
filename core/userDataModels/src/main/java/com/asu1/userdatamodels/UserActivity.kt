package com.asu1.userdatamodels

import kotlinx.serialization.Serializable

@Serializable
data class UserActivity(
    val quizId: String,
    val quizTitle: String,
    val score: Float,
    val solvedDate: String
)

val sampleUserActivity = UserActivity(
    quizId = "quizId",
    quizTitle = "quizName",
    score = 0.0f,
    solvedDate = "solvedDate"
)

val sampleUserActivityList = listOf(
    sampleUserActivity,
    sampleUserActivity,
    sampleUserActivity,
    sampleUserActivity,
    sampleUserActivity,
)
