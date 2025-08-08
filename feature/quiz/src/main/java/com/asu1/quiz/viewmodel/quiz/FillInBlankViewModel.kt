package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.FillInBlankQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FillInBlankViewModel: BaseQuizViewModel<FillInBlankQuiz>(
    FillInBlankQuiz()
) {
    override val mutableQuizState: MutableStateFlow<FillInBlankQuiz> = MutableStateFlow(FillInBlankQuiz())

    init {
        resetQuiz()
    }
    override fun loadQuiz(quiz: FillInBlankQuiz) {
        this.mutableQuizState.value = quiz
    }

    override fun resetQuiz() {
        this.mutableQuizState.value = FillInBlankQuiz()
    }

    override fun viewerInit() {
        mutableQuizState.value.initViewState()
    }

    fun deleteCorrectAnswer(index: Int) {
        val updated = mutableQuizState.value.copy()
        updated.removeCorrectAnswer(index)
        mutableQuizState.value = updated
    }

    fun updateCorrectAnswer(index: Int, newAnswer: String){
        val correctAnswer = mutableQuizState.value.correctAnswers.toMutableList().apply {
            this[index] = newAnswer
        }
        mutableQuizState.update { state ->
            state.copy(
                correctAnswers = correctAnswer
            )
        }
    }

    fun updateRawText(newRawText: String){
        val updated = mutableQuizState.value.copy()
        updated.updateRawText(newRawText)
        mutableQuizState.value = updated
    }

    fun addAnswer(){
        val updated = mutableQuizState.value.copy()
        updated.addCorrectAnswer()
        mutableQuizState.value = updated
    }
}
