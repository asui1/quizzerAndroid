package com.asu1.quizzer.model

import androidx.compose.ui.geometry.Offset
import java.time.LocalDate
import java.time.YearMonth

enum class QuizType(val value: Int) {
    QUIZ1(0),
    QUIZ2(1),
    QUIZ3(2),
    QUIZ4(3),
}

abstract class Quiz(
    open var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    open var question: String = "",
    open val layoutType: QuizType = QuizType.QUIZ1,
) {
    operator fun get(index: Int): String {
        return answers[index]
    }

    operator fun set(index: Int, value: String) {
        answers[index] = value
    }
}

enum class BodyType(val value: Int) {
    NONE(0),
    TEXT(1),
    IMAGE(2),
    YOUTUBE(3),
}

//BASIC MULTIPLE CHOICE QUIZ
data class Quiz1(
    var bodyType: BodyType = BodyType.NONE,
    var image: ByteArray? = null,
    var ans: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
    var shuffleAnswers: Boolean = false,
    var maxAnswerSelection: Int = 1,
    var bodyText: String = "",
    var youtubeId: String = "",
    var youtubeStartTime: Int = 0,
    override var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ1,
) : Quiz(answers, question){
}


//SELECTING DATE FROM CALENDAR
data class Quiz2(
    var maxAnswerSelection: Int = 1,
    var centerDate: YearMonth = YearMonth.now(),
    var yearRange: Int = 20,
    var answerDate: MutableSet<LocalDate> = mutableSetOf(),
    override var answers: MutableList<String> = mutableListOf(),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ2,
): Quiz(answers, question)

//ORDERING QUESTIONS
data class Quiz3(
    var shuffledAnswers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ3,
): Quiz(answers, question)

//CONNECTING QUESTIONS
data class Quiz4(
    var connectionAnswers: List<String> = mutableListOf("", "", "", ""),
    var connectionAnswerIndex: List<Int?> = mutableListOf(null, null, null, null),
    var dotPairOffsets: List<Pair<Offset?, Offset?>> = mutableListOf(Pair(null, null), Pair(null, null), Pair(null, null), Pair(null, null)),
    override var answers: MutableList<String> = mutableListOf("", "", "", ""),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ4,
): Quiz(answers, question){
    init {
        // Additional initialization logic if needed
    }
    fun updateConnectionAnswerIndex(index: Int, value: Int?){
        connectionAnswerIndex = connectionAnswerIndex.toMutableList().also {
            it[index] = value
        }
    }
}