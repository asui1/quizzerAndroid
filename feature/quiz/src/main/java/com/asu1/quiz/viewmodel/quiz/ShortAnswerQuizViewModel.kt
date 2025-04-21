package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.ShortAnswerQuiz
import kotlinx.coroutines.flow.MutableStateFlow

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

}