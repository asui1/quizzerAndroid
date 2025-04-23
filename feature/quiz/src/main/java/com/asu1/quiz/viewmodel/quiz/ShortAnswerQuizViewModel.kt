package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.ShortAnswerQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ShortAnswerQuizViewModel : BaseQuizViewModel<ShortAnswerQuiz>(
    ShortAnswerQuiz()
) {
    override val _quizState: MutableStateFlow<ShortAnswerQuiz> = MutableStateFlow(ShortAnswerQuiz())

    init {
        resetQuiz()
    }

    override fun resetQuiz(){
        _quizState.value = ShortAnswerQuiz()
    }

    override fun loadQuiz(quiz: ShortAnswerQuiz){
        _quizState.value = quiz
    }

    override fun viewerInit(){
        _quizState.value.initViewState()
    }

    fun updateAnswer(newAnswer:String){
        _quizState.update {
            it.copy(
                answer = newAnswer
            )
        }
    }

}