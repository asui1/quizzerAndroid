package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class MultipleChoiceQuizViewModel : BaseQuizViewModel<MultipleChoiceQuiz>(
    MultipleChoiceQuiz()
) {
    override val mutableQuizState: MutableStateFlow<MultipleChoiceQuiz> = MutableStateFlow(MultipleChoiceQuiz())

    init {
        resetQuiz()
    }

    override fun resetQuiz(){
        mutableQuizState.value = MultipleChoiceQuiz()
    }

    override fun loadQuiz(quiz: MultipleChoiceQuiz){
        mutableQuizState.value = quiz
    }

    override fun viewerInit(){
        mutableQuizState.value.initViewState()
    }

    fun toggleAnsAt(index: Int){
        val state = mutableQuizState.value
        if (index !in state.correctFlags.indices) return
        if(index >= mutableQuizState.value.correctFlags.size){
            return
        }
        val newFlags = state.correctFlags.toMutableList().also {
            it[index] = !it[index]
        }
        mutableQuizState.update { it.copy(correctFlags = newFlags) }
    }

    fun removeAnswerAt(index: Int) {
        val state = mutableQuizState.value
        if (index !in state.options.indices || state.options.size <= 3) return

        val newOptions = state.options.toMutableList().apply { removeAt(index) }
        val newFlags   = state.correctFlags.toMutableList().apply { removeAt(index) }
        mutableQuizState.update { it.copy(options = newOptions, correctFlags = newFlags) }
    }

    fun addAnswer() {
        mutableQuizState.update { state ->
            state.copy(
                options       = state.options + "",
                correctFlags  = state.correctFlags + false
            )
        }
    }

    fun updateAnswerAt(index: Int, newAnswer: String){
        val state = mutableQuizState.value
        if (index !in state.options.indices) return

        val newOptions = state.options.toMutableList().apply {
            this[index] = newAnswer
        }

        mutableQuizState.update { state ->

            state.copy(
                options = newOptions
            )
        }
    }

    fun toggleShuffleAnswers(){
        mutableQuizState.update {
            it.copy(shuffleAnswers = !it.shuffleAnswers)
        }
    }
}