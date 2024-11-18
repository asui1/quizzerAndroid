package com.asu1.quizzer.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRank(
    val nickname: String,
    val profileImageUri: String,
    val totalScore: Float,
    val quizzesSolved: Int,
)
