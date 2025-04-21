package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quiz.Quiz5
import kotlinx.coroutines.flow.MutableStateFlow

class Quiz5ViewModel: BaseQuizViewModel<Quiz5>(Quiz5()) {
    override val _quizState: MutableStateFlow<Quiz5> = MutableStateFlow(Quiz5())
    init {
        resetQuiz()
    }
    override fun loadQuiz(quiz: Quiz5) {
        this._quizState.value = quiz
    }

    override fun resetQuiz() {
        this._quizState.value = Quiz5()
    }

    override fun toggleAnsAt(index: Int) {
        //NOT USED IN QUIZ5
    }

    override fun removeAnswerAt(index: Int) {
        //NOT USED IN QUIZ5
    }

    override fun addAnswer() {
        //NOT USED IN QUIZ5
    }

    override fun viewerInit() {
        _quizState.value.initViewState()
    }

}
