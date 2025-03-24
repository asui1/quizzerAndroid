package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quiz.Quiz3
import kotlinx.coroutines.flow.MutableStateFlow

class Quiz3ViewModel: BaseQuizViewModel<Quiz3>(Quiz3()) {
    override val _quizState: MutableStateFlow<Quiz3> = MutableStateFlow(Quiz3())

    init {
        resetQuiz()
    }

    override fun viewerInit() {
        this._quizState.value.initViewState()
    }

    override fun loadQuiz(quiz: Quiz3) {
        this._quizState.value = quiz
    }

    override fun resetQuiz() {
        this._quizState.value = Quiz3()
    }

    override fun toggleAnsAt(index: Int) {
        //NOT USED IN QUIZ3
    }

    override fun removeAnswerAt(index: Int) {
        if(this._quizState.value.answers.size <= 3){
            return
        }
        this._quizState.value = this._quizState.value.cloneQuiz(answers = this._quizState.value.answers.toMutableList().apply {
            if(index < this.size){
                this.removeAt(index)
            }
        })
    }

    override fun addAnswer() {
        this._quizState.value = this._quizState.value.cloneQuiz(answers = this._quizState.value.answers.toMutableList().apply {
            this.add("")
        })
    }
}
