package com.asu1.quizzer.viewModels.quizModels

import com.asu1.models.quiz.Quiz2
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.YearMonth

class Quiz2ViewModel: BaseQuizViewModel<Quiz2>(Quiz2()) {
    override val _quizState: MutableStateFlow<Quiz2> = MutableStateFlow(Quiz2())

    init {
        resetQuiz()
    }

    override fun viewerInit() {
        this._quizState.value.initViewState()
    }

    override fun loadQuiz(quiz: Quiz2) {
        this._quizState.value = quiz
    }

    override fun resetQuiz() {
        this._quizState.value = Quiz2()
    }

    override fun toggleAnsAt(index: Int) {
        //NOT USED IN QUIZ2
    }

    override fun removeAnswerAt(index: Int) {
        //NOT USED IN QUIZ2
    }

    override fun addAnswer() {
        //NOT USED IN QUIZ2
    }

    fun updateCenterDate(date: YearMonth){
        val tenYearsBefore = date.minusYears(10).atEndOfMonth()
        val tenYearsAfter = date.plusYears(10).atEndOfMonth()
        val filteredAnswerDates = this._quizState.value.answerDate.filter {
            it.isAfter(tenYearsBefore) && it.isBefore(tenYearsAfter)
        }.toMutableSet()
        this._quizState.value = this._quizState.value.copy(centerDate = date, answerDate = filteredAnswerDates, userAnswerDate = mutableSetOf())
    }

    fun updateDate(date: LocalDate){
        val answerDate = this._quizState.value.answerDate.toMutableSet()
        if(answerDate.contains(date)){
            answerDate.remove(date)
        }else{
            answerDate.add(date)
        }
        this._quizState.value = this._quizState.value.copy(answerDate = answerDate)
    }
}