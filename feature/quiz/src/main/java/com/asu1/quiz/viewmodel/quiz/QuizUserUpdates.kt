package com.asu1.quiz.viewmodel.quiz

import java.time.LocalDate

sealed class QuizUserUpdates {
    data class MultipleChoiceQuizUpdate(val index: Int) : QuizUserUpdates()
    data class DateSelectionQuizUpdate(val date: LocalDate) : QuizUserUpdates()
    data class ReorderQuizUpdate(val first: Int, val second: Int) : QuizUserUpdates()
    data class ConnectItemQuizUpdate(val items: List<Int?>) : QuizUserUpdates()
}