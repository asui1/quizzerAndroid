package com.asu1.quizzer.model

abstract class Quiz {
    var layoutType: Int = 0
    var answers: List<String> = emptyList()
    var ans: List<Boolean> = emptyList()
    var image: ByteArray? = null
    var question: String = ""
}

class Quiz1: Quiz() {
    var bodyType: Int = 0
    var bodyText: String = ""
    var shuffleAnswers: Boolean = false
    var maxAnswerSelection: Int = 1
    var titleImageBytes: ByteArray? = null
    var youtubeId = ""
    var youtubeStartTime = 0
}

class Quiz2: Quiz() {
    var bodyType: Int = 0
    var bodyText: String = ""
    var shuffleAnswers: Boolean = false
    var maxAnswerSelection: Int = 1
    var titleImageBytes: ByteArray? = null
    var youtubeId = ""
    var youtubeStartTime = 0
}