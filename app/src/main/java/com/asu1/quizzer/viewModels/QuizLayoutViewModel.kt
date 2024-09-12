package com.asu1.quizzer.viewModels

import android.app.Application
import androidx.compose.material3.ColorScheme
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.Quiz
import com.github.f4b6a3.uuid.UuidCreator

class QuizLayoutViewModel(application: Application) : AndroidViewModel(application) {
    private val _quizTitle = MutableLiveData<String>()
    val quizTitle: LiveData<String> get() = _quizTitle

    private val _quizImage = MutableLiveData<ByteArray?>()
    val quizImage: LiveData<ByteArray?> get() = _quizImage

    private val _quizDescription = MutableLiveData<String>()
    val quizDescription: LiveData<String> get() = _quizDescription

    private val _quizTags = MutableLiveData<List<String>>()
    val quizTags: LiveData<List<String>> get() = _quizTags

    private val _flipStyle = MutableLiveData<Int>()
    val flipStyle: LiveData<Int> get() = _flipStyle

    private val _backgroundImage = MutableLiveData<ImageColor?>()
    val backgroundImage: LiveData<ImageColor?> get() = _backgroundImage

    private val _shuffleQuestions = MutableLiveData<Boolean>()
    val shuffleQuestions: LiveData<Boolean> get() = _shuffleQuestions

    private val _questionTextStyle = MutableLiveData<List<Int>>()
    val questionTextStyle: LiveData<List<Int>> get() = _questionTextStyle

    private val _bodyTextStyle = MutableLiveData<List<Int>>()
    val bodyTextStyle: LiveData<List<Int>> get() = _bodyTextStyle

    private val _answerTextStyle = MutableLiveData<List<Int>>()
    val answerTextStyle: LiveData<List<Int>> get() = _answerTextStyle

    private val _creator = MutableLiveData<String>()
    val creator: LiveData<String> get() = _creator

    private val _uuid = MutableLiveData<String?>()
    val uuid: LiveData<String?> get() = _uuid

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>> get() = _quizzes

    private val _colorScheme = MutableLiveData<ColorScheme>()
    val colorScheme: LiveData<ColorScheme> get() = _colorScheme

    fun initQuizLayout(email: String?, colorScheme: ColorScheme) {
        resetQuizLayout()
        _creator.value = email ?: "GUEST"
        _colorScheme.value = colorScheme
    }

    fun resetQuizLayout() {
        _quizTitle.value = ""
        _quizImage.value = null
        _quizDescription.value = ""
        _quizTags.value = emptyList()
        _flipStyle.value = 0
        _backgroundImage.value = null
        _shuffleQuestions.value = false
        _questionTextStyle.value = listOf(0, 0, 1, 0)
        _bodyTextStyle.value = listOf(0, 0, 2, 1)
        _answerTextStyle.value = listOf(0, 0, 0, 2)
        _uuid.value = null
        _quizzes.value = emptyList()
        _creator.value = "GUEST"
    }

    fun setQuizTitle(title: String) {
        _quizTitle.value = title
    }

    fun setQuizImage(image: ByteArray) {
        _quizImage.value = image
    }

    fun setQuizDescription(description: String) {
        _quizDescription.value = description
    }

    fun addTag(tag: String) {
        val tags = _quizTags.value!!.toMutableList()
        tags.add(tag)
        _quizTags.value = tags
    }

    fun removeTag(tag: String) {
        val tags = _quizTags.value!!.toMutableList()
        tags.remove(tag)
        _quizTags.value = tags
    }

    fun updateQuizTags(tags: List<String>) {
        _quizTags.value = tags
    }

    fun setFlipStyle(flipStyle: Int) {
        _flipStyle.value = flipStyle
    }

    fun updateBackgroundImage(imageColor: ImageColor) {
        _backgroundImage.postValue(imageColor)
    }

    fun updateShuffleQuestions(shuffleQuestions: Boolean) {
        _shuffleQuestions.value = shuffleQuestions
    }

    fun updateQuestionTextStyle(style: List<Int>) {
        _questionTextStyle.value = style
    }

    fun updateBodyTextStyle(style: List<Int>) {
        _bodyTextStyle.value = style
    }

    fun updateAnswerTextStyle(style: List<Int>) {
        _answerTextStyle.value = style
    }

    fun updateCreator(creator: String) {
        _creator.value = creator
    }

    fun generateUUIDWithTitle() {
        if(_quizTitle.value.isNullOrEmpty()) return

        if(_uuid.value.isNullOrEmpty()) {
            val name = "${_quizTitle.value}-${System.currentTimeMillis()}"
            _uuid.value =  UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, name).toString()
        }

    }
    fun updateQuizzes(quizzes: List<Quiz>) {
        _quizzes.value = quizzes
    }

    fun addQuiz(quiz: Quiz, index: Int? = null) {
        if(index == null){
            val quizzes = quizzes.value!!.toMutableList()
            quizzes.add(quiz)
            _quizzes.value = quizzes
            return
        } else{
            val quizzes = quizzes.value!!.toMutableList()
            quizzes.add(index, quiz)
            _quizzes.value = quizzes
            return
        }
    }

    fun removeQuiz(quiz: Quiz) {
        val quizzes = quizzes.value!!.toMutableList()
        quizzes.remove(quiz)
        _quizzes.value = quizzes
    }

    fun updateColorScheme(colorScheme: ColorScheme) {
        _colorScheme.value = colorScheme
    }
}