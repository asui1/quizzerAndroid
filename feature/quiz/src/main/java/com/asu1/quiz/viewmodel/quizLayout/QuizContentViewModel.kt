package com.asu1.quiz.viewmodel.quizLayout

import SnackBarManager
import ToastType
import androidx.lifecycle.ViewModel
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.Quiz3
import com.asu1.models.quiz.Quiz4
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizJson
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class QuizContentViewModel: ViewModel() {
    private val _quizContentViewModelState = MutableStateFlow(ViewModelState.IDLE)
    val quizContentViewModelState = _quizContentViewModelState.asStateFlow()

    private val _quizContentState = MutableStateFlow<QuizContentState>(QuizContentState())
    val quizContentState = _quizContentState.asStateFlow()

    fun reset(){
        _quizContentState.update {
            QuizContentState()
        }
    }

    fun loadQuizContents(quizzes: List<QuizJson>){
        _quizContentState.update { currentState ->
            currentState.copy(
                quizzes = quizzes.map{ it.toQuiz()},
                quizInitIndex = 0,
            )
        }
    }

    fun updateQuizInitIndex(newIndex: Int) {
        _quizContentState.update { currentState ->
            newIndex.takeIf { it in currentState.quizzes.indices }
                ?.let { currentState.copy(quizInitIndex = it) }
                ?: currentState
        }
    }

    fun validateQuiz(): Boolean {
        val quizzes = _quizContentState.value.quizzes
        if(quizzes.isEmpty()) {
            SnackBarManager.showSnackBar(R.string.quiz_cannot_be_empty, ToastType.ERROR)
            return false
        }
        val invalidQuiz = quizzes.firstOrNull { it.validateQuiz() != QuizError.NO_ERROR }
        return if (invalidQuiz != null) {
            val errorStatus = invalidQuiz.validateQuiz()
            SnackBarManager.showSnackBar(errorStatus.messageId, ToastType.ERROR)
            updateQuizInitIndex(quizzes.indexOf(invalidQuiz))
            false
        } else {
            true
        }
    }

    fun addQuiz(quiz: Quiz<*>, index: Int? = null) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()

            if (index == null || index !in updatedQuizzes.indices) {
                updatedQuizzes.add(quiz)
            } else {
                updatedQuizzes.add(index, quiz)
            }

            currentState.copy(quizzes = updatedQuizzes)
        }
    }

    fun updateQuizAnswer(index: Int, update: QuizUserUpdates){
        when(update){
            is QuizUserUpdates.Quiz1Update -> updateQuiz1(index, update.index)
            is QuizUserUpdates.Quiz2Update -> updateQuiz2(index, update.date)
            is QuizUserUpdates.Quiz3Update -> updateQuiz3(index, update.first, update.second)
            is QuizUserUpdates.Quiz4Update -> updateQuiz4(index, update.items)
        }
    }

    fun updateQuiz1(quizIndex: Int, answerIndex: Int) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()
            (updatedQuizzes[quizIndex] as? Quiz1)?.toggleUserAnswerAt(answerIndex)
            currentState.copy(quizzes = updatedQuizzes)
        }
    }

    fun updateQuiz2(quizIndex: Int, date: LocalDate) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()
            (updatedQuizzes[quizIndex] as? Quiz2)?.toggleUserAnswer(date)
            currentState.copy(quizzes = updatedQuizzes)
        }
    }

    fun updateQuiz3(quizIndex: Int, from: Int, to: Int) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()
            (updatedQuizzes[quizIndex] as? Quiz3)?.swap(from, to)
            currentState.copy(quizzes = updatedQuizzes)
        }
    }

    fun updateQuiz4(quizIndex: Int, items: List<Int?>) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()
            try {
                (updatedQuizzes[quizIndex] as? Quiz4)?.let {
                    it.userConnectionIndex = items
                    currentState.copy(quizzes = updatedQuizzes)
                } ?: currentState
            } catch (e: Exception) {
                Logger.debug("Failed to update quiz 4: ${e.message}")
                currentState
            }
        }
    }

    fun updateQuiz(quiz: Quiz<*>, index: Int) {
        _quizContentState.update { currentState ->
            if (index in currentState.quizzes.indices) {
                val updatedQuizzes = currentState.quizzes.toMutableList()
                updatedQuizzes[index] = quiz
                currentState.copy(quizzes = updatedQuizzes)
            } else {
                currentState
            }
        }
    }

    fun removeQuizAt(index: Int) {
        _quizContentState.update { currentState ->
            if (index in currentState.quizzes.indices) {
                val updatedQuizzes = currentState.quizzes.toMutableList().apply {
                    this.removeAt(
                        index
                    )
                }
                currentState.copy(quizzes = updatedQuizzes)
            } else {
                currentState
            }
        }
    }

    fun gradeQuiz(): Pair<Int, List<Boolean>> {
        val quizzes = _quizContentState.value.quizzes
        if (quizzes.isEmpty()) return Pair(0, emptyList())
        var currentScore = 0

        val corrections = mutableListOf<Boolean>()
        quizzes.forEach { quiz ->
            val correct = quiz.gradeQuiz()
            if (correct) currentScore += 1
            corrections.add(correct)
        }
        return Pair(currentScore, corrections)
    }


}

data class QuizContentState(
    val quizzes: List<Quiz<*>> = emptyList(),
    val quizInitIndex: Int = 0,
)
