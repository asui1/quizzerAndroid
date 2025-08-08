package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.utils.toggle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth

class DateSelectionQuizViewModel: BaseQuizViewModel<DateSelectionQuiz>(
    DateSelectionQuiz()
) {
    override val mutableQuizState: MutableStateFlow<DateSelectionQuiz> = MutableStateFlow(DateSelectionQuiz())

    init {
        resetQuiz()
    }

    override fun viewerInit() {
        this.mutableQuizState.value.initViewState()
    }

    override fun loadQuiz(quiz: DateSelectionQuiz) {
        this.mutableQuizState.value = quiz
    }

    override fun resetQuiz() {
        this.mutableQuizState.value = DateSelectionQuiz()
    }

    fun updateCenterDate(newCenter: YearMonth) {
        mutableQuizState.update { quiz ->
            // compute the valid window
            val lower = newCenter.minusYears(quiz.yearRange.toLong()).atDay(1)
            val upper = newCenter.plusYears(quiz.yearRange.toLong()).atEndOfMonth()

            // filter out any answerDates outside that window
            val filtered = quiz.answerDate
                .filter { it in lower..upper }

            // copy the data portion, then clear any old selections
            quiz.copy(
                centerDate = newCenter.atDay(1),
                answerDate = filtered.toSet()
            ).also { it.userDates.clear() }
        }
    }

    fun updateDate(date: LocalDate) {
        mutableQuizState.update { quiz ->
            val answerDate = quiz.answerDate.toMutableSet()
            answerDate.toggle(date)
            quiz.copy(
                answerDate = answerDate
            )
        }
    }

}