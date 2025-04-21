package com.asu1.quiz.viewmodel.quiz

import androidx.lifecycle.ViewModel
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.serializers.BodyType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseQuizViewModel<Q: Quiz>(initialQuiz: Q) : ViewModel() {
    protected open val _quizState: MutableStateFlow<Q> = MutableStateFlow(initialQuiz)
    val quizState: StateFlow<Q> get() = _quizState.asStateFlow()

    fun updateQuestion(newQuestion: String) {
        _quizState.update { quiz ->
            @Suppress("UNCHECKED_CAST")
            (quiz.cloneQuiz(question = newQuestion) as Q)
        }
    }

    abstract fun loadQuiz(quiz: Q)
    abstract fun resetQuiz()
    abstract fun viewerInit()
    fun updateBodyState(bodyType: BodyType){
        _quizState.update { quiz ->
            @Suppress("UNCHECKED_CAST")
            (quiz.cloneQuiz(bodyType = bodyType) as Q)
        }
    }
}