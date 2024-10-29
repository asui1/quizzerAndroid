package com.asu1.quizzer.viewModels.quizModels

import androidx.lifecycle.ViewModel
import com.asu1.quizzer.model.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseQuizViewModel<T : Quiz> : ViewModel() {
    protected val _quizState = MutableStateFlow<T?>(null)
    val quizState: StateFlow<T?> = _quizState.asStateFlow()

    fun setPoint(point: Int){
        _quizState.value?.point = point
    }

    fun getPoint(): Int{
        return _quizState.value?.point ?: 0
    }

    abstract fun loadQuiz(quiz: T)
    abstract fun resetQuiz()
    abstract fun updateAnswerAt(index: Int, answer: String)
    abstract fun toggleAnsAt(index: Int)
    abstract fun removeAnswerAt(index: Int)
    abstract fun addAnswer()
    abstract fun updateQuestion(question: String)
    abstract fun viewerInit()
}