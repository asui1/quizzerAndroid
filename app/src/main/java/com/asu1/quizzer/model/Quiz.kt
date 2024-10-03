package com.asu1.quizzer.model

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
class Quiz2: Quiz() {
    var layoutType: QuizType = QuizType.QUIZ2
    var maxAnswerSelection: Int = 1
    var centerDate: List<Int> = listOf(2024, 6, 22)
    var yearRange: Int = 20
    var answerDate: List<Int> = listOf(2024, 6, 22)
}

class Quiz3: Quiz(){
    var layoutType: QuizType = QuizType.QUIZ3
    var shuffledAnswers: List<String> = emptyList()
}

class Quiz4: Quiz(){
    var layoutType: QuizType = QuizType.QUIZ4
    var connectionAnswers: List<String> = emptyList()
    var connectionAnswerIndex: List<Int> = emptyList()
}