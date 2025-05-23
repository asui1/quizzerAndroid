package com.asu1.quiz.viewmodel.quizLayout

import android.graphics.Bitmap
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.asu1.colormodel.ThemeColorPicker
import com.asu1.colormodel.updateError
import com.asu1.colormodel.updateErrorContainer
import com.asu1.colormodel.updateOutline
import com.asu1.colormodel.updatePrimary
import com.asu1.colormodel.updatePrimaryContainer
import com.asu1.colormodel.updateSecondary
import com.asu1.colormodel.updateSecondaryContainer
import com.asu1.colormodel.updateSurfaceGroup
import com.asu1.colormodel.updateTertiary
import com.asu1.colormodel.updateTertiaryContainer
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizTheme
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.BodyTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.resources.ViewModelState
import com.asu1.resources.borders
import com.asu1.resources.colors
import com.asu1.resources.fonts
import com.asu1.resources.outlines
import com.asu1.utils.images.createEmptyBitmap
import com.asu1.utils.shaders.ShaderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizThemeViewModel : ViewModel() {
    private val _quizThemeViewModelState = MutableStateFlow(ViewModelState.IDLE)
    val quizThemeViewModelState = _quizThemeViewModelState.asStateFlow()

    private val _quizTheme = MutableStateFlow(QuizTheme())
    val quizTheme: StateFlow<QuizTheme> = _quizTheme.asStateFlow()

    fun resetQuizTheme(){
        _quizTheme.value = QuizTheme()
        initTextStyleManager()
    }

    fun loadQuizTheme(quizTheme: QuizTheme){
        _quizTheme.update {
            quizTheme
        }
        initTextStyleManager()
    }

    fun initTextStyleManager(){
        QuestionTextStyle.update(_quizTheme.value.questionTextStyle, _quizTheme.value.colorScheme)
        BodyTextStyle.update(_quizTheme.value.bodyTextStyle, _quizTheme.value.colorScheme)
        AnswerTextStyle.update(_quizTheme.value.answerTextStyle, _quizTheme.value.colorScheme)
    }

    fun updateBackgroundImage(image: Bitmap?) {
        _quizTheme.update {
            it.copy(backgroundImage = it.backgroundImage.copy(imageData = image ?: createEmptyBitmap()))
        }
    }

    fun updateBackgroundColor(color: Color) {
        _quizTheme.update {
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

    fun updateColorScheme(name: ThemeColorPicker, color: Color) {
        // grab the old color
        val old = when(name) {
            ThemeColorPicker.Primary          -> _quizTheme.value.colorScheme.primary
            ThemeColorPicker.PrimaryContainer -> _quizTheme.value.colorScheme.primaryContainer
            ThemeColorPicker.Secondary        -> _quizTheme.value.colorScheme.secondary
            ThemeColorPicker.SecondaryContainer -> _quizTheme.value.colorScheme.secondaryContainer
            ThemeColorPicker.Tertiary -> _quizTheme.value.colorScheme.tertiary
            ThemeColorPicker.TertiaryContainer -> _quizTheme.value.colorScheme.tertiaryContainer
            ThemeColorPicker.Error -> _quizTheme.value.colorScheme.error
            ThemeColorPicker.ErrorContainer -> _quizTheme.value.colorScheme.errorContainer
            ThemeColorPicker.Surface -> _quizTheme.value.colorScheme.surface
            ThemeColorPicker.Outline -> _quizTheme.value.colorScheme.outline
        }
        if (old == color) return

        _quizTheme.update {
            val updatedScheme = when (name) {
                ThemeColorPicker.Primary -> _quizTheme.value.colorScheme.updatePrimary(color)
                ThemeColorPicker.PrimaryContainer -> _quizTheme.value.colorScheme.updatePrimaryContainer(color)
                ThemeColorPicker.Secondary -> _quizTheme.value.colorScheme.updateSecondary(color)
                ThemeColorPicker.SecondaryContainer -> _quizTheme.value.colorScheme.updateSecondaryContainer(color)
                ThemeColorPicker.Tertiary -> _quizTheme.value.colorScheme.updateTertiary(color)
                ThemeColorPicker.TertiaryContainer -> _quizTheme.value.colorScheme.updateTertiaryContainer(color)
                ThemeColorPicker.Error -> _quizTheme.value.colorScheme.updateError(color)
                ThemeColorPicker.ErrorContainer -> _quizTheme.value.colorScheme.updateErrorContainer(color)
                ThemeColorPicker.Surface -> _quizTheme.value.colorScheme.updateSurfaceGroup(color)
                ThemeColorPicker.Outline -> _quizTheme.value.colorScheme.updateOutline(color)
            }
            it.copy(colorScheme = updatedScheme)
        }
        initTextStyleManager()
    }

    fun updateColorScheme(colorScheme: ColorScheme) {
        _quizTheme.update {
            it.copy(
                colorScheme = colorScheme,
                backgroundImage = it.backgroundImage.copy(color = colorScheme.surface)
            )
        }
        initTextStyleManager()
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
            val newStyle = textStyleUpdater(it.questionTextStyle, indexSelector, isIncrease)
            QuestionTextStyle.update(newStyle, it.colorScheme, indexSelector)
            it.copy(questionTextStyle = newStyle)
        }
    }

    fun updateBodyTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.update {
            val newStyle = textStyleUpdater(it.bodyTextStyle, indexSelector, isIncrease)
            BodyTextStyle.update(newStyle, it.colorScheme, indexSelector)
            it.copy(bodyTextStyle = newStyle)
        }
    }

    fun updateAnswerTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.update {
            val newStyle = textStyleUpdater(it.answerTextStyle, indexSelector, isIncrease)
            AnswerTextStyle.update(newStyle, it.colorScheme, indexSelector)
            it.copy(answerTextStyle = newStyle)
        }
    }

    fun updateQuizTheme(action: QuizThemeActions){
        when(action){
            is QuizThemeActions.InitTextStyleManager -> initTextStyleManager()
            is QuizThemeActions.UpdateColor -> updateColorScheme(action.colorType, action.color)
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
    data class UpdateColor(val colorType: ThemeColorPicker, val color: Color): QuizThemeActions()
    data class UpdateBackgroundColor(val color: Color): QuizThemeActions()
    data class UpdateGradientColor(val color: Color): QuizThemeActions()
    data class UpdateBackgroundImage(val bitmap: Bitmap?): QuizThemeActions()
    data class UpdateBackgroundType(val backgroundState: ImageColorState): QuizThemeActions()
    data class UpdateGradientType(val shaderType: ShaderType): QuizThemeActions()
    data class UpdateTextStyle(val targetSelector: Int, val index: Int, val isIncrease: Boolean): QuizThemeActions()
}
