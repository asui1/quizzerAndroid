package com.asu1.userdatamodels

import kotlinx.serialization.Serializable

@Serializable
data class UserRank(
    val nickname: String,
    val profileImageUri: String,
    val orderScore: Int,
    val totalScore: Int,
    val quizzesSolved: Int,
    val problemsSolved: Int,
)

val sampleUserRank = UserRank(
    nickname = "John Doe",
    profileImageUri =
        "https://lh3.googleusercontent.com/a/ACg8ocJfoHUjigfS1fBoyEPXLv1pusBvf7WTJAfUoQV8YhPjr4Whq98=s96-c",
    orderScore = 10,
    totalScore = 50,
    quizzesSolved = 5,
    problemsSolved = 50,
)

val sampleUserRankList = listOf(
    sampleUserRank,
    sampleUserRank.copy(nickname = "2"),
    sampleUserRank.copy(nickname = "3"),
    sampleUserRank.copy(nickname = "4"),
    sampleUserRank.copy(nickname = "5"),
    sampleUserRank.copy(nickname = "6"),
)

@Serializable
data class UserRankList(
    val searchResult: List<UserRank>
)
