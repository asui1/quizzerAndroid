package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.ReorderQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ReorderQuizViewModel : BaseQuizViewModel<ReorderQuiz>(
    ReorderQuiz()
) {
    override val _quizState: MutableStateFlow<ReorderQuiz> = MutableStateFlow(ReorderQuiz())

    init {
        resetQuiz()
    }

    override fun viewerInit() {
        this._quizState.value.initViewState()
    }

    override fun loadQuiz(quiz: ReorderQuiz) {
        this._quizState.value = quiz
    }

    override fun resetQuiz() {
        this._quizState.value = ReorderQuiz()
    }

    fun updateAnswerAt(index: Int, newAnswer: String) {
        val state = _quizState.value
        if (index !in state.answers.indices) return

        // build the new list once, outside of the lambda
        val newAnswers = state.answers
            .toMutableList()
            .apply { this[index] = newAnswer }

        _quizState.update { quiz ->
            // lambda only does the immutable copy
            quiz.copy(answers = newAnswers)
        }
    }

    fun removeAnswerAt(index: Int) {
        _quizState.update { quiz ->
            if (quiz.answers.size <= 3 || index !in quiz.answers.indices) return@update quiz

            val newAnswers = quiz.answers
                .toMutableList()
                .also { it.removeAt(index) }

            quiz.copy(answers = newAnswers)
        }
    }

    fun addAnswer() {
        _quizState.update { quiz ->
            val newAnswers = quiz.answers + ""

            quiz.copy(answers = newAnswers)
        }
    }
}
