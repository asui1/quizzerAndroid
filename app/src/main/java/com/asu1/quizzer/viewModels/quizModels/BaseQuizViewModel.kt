package com.asu1.quizzer.viewModels.quizModels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.asu1.models.quiz.Quiz
import com.asu1.models.serializers.BodyType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseQuizViewModel<T : Quiz<T>>(initialQuiz: T) : ViewModel() {
    protected open val _quizState: MutableStateFlow<T> = MutableStateFlow(initialQuiz)
    val quizState: StateFlow<T> get() = _quizState.asStateFlow()

    fun updateQuestion(question: String) {
        val currentQuiz = _quizState.value
        _quizState.update {
            currentQuiz.cloneQuiz(question = question)
        }
        _quizState.value = currentQuiz.cloneQuiz(question = question)
    }

    abstract fun loadQuiz(quiz: T)
    abstract fun resetQuiz()
    fun updateAnswerAt(index: Int, answer: String) {
        val currentQuiz = _quizState.value
        _quizState.value = currentQuiz.cloneQuiz(answers = currentQuiz.answers.toMutableList().apply {
            if (index >= size) {
                add(answer)
            } else {
                set(index, answer)
            }
        })
    }
    abstract fun toggleAnsAt(index: Int)
    abstract fun removeAnswerAt(index: Int)
    abstract fun addAnswer()
    abstract fun viewerInit()
    fun updateBodyState(bodyType: BodyType){
        val currentQuiz = _quizState.value
        _quizState.value = currentQuiz.cloneQuiz(bodyType = bodyType)
    }
    fun updateBodyText(bodyText: String) {
        val currentQuiz = _quizState.value
        _quizState.value = currentQuiz.cloneQuiz(bodyType = BodyType.TEXT(bodyText = bodyText))
    }

    fun updateBodyImage(image: Bitmap?) {
        val currentQuiz = _quizState.value
        _quizState.value = if (image == null) {
            currentQuiz.cloneQuiz(bodyType = BodyType.IMAGE())
        } else {
            currentQuiz.cloneQuiz(bodyType = BodyType.IMAGE(bodyImage = image))
        }
    }

    fun updateBodyYoutube(youtubeId: String, startTime: Int) {
        val currentQuiz = _quizState.value
        _quizState.value = currentQuiz.cloneQuiz(bodyType = BodyType.YOUTUBE(youtubeId, startTime))
    }
    fun setPoint(point: Int) {
        val currentQuiz = _quizState.value
        _quizState.value = currentQuiz.cloneQuiz(point = point)
    }
}