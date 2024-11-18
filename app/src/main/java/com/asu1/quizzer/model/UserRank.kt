package com.asu1.quizzer.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRank(
    val nickname: String,
    val profileImageUri: String,
    val orderScore: Int,
    val totalScore: Float,
    val quizzesSolved: Int,
    val problemsSolved: Int,
)
val userRankSample = UserRank(
    nickname = "John Doe",
    profileImageUri = "https://lh3.googleusercontent.com/a/ACg8ocJfoHUjigfS1fBoyEPXLv1pusBvf7WTJAfUoQV8YhPjr4Whq98=s96-c",
    orderScore = 350,
    totalScore = 342.3f,
    quizzesSolved = 5,
    problemsSolved = 50,
)
