package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class MultipleChoiceQuizViewModel : BaseQuizViewModel<MultipleChoiceQuiz>(
    MultipleChoiceQuiz()
) {
    override val _quizState: MutableStateFlow<MultipleChoiceQuiz> = MutableStateFlow(MultipleChoiceQuiz())

    init {
        resetQuiz()
    }

    override fun resetQuiz(){
        _quizState.value = MultipleChoiceQuiz()
    }

    override fun loadQuiz(quiz: MultipleChoiceQuiz){
        _quizState.value = quiz
    }

    override fun viewerInit(){
        _quizState.value.initViewState()
    }

    fun toggleAnsAt(index: Int){
        val state = _quizState.value
        if (index !in state.correctFlags.indices) return
        if(index >= _quizState.value.correctFlags.size){
            return
        }
        val newFlags = state.correctFlags.toMutableList().also {
            it[index] = !it[index]
        }
        _quizState.update { it.copy(correctFlags = newFlags) }
    }

    fun removeAnswerAt(index: Int) {
        val state = _quizState.value
        if (index !in state.options.indices || state.options.size <= 3) return

        val newOptions = state.options.toMutableList().apply { removeAt(index) }
        val newFlags   = state.correctFlags.toMutableList().apply { removeAt(index) }
        _quizState.update { it.copy(options = newOptions, correctFlags = newFlags) }
    }

    fun addAnswer() {
        _quizState.update { state ->
            state.copy(
                options       = state.options + "",
                correctFlags  = state.correctFlags + false
            )
        }
    }

    fun toggleShuffleAnswers(){
        _quizState.update {
            it.copy(shuffleAnswers = !it.shuffleAnswers)
        }
    }
}