package com.asu1.quizzer.viewModels.quizModels

import androidx.lifecycle.ViewModel
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseQuizViewModel<T : Quiz> : ViewModel() {
    protected val _quizState = MutableStateFlow<T?>(null)
    val quizState: StateFlow<T?> = _quizState.asStateFlow()

    abstract fun loadQuiz(quiz: T)
    abstract fun resetQuiz()
    abstract fun updateAnswerAt(index: Int, answer: String)
    abstract fun toggleAnsAt(index: Int)
    abstract fun removeAnswerAt(index: Int)
    abstract fun addAnswer()
    abstract fun updateQuestion(question: String)
    abstract fun viewerInit()
    abstract fun updateBodyState(bodyType: BodyType)
    abstract fun updateBodyText(bodyText: String)
    abstract fun updateBodyImage(image: ByteArray)
    abstract fun updateBodyYoutube(youtubeId: String, startTime: Int)
    abstract fun setPoint(point: Int)
}