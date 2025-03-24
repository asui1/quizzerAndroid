package com.asu1.quiz.viewmodel.quizLayout

import androidx.lifecycle.ViewModel
import com.asu1.models.quiz.QuizResult
import com.asu1.resources.ViewModelState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizResultViewModel: ViewModel() {
    private val _quizResult = MutableStateFlow<QuizResult?>(null)
    val quizResult: StateFlow<QuizResult?> = _quizResult.asStateFlow()

    private val _quizResultViewModelState = MutableStateFlow(ViewModelState.IDLE)
    val quizResultViewModelState = _quizResultViewModelState.asStateFlow()


    fun resetQuizResult(){
        _quizResult.value = null
    }

    fun updateQuizResult(quizResult: QuizResult){
        _quizResult.value = quizResult
    }
}