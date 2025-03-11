package com.asu1.quizzer.viewModels.quizModels

import android.graphics.Bitmap
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizTheme
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.resources.ColorList
import com.asu1.resources.ViewModelState
import com.asu1.resources.borders
import com.asu1.resources.colors
import com.asu1.resources.fonts
import com.asu1.resources.outlines
import com.asu1.utils.Logger
import com.asu1.utils.images.createEmptyBitmap
import com.asu1.utils.shaders.ShaderType
import com.asu1.utils.withPrimaryColor
import com.asu1.utils.withSecondaryColor
import com.asu1.utils.withTertiaryColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class QuizThemeViewModel @Inject constructor() : ViewModel() {
    private val _quizThemeViewModelState = MutableStateFlow(ViewModelState.IDLE)
    val quizThemeViewModelState = _quizThemeViewModelState.asStateFlow()

    private val _quizTheme = MutableStateFlow(QuizTheme())
    val quizTheme: StateFlow<QuizTheme> = _quizTheme.asStateFlow()

    private var textStyleManager = TextStyleManager()

    fun resetQuizTheme(){
        _quizTheme.value = QuizTheme()
        textStyleManager = TextStyleManager()
    }

    fun loadQuizTheme(quizTheme: QuizTheme){
        _quizTheme.update {
            quizTheme
        }
        initTextStyleManager()
    }



    fun initTextStyleManager(){
        textStyleManager.initTextStyleManager(
            colorScheme = quizTheme.value.colorScheme,
            questionStyle = quizTheme.value.questionTextStyle,
            answerStyle = quizTheme.value.answerTextStyle,
            bodyStyle = quizTheme.value.bodyTextStyle
        )
    }
    fun getTextStyleManager(): TextStyleManager {
        return textStyleManager
    }

    fun updateBackgroundImage(image: Bitmap?) {
        _quizTheme.update {
            it.copy(backgroundImage = it.backgroundImage.copy(imageData = image ?: createEmptyBitmap()))
        }
    }

    fun updateBackgroundColor(color: Color) {
        _quizTheme.update {
            Logger.debug("UPDATE BACKGROUND COLOR: ${color}")
            it.copy(backgroundImage = it.backgroundImage.copy(color = color))
        }
    }

    fun updateBackgroundState(state: ImageColorState) {
        _quizTheme.update {
            it.copy(backgroundImage = it.backgroundImage.copy(state = state))
        }
    }

    fun updateGradientColor(gradientColor: Color) {
        _quizTheme.update {
            it.copy(backgroundImage = it.backgroundImage.copy(colorGradient = gradientColor))
        }
    }

    fun updateGradientType(shaderType: ShaderType) {
        _quizTheme.update {
            it.copy(backgroundImage = it.backgroundImage.copy(shaderType = shaderType))
        }
    }

    fun updateColorScheme(name: String, color: Color) {
        _quizTheme.update {
            val updatedScheme = when (name) {
                ColorList[0] -> it.colorScheme.withPrimaryColor(color)
                ColorList[1] -> it.colorScheme.withSecondaryColor(color)
                ColorList[2] -> it.colorScheme.withTertiaryColor(color)
                else -> it.colorScheme
            }
            it.copy(colorScheme = updatedScheme)
        }
    }

    fun updateColorScheme(colorScheme: ColorScheme) {
        _quizTheme.update {
            it.copy(
                colorScheme = colorScheme,
                backgroundImage = it.backgroundImage.copy(color = colorScheme.surface)
            )
        }
    }

    fun updateTextStyle(targetSelector: Int, indexSelector: Int, isIncrease: Boolean) {
        when (targetSelector) {
            0 -> updateQuestionTextStyle(indexSelector, isIncrease)
            1 -> updateBodyTextStyle(indexSelector, isIncrease)
            2 -> updateAnswerTextStyle(indexSelector, isIncrease)
        }
    }

    fun textStyleUpdater(textStyle: List<Int>, indexSelector: Int, isIncrease: Boolean): List<Int> {
        val size = when (indexSelector) {
            0 -> fonts.size
            1 -> colors.size
            2 -> borders.size
            4 -> outlines.size
            else -> borders.size
        }
        return textStyle.toMutableList().apply {
            this[indexSelector] = (this[indexSelector] + if (isIncrease) 1 else -1).mod(size)
        }
    }

    fun updateQuestionTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.update {
            it.copy(questionTextStyle = textStyleUpdater(it.questionTextStyle, indexSelector, isIncrease))
        }
    }

    fun updateBodyTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.update {
            it.copy(bodyTextStyle = textStyleUpdater(it.bodyTextStyle, indexSelector, isIncrease))
        }
    }

    fun updateAnswerTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.update {
            it.copy(answerTextStyle = textStyleUpdater(it.answerTextStyle, indexSelector, isIncrease))
        }
    }

    fun updateQuizTheme(action: QuizThemeActions){
        when(action){
            is QuizThemeActions.InitTextStyleManager -> initTextStyleManager()
            is QuizThemeActions.UpdateColor -> updateColorScheme(action.colorName, action.color)
            is QuizThemeActions.UpdateBackgroundColor -> updateBackgroundColor(action.color)
            is QuizThemeActions.UpdateGradientColor -> updateGradientColor(action.color)
            is QuizThemeActions.UpdateBackgroundImage -> updateBackgroundImage(action.bitmap)
            is QuizThemeActions.UpdateBackgroundType -> updateBackgroundState(action.backgroundState)
            is QuizThemeActions.UpdateGradientType -> updateGradientType(action.shaderType)
            is QuizThemeActions.UpdateTextStyle -> updateTextStyle(action.targetSelector, action.index, action.isIncrease)
        }
    }
}

sealed class QuizThemeActions{
    data object InitTextStyleManager: QuizThemeActions()
    data class UpdateColor(val colorName: String, val color: Color): QuizThemeActions()
    data class UpdateBackgroundColor(val color: Color): QuizThemeActions()
    data class UpdateGradientColor(val color: Color): QuizThemeActions()
    data class UpdateBackgroundImage(val bitmap: Bitmap?): QuizThemeActions()
    data class UpdateBackgroundType(val backgroundState: ImageColorState): QuizThemeActions()
    data class UpdateGradientType(val shaderType: ShaderType): QuizThemeActions()
    data class UpdateTextStyle(val targetSelector: Int, val index: Int, val isIncrease: Boolean): QuizThemeActions()
}
