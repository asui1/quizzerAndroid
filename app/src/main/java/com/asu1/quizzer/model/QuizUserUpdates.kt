package com.asu1.quizzer.model

import java.time.LocalDate

sealed class QuizUserUpdates {
    data class Quiz1Update(val index: Int) : QuizUserUpdates()
    data class Quiz2Update(val date: LocalDate) : QuizUserUpdates()
    data class Quiz3Update(val first: Int, val second: Int) : QuizUserUpdates()
    data class Quiz4Update(val items: List<Int?>) : QuizUserUpdates()
}