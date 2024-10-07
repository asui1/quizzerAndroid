package com.asu1.quizzer.model

import androidx.collection.mutableIntListOf
import java.time.LocalDate
import java.time.YearMonth
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

enum class QuizType(val value: Int) {
    QUIZ1(0),
    QUIZ2(1),
    QUIZ3(2),
    QUIZ4(3),
}

abstract class Quiz(
    var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    var question: String = ""
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
class Quiz1(
    val layoutType: QuizType = QuizType.QUIZ1,
    var bodyType: BodyType = BodyType.NONE,
    var image: ByteArray? = null,
    var ans: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
    var shuffleAnswers: Boolean = false,
    var maxAnswerSelection: Int = 1,
    var bodyText: String = "",
    var titleImageBytes: ByteArray? = null,
    var youtubeId: String = "",
    var youtubeStartTime: Int = 0,
    answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    question: String = ""
) : Quiz(answers, question) {
    init {
        // Additional initialization logic if needed
    }
}

//SELECTING DATE FROM CALENDAR
class Quiz2(
    var layoutType: QuizType = QuizType.QUIZ2,
    var maxAnswerSelection: Int = 1,
    var centerDate: YearMonth = YearMonth.now(),
    var yearRange: Int = 20,
    var answerDate: MutableSet<LocalDate> = mutableSetOf(LocalDate.of(2024, 6, 22)),
    answers: MutableList<String> = mutableListOf(),
    question: String = ""
): Quiz(answers, question){
    init {
        // Additional initialization logic if needed
    }
    fun update(localDate: LocalDate = LocalDate.of(2024, 6, 22)){
        if(answerDate.contains(localDate)) {
            answerDate.remove(localDate)
        }else{
            answerDate.add(localDate)
        }
    }
}

//ORDERING QUESTIONS
class Quiz3(
    var layoutType: QuizType = QuizType.QUIZ3,
    var shuffledAnswers: MutableList<String> = mutableListOf("", "", "", "", ""),
    answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    question: String = ""
): Quiz(){
    init {
        // Additional initialization logic if needed
    }

}

//CONNECTING QUESTIONS
class Quiz4(
    var layoutType: QuizType = QuizType.QUIZ4,
    var connectionAnswers: List<String> = mutableListOf("", "", "", ""),
    var connectionAnswerIndex: List<Int?> = mutableListOf(null, null, null, null),
    answers: MutableList<String> = mutableListOf("", "", "", ""),
    question: String = "",
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