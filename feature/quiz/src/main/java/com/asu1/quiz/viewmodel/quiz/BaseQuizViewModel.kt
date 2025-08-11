package com.asu1.quiz.viewmodel.quiz

import androidx.lifecycle.ViewModel
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.serializers.BodyType
import com.asu1.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseQuizViewModel<Q: Quiz>(initialQuiz: Q) : ViewModel() {
    protected open val mutableQuizState: MutableStateFlow<Q> = MutableStateFlow(initialQuiz)
    val quizState: StateFlow<Q> get() = mutableQuizState.asStateFlow()

    fun onAction(action: QuizAction) {
        when (action) {
            is QuizAction.UpdateQuestion -> updateQuestion(action.value)
            is QuizAction.UpdateBody     -> updateBodyState(action.body)
            is QuizAction.Load -> {
                try {
                    @Suppress("UNCHECKED_CAST")
                    loadQuiz(action.quiz as Q)
                } catch (e: TypeCastException){
                    Logger.debug("Quiz Load Failed with TypeCastException: $e")
                }
            }
            QuizAction.Reset -> this.resetQuiz()
            QuizAction.ViewerInit -> this.viewerInit()
        }
    }

    fun updateQuestion(newQuestion: String) {
        mutableQuizState.update { quiz ->
            @Suppress("UNCHECKED_CAST")
            (quiz.cloneQuiz(question = newQuestion) as Q)
        }
    }

    abstract fun loadQuiz(quiz: Q)
    abstract fun resetQuiz()
    abstract fun viewerInit()
    fun updateBodyState(bodyType: BodyType){
        mutableQuizState.update { quiz ->
            @Suppress("UNCHECKED_CAST")
            (quiz.cloneQuiz(bodyType = bodyType) as Q)
        }
    }
}
