package com.asu1.userdatamodels

import kotlinx.serialization.Serializable

@Serializable
data class UserActivity(
    val quizId: String,
    val quizTitle: String,
    val correctCount: Int,
    val totalCount: Int,
    val solvedDate: String
)
val sampleUserActivity = UserActivity(
    quizId = "quizId",
    quizTitle = "quizName",
    correctCount = 3,
    totalCount = 5,
    solvedDate = "solvedDate"
)

val sampleUserActivityList = listOf(
    sampleUserActivity,
    sampleUserActivity,
    sampleUserActivity,
    sampleUserActivity,
    sampleUserActivity,
)
