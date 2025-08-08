package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.ReorderQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ReorderQuizViewModel : BaseQuizViewModel<ReorderQuiz>(
    ReorderQuiz()
) {
    override val mutableQuizState: MutableStateFlow<ReorderQuiz> = MutableStateFlow(ReorderQuiz())

    init {
        resetQuiz()
    }

    override fun viewerInit() {
        this.mutableQuizState.value.initViewState()
    }

    override fun loadQuiz(quiz: ReorderQuiz) {
        this.mutableQuizState.value = quiz
    }

    override fun resetQuiz() {
        this.mutableQuizState.value = ReorderQuiz()
    }

    fun updateAnswerAt(index: Int, newAnswer: String) {
        val state = mutableQuizState.value
        if (index !in state.answers.indices) return

        // build the new list once, outside of the lambda
        val newAnswers = state.answers
            .toMutableList()
            .apply { this[index] = newAnswer }

        mutableQuizState.update { quiz ->
            // lambda only does the immutable copy
            quiz.copy(answers = newAnswers)
        }
    }

    fun removeAnswerAt(index: Int) {
        mutableQuizState.update { quiz ->
            if (quiz.answers.size <= 3 || index !in quiz.answers.indices) return@update quiz

            val newAnswers = quiz.answers
                .toMutableList()
                .also { it.removeAt(index) }

            quiz.copy(answers = newAnswers)
        }
    }

    fun addAnswer() {
        mutableQuizState.update { quiz ->
            val newAnswers = quiz.answers + ""

            quiz.copy(answers = newAnswers)
        }
    }
}
