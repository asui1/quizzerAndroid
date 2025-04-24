package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.FillInBlankQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FillInBlankViewModel: BaseQuizViewModel<FillInBlankQuiz>(
    FillInBlankQuiz()
) {
    override val _quizState: MutableStateFlow<FillInBlankQuiz> = MutableStateFlow(FillInBlankQuiz())

    init {
        resetQuiz()
    }
    override fun loadQuiz(quiz: FillInBlankQuiz) {
        this._quizState.value = quiz
    }

    override fun resetQuiz() {
        this._quizState.value = FillInBlankQuiz()
    }

    override fun viewerInit() {
        _quizState.value.initViewState()
    }

    fun deleteCorrectAnswer(index: Int) {
        val updated = _quizState.value.copy()
        updated.removeCorrectAnswer(index)
        _quizState.value = updated
    }

    fun updateCorrectAnswer(index: Int, newAnswer: String){
        val correctAnswer = _quizState.value.correctAnswers.toMutableList().apply {
            this[index] = newAnswer
        }
        _quizState.update { state ->
            state.copy(
                correctAnswers = correctAnswer
            )
        }
    }

    fun updateRawText(newRawText: String){
        val updated = _quizState.value.copy()
        updated.updateRawText(newRawText)
        _quizState.value = updated
    }

    fun addAnswer(){
        val updated = _quizState.value.copy()
        updated.addCorrectAnswer()
        _quizState.value = updated
    }
}
