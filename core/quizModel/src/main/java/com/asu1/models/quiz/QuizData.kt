package com.asu1.models.quiz

data class QuizData(
    var title: String = "",
    var image: ByteArray = byteArrayOf(),
    var description: String = "",
    var tags: Set<String> = emptySet(),
    var shuffleQuestions: Boolean = false,
    var creator: String = "GUEST",
    var uuid : String? = null,
)
