package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.ShortAnswerQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ShortAnswerQuizViewModel : BaseQuizViewModel<ShortAnswerQuiz>(
    ShortAnswerQuiz()
) {
    override val mutableQuizState: MutableStateFlow<ShortAnswerQuiz> = MutableStateFlow(ShortAnswerQuiz())

    init {
        resetQuiz()
    }

    override fun resetQuiz(){
        mutableQuizState.value = ShortAnswerQuiz()
    }

    override fun loadQuiz(quiz: ShortAnswerQuiz){
        mutableQuizState.value = quiz
    }

    override fun viewerInit(){
        mutableQuizState.value.initViewState()
    }

    fun updateAnswer(newAnswer:String){
        mutableQuizState.update {
            it.copy(
                answer = newAnswer
            )
        }
    }

}