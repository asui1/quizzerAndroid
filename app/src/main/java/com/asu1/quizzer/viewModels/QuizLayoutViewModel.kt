package com.asu1.quizzer.viewModels

import android.app.Application
import android.graphics.BitmapFactory
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.screens.quizlayout.borders
import com.asu1.quizzer.screens.quizlayout.colors
import com.asu1.quizzer.screens.quizlayout.fonts
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.withErrorColor
import com.asu1.quizzer.util.withOnErrorColor
import com.asu1.quizzer.util.withOnPrimaryColor
import com.asu1.quizzer.util.withOnSecondaryColor
import com.asu1.quizzer.util.withOnTertiaryColor
import com.asu1.quizzer.util.withPrimaryColor
import com.asu1.quizzer.util.withSecondaryColor
import com.asu1.quizzer.util.withTertiaryColor
import com.github.f4b6a3.uuid.UuidCreator
import com.materialkolor.ktx.themeColor
import com.materialkolor.ktx.themeColors
import com.materialkolor.rememberDynamicColorScheme

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

    private val _fullUpdate = MutableLiveData<Int>(0)
    val fullUpdate: LiveData<Int> get() = _fullUpdate

    fun initQuizLayout(email: String?, colorScheme: ColorScheme) {
        resetQuizLayout()
        _creator.value = email ?: "GUEST"
        _colorScheme.value = colorScheme
    }

    private fun resetQuizLayout() {
        Logger().debug("resetQuizLayout")
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
        Logger().debug("setQuizTitle: $title")
        _quizTitle.postValue(title)
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

    fun setFlipStyle(flipStyle: Int) {
        _flipStyle.value = flipStyle
    }

    fun updateBackgroundImage(imageColor: ImageColor) {
        _backgroundImage.postValue(imageColor)
    }

    fun setColorScheme(name: String, color: Color){
        val colorScheme = when(name){
            "Primary Color" -> _colorScheme.value!!.withPrimaryColor(color)
            "Secondary Color" -> _colorScheme.value!!.withSecondaryColor(color)
            "Tertiary Color" -> _colorScheme.value!!.withTertiaryColor(color)
            "onPrimary Color" -> _colorScheme.value!!.withOnPrimaryColor(color)
            "onSecondary Color" -> _colorScheme.value!!.withOnSecondaryColor(color)
            "onTertiary Color" -> _colorScheme.value!!.withOnTertiaryColor(color)
            "Error Color" -> _colorScheme.value!!.withErrorColor(color)
            "onError Color" -> _colorScheme.value!!.withOnErrorColor(color)
            else -> _colorScheme.value!!
        }
        _colorScheme.value = colorScheme
    }

    fun setColorScheme(colorScheme: ColorScheme) {
        _colorScheme.value = colorScheme
        _fullUpdate.value = _fullUpdate.value!!.plus(1)
    }

    fun updateTextStyle(targetSelector: Int, indexSelector: Int, isIncrease: Boolean) {
        when(targetSelector){
            0 -> updateQuestionTextStyle(indexSelector, isIncrease)
            1 -> updateBodyTextStyle(indexSelector, isIncrease)
            2 -> updateAnswerTextStyle(indexSelector, isIncrease)
        }
    }

    fun updateShuffleQuestions(shuffleQuestions: Boolean) {
        _shuffleQuestions.value = shuffleQuestions
    }

    fun textStyleUpdater(textStyle: List<Int>, indexSelector: Int, isIncrease: Boolean): List<Int> {
        val size = when(indexSelector){
            0 -> fonts.size
            1 -> colors.size
            2 -> borders.size
            else -> borders.size
        }
        return textStyle.toMutableList().apply {

            if(isIncrease){
                this[indexSelector] = (this[indexSelector] + 1)
            } else {
                this[indexSelector] = (this[indexSelector] - 1)
            }
            if(this[indexSelector] < 0) this[indexSelector] = size - 1
            if(this[indexSelector] >= size) this[indexSelector] = 0
        }
    }

    fun updateQuestionTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _questionTextStyle.value = textStyleUpdater(_questionTextStyle.value!!, indexSelector, isIncrease)
    }

    fun updateBodyTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _bodyTextStyle.value = textStyleUpdater(_bodyTextStyle.value!!, indexSelector, isIncrease)
    }

    fun updateAnswerTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _answerTextStyle.value = textStyleUpdater(_answerTextStyle.value!!, indexSelector, isIncrease)
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