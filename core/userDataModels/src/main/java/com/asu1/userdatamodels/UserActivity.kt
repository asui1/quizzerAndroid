package com.asu1.userdatamodels

import kotlinx.serialization.Serializable

@Serializable
data class UserActivity(
    val quizId: String,
    val quizName: String,
    val quizImage: String,
    val score: Int,
    val solvedDate: String
)
