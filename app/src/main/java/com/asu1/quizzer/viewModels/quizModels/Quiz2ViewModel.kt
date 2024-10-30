package com.asu1.quizzer.viewModels.quizModels

import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth

class Quiz2ViewModel: BaseQuizViewModel<Quiz2>() {
    private val _quiz2State = MutableStateFlow(Quiz2())
    val quiz2State: StateFlow<Quiz2> get() = _quiz2State.asStateFlow()

    init {
        resetQuiz()
    }

    override fun viewerInit() {
        _quiz2State.value.initViewState()
    }

    override fun loadQuiz(quiz: Quiz2) {
        quiz.initViewState()
        _quiz2State.value = quiz
    }

    override fun resetQuiz() {
        _quiz2State.value = Quiz2()
    }

    override fun updateAnswerAt(index: Int, answer: String) {
        //NOT USED IN QUIZ2
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

    override fun updateQuestion(question: String) {
        _quiz2State.value = _quiz2State.value.copy(question = question)
    }

    fun updateCenterDate(date: YearMonth){
        val tenYearsBefore = date.minusYears(10).atEndOfMonth()
        val tenYearsAfter = date.plusYears(10).atEndOfMonth()
        val filteredAnswerDates = _quiz2State.value.answerDate.filter {
            it.isAfter(tenYearsBefore) && it.isBefore(tenYearsAfter)
        }.toMutableSet()
        _quiz2State.value = _quiz2State.value.copy(centerDate = date, answerDate = filteredAnswerDates, userAnswerDate = mutableSetOf())
    }

    fun updateDate(date: LocalDate){
        val answerDate = _quiz2State.value.answerDate.toMutableSet()
        if(answerDate.contains(date)){
            answerDate.remove(date)
        }else{
            answerDate.add(date)
        }
        _quiz2State.value = _quiz2State.value.copy(answerDate = answerDate)
    }

    fun updateUserAnswerDate(date: LocalDate){
        val userAnswerDate = _quiz2State.value.userAnswerDate.toMutableSet()
        if(userAnswerDate.contains(date)){
            userAnswerDate.remove(date)
        }else{
            userAnswerDate.add(date)
        }
        _quiz2State.value = _quiz2State.value.copy(userAnswerDate = userAnswerDate)
    }

    override fun updateBodyState(bodyType: BodyType){
        //NOT USED IN QUIZ2
    }

    override fun updateBodyText(bodyText: String){
        //NOT USED IN QUIZ2
    }

    override fun updateBodyImage(image: ByteArray){
        //NOT USED IN QUIZ2
    }

    override fun updateBodyYoutube(youtubeId: String, startTime: Int){
        //NOT USED IN QUIZ2
    }
    override fun setPoint(point: Int){
        _quiz2State.update{
            it.copy(point = point)
        }
    }

}