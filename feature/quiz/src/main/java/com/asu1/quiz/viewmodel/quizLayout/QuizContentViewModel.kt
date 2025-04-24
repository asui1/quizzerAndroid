package com.asu1.quiz.viewmodel.quizLayout

import SnackBarManager
import ToastType
import androidx.lifecycle.ViewModel
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.serializers.QuizError
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

    fun loadQuizContents(quizzes: List<Quiz>){
        quizzes.forEach { quiz ->
            quiz.initViewState()
            Logger.debug(quiz)
        }
        _quizContentState.update { currentState ->
            currentState.copy(
                quizzes = quizzes,
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

    fun addQuiz(quiz: Quiz, index: Int? = null) {
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
            is QuizUserUpdates.MultipleChoiceQuizUpdate -> updateMultipleChoiceQuiz(index, update.index)
            is QuizUserUpdates.DateSelectionQuizUpdate -> updateDateSelectionQuiz(index, update.date)
            is QuizUserUpdates.ReorderQuizUpdate -> updateReorderQuiz(index, update.from, update.to)
            is QuizUserUpdates.ConnectItemQuizUpdate -> updateConnectItemsQuiz(index, update.items)
            is QuizUserUpdates.ShortAnswerQuizUpdate -> updateShortAnswerQuiz(index, update.userAnswer)
        }
    }

    fun updateMultipleChoiceQuiz(quizIndex: Int, answerIndex: Int) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()
            (updatedQuizzes[quizIndex] as? MultipleChoiceQuiz)?.toggleUserSelectionAt(answerIndex)
            currentState.copy(quizzes = updatedQuizzes)
        }
    }

    fun updateDateSelectionQuiz(quizIndex: Int, date: LocalDate) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()
            (updatedQuizzes[quizIndex] as? DateSelectionQuiz)?.toggleUserAnswer(date)
            currentState.copy(quizzes = updatedQuizzes)
        }
    }

    fun updateReorderQuiz(quizIndex: Int, from: Int, to: Int) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()
            (updatedQuizzes[quizIndex] as? ReorderQuiz)?.swap(from, to)
            currentState.copy(quizzes = updatedQuizzes)
        }
    }

    fun updateConnectItemsQuiz(quizIndex: Int, items: List<Int?>) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()
            try {
                (updatedQuizzes[quizIndex] as? ConnectItemsQuiz)?.let {
                    it.userConnectionIndex = items as MutableList<Int?>
                    currentState.copy(quizzes = updatedQuizzes)
                } ?: currentState
            } catch (e: Exception) {
                Logger.debug("Failed to update quiz 4: ${e.message}")
                currentState
            }
        }
    }

    fun updateShortAnswerQuiz(quizIndex: Int, newAnswer: String) {
        _quizContentState.update { currentState ->
            val updatedQuizzes = currentState.quizzes.toMutableList()
            (updatedQuizzes[quizIndex] as? ShortAnswerQuiz)?.userAnswer = newAnswer
            currentState.copy(quizzes = updatedQuizzes)
        }
    }

    fun updateQuiz(quiz: Quiz, index: Int) {
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
    val quizzes: List<Quiz> = emptyList(),
    val quizInitIndex: Int = 0,
)
