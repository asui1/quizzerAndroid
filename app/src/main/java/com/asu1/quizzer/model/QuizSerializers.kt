package com.asu1.quizzer.model

import kotlinx.serialization.Serializable

@Serializable
data class Quiz1Json(
    val layoutType: Int,
    val body: Quiz1Body
)

@Serializable
data class Quiz1Body(
    val bodyType: Int,
    val image: String?,
    val bodyText: String,
    val shuffleAnswers: Boolean,
    val maxAnswerSelection: Int,
    val answers: List<String>,
    val ans: List<Boolean>,
    val question: String,
    val youtubeId: String?,
    val youtubeStartTime: Int?
)

@Serializable
data class Quiz2Json(
    val layoutType: Int,
    val body: Quiz2Body
)

@Serializable
data class Quiz2Body(
    val centerDate: List<Int>,
    val yearRange: Int,
    val answerDate: List<List<Int>>,
    val maxAnswerSelection: Int,
    val answers: List<String>,
    val ans: List<Boolean>,
    val question: String
)

@Serializable
data class Quiz3Json(
    val layoutType: Int,
    val body: Quiz3Body
)

@Serializable
data class Quiz3Body(
    val layoutType: Int,
    val maxAnswerSelection: Int,
    val answers: List<String>,
    val ans: List<Boolean>,
    val question: String
)

@Serializable
data class Quiz4Json(
    val layoutType: Int,
    val body: Quiz4Body
)

@Serializable
data class Quiz4Body(
    val connectionAnswers: List<String>,
    val connectionAnswerIndex: List<Int?>,
    val layoutType: Int,
    val maxAnswerSelection: Int,
    val answers: List<String>,
    val ans: List<Boolean>,
    val question: String
)