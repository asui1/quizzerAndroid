package com.asu1.quizzer.viewModels

import android.app.Application
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.screens.quizlayout.borders
import com.asu1.quizzer.screens.quizlayout.colors
import com.asu1.quizzer.screens.quizlayout.fonts
import com.asu1.quizzer.ui.theme.LightColorScheme
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class QuizTheme(
    var flipStyle: FlipStyle = FlipStyle.Center,
    var backgroundImage: ImageColor = ImageColor(Color.White, byteArrayOf(), Color.White, ImageColorState.COLOR),
    var questionTextStyle: List<Int> = listOf(0, 0, 1, 0),
    var bodyTextStyle: List<Int> = listOf(0, 0, 2, 1),
    var answerTextStyle: List<Int> = listOf(0, 0, 0, 2),
    var colorScheme: ColorScheme = LightColorScheme,
)

data class QuizData(
    var title: String = "",
    var image: ByteArray? = null,
    var description: String = "",
    var tags: Set<String> = emptySet(),
    var shuffleQuestions: Boolean = false,
    var creator: String = "GUEST",
    var uuid : String? = null,
)

enum class FlipStyle(val value: Int) {
    Center(0),
    Bottom(1),
    UnderBar(2),
    FingerFlip(3), ;

    companion object {
        fun from(flipStyle: Int): FlipStyle {
            return when (flipStyle) {
                0 -> Center
                1 -> Bottom
                2 -> UnderBar
                3 -> FingerFlip
                else -> Center
            }
        }
    }
}


class QuizLayoutViewModel(application: Application) : AndroidViewModel(application) {
    private val _quizTheme = MutableStateFlow(QuizTheme())
    val quizTheme: StateFlow<QuizTheme> = _quizTheme.asStateFlow()

    private val _quizData = MutableStateFlow(QuizData())
    val quizData: StateFlow<QuizData> = _quizData.asStateFlow()


    private val _quizzes = MutableLiveData<List<Quiz>>(emptyList())
    val quizzes: LiveData<List<Quiz>> get() = _quizzes

    fun initQuizLayout(email: String?, colorScheme: ColorScheme) {
        resetQuizLayout()
        _quizData.value = _quizData.value.copy(creator = email ?: "GUEST")
        _quizTheme.value = _quizTheme.value.copy(colorScheme = colorScheme)
    }

    private fun resetQuizLayout() {
        Logger().debug("resetQuizLayout")
        _quizTheme.value = QuizTheme()
        _quizData.value = QuizData()
        _quizzes.value = emptyList()
    }

    fun setQuizTitle(title: String) {
        _quizData.value = _quizData.value.copy(title = title)
    }

    fun setQuizImage(image: ByteArray) {
        _quizData.value = _quizData.value.copy(image = image)
    }

    fun setQuizDescription(description: String) {
        _quizData.value = _quizData.value.copy(description = description)
    }

    fun addTag(tag: String) {
        val tags = _quizData.value.tags.toMutableSet()
        tags.add(tag)
        _quizData.value = _quizData.value.copy(tags = tags)
    }

    fun removeTag(tag: String) {
        val tags = _quizData.value.tags.toMutableSet()
        tags.remove(tag)
        _quizData.value = _quizData.value.copy(tags = tags)
    }

    fun updateTag(tag: String){
        val tags = _quizData.value.tags.toMutableSet()
        if(tags.contains(tag)){
            tags.remove(tag)
        } else {
            tags.add(tag)
        }
        _quizData.value = _quizData.value.copy(tags = tags)
    }

    fun setFlipStyle(flipStyle: FlipStyle) {
        _quizTheme.value = _quizTheme.value.copy(flipStyle = flipStyle)
    }

    fun setBackgroundImage(imageColor: ImageColor) {
        _quizTheme.value = _quizTheme.value.copy(backgroundImage = imageColor)
    }

    fun setColorScheme(name: String, color: Color){
        val colorScheme = _quizTheme.value.colorScheme
        val updateColorScheme = when(name){
            "Primary Color" -> colorScheme.withPrimaryColor(color)
            "Secondary Color" -> colorScheme.withSecondaryColor(color)
            "Tertiary Color" -> colorScheme.withTertiaryColor(color)
            "onPrimary Color" -> colorScheme.withOnPrimaryColor(color)
            "onSecondary Color" -> colorScheme.withOnSecondaryColor(color)
            "onTertiary Color" -> colorScheme.withOnTertiaryColor(color)
            "Error Color" -> colorScheme.withErrorColor(color)
            "onError Color" -> colorScheme.withOnErrorColor(color)
            else -> colorScheme
        }
        _quizTheme.value = _quizTheme.value.copy(colorScheme = updateColorScheme)
    }

    fun setColorScheme(colorScheme: ColorScheme) {
        _quizTheme.value = _quizTheme.value.copy(colorScheme = colorScheme, backgroundImage = ImageColor(colorScheme.surface, _quizTheme.value.backgroundImage.image, _quizTheme.value.backgroundImage.color2, _quizTheme.value.backgroundImage.state))
    }

    fun updateTextStyle(targetSelector: Int, indexSelector: Int, isIncrease: Boolean) {
        when(targetSelector){
            0 -> updateQuestionTextStyle(indexSelector, isIncrease)
            1 -> updateBodyTextStyle(indexSelector, isIncrease)
            2 -> updateAnswerTextStyle(indexSelector, isIncrease)
        }
    }

    fun updateShuffleQuestions(shuffleQuestions: Boolean) {
        _quizData.value = _quizData.value.copy(shuffleQuestions = shuffleQuestions)
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
        _quizTheme.value = _quizTheme.value.copy(questionTextStyle = textStyleUpdater(_quizTheme.value.questionTextStyle, indexSelector, isIncrease))
    }

    fun updateBodyTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.value = _quizTheme.value.copy(bodyTextStyle = textStyleUpdater(_quizTheme.value.bodyTextStyle, indexSelector, isIncrease))
    }

    fun updateAnswerTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.value = _quizTheme.value.copy(answerTextStyle = textStyleUpdater(_quizTheme.value.answerTextStyle, indexSelector, isIncrease))
    }

    fun updateCreator(creator: String) {
        _quizData.value = _quizData.value.copy(creator = creator)
    }

    fun generateUUIDWithTitle() {
        if(_quizData.value.title.isEmpty()) return
        val name = "${_quizData.value.title}-${System.currentTimeMillis()}"
        if(_quizData.value.uuid.isNullOrEmpty()) {
            _quizData.value = _quizData.value.copy(uuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, name).toString())
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
        _quizTheme.value = _quizTheme.value.copy(colorScheme = colorScheme)
    }
}