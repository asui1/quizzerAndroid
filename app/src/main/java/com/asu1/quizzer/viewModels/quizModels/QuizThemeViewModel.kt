package com.asu1.quizzer.viewModels.quizModels

import android.graphics.Bitmap
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizTheme
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.quizzer.screens.quizlayout.randomDynamicColorScheme
import com.asu1.resources.ColorList
import com.asu1.resources.GenerateWith
import com.asu1.resources.ViewModelState
import com.asu1.resources.borders
import com.asu1.resources.colors
import com.asu1.resources.contrastSize
import com.asu1.resources.fonts
import com.asu1.resources.outlines
import com.asu1.resources.paletteSize
import com.asu1.utils.Logger
import com.asu1.utils.calculateSeedColor
import com.asu1.utils.images.createEmptyBitmap
import com.asu1.utils.shaders.ShaderType
import com.asu1.utils.toScheme
import com.asu1.utils.withPrimaryColor
import com.asu1.utils.withSecondaryColor
import com.asu1.utils.withTertiaryColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlin.collections.plus

@HiltViewModel
class QuizThemeViewModel @Inject constructor() : ViewModel() {
    private val _quizThemeViewModelState = MutableStateFlow(ViewModelState.IDLE)
    val quizThemeViewModelState = _quizThemeViewModelState.asStateFlow()

    private val _quizTheme = MutableStateFlow(QuizTheme())
    val quizTheme: StateFlow<QuizTheme> = _quizTheme.asStateFlow()

    private val _titleImageColors = MutableStateFlow<List<Color>>(emptyList())
    val titleImageColors: StateFlow<List<Color>> get() = _titleImageColors.asStateFlow()

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

    fun updateTitleImageColors(image: Bitmap?){
        if(image == null){
            _titleImageColors.value = emptyList()
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            val seedColor = calculateSeedColor(image.asImageBitmap())
            if(seedColor.size < 3){
                _titleImageColors.value = seedColor + List(3 - seedColor.size) { seedColor[0] }
            }
            _titleImageColors.value = seedColor
        }
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

    fun generateColorScheme(
        base: GenerateWith,
        paletteLevel: Int,
        contrastLevel: Int,
        isDark: Boolean,
    ) {
        viewModelScope.launch {
            if (contrastLevel in 0 until contrastSize && paletteLevel in 0..paletteSize) {
                val newColorScheme = when (base) {
                    GenerateWith.TITLE_IMAGE -> {
                        if (_titleImageColors.value.isEmpty()) return@launch
                        if (paletteLevel == paletteSize) {
                            toScheme(
                                primary = _titleImageColors.value[0],
                                secondary = _titleImageColors.value[1],
                                tertiary = _titleImageColors.value[2],
                                isLight = !isDark
                            )
                        } else {
                            randomDynamicColorScheme(_titleImageColors.value[0], paletteLevel, contrastLevel, isDark)
                        }
                    }

                    GenerateWith.COLOR -> {
                        val currentScheme = _quizTheme.value.colorScheme
                        if (paletteLevel == paletteSize) {
                            toScheme(
                                primary = currentScheme.primary,
                                secondary = currentScheme.secondary,
                                tertiary = currentScheme.tertiary,
                                isLight = !isDark
                            )
                        } else {
                            randomDynamicColorScheme(currentScheme.primary, paletteLevel, contrastLevel, isDark)
                        }
                    }
                }
                updateColorScheme(newColorScheme)
            }
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
            is QuizThemeActions.GenerateColorScheme -> generateColorScheme(action.generateWith, action.palette, action.contrast, action.isDark)
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
    data class GenerateColorScheme(val generateWith: GenerateWith, val palette: Int, val contrast: Int, val isDark: Boolean): QuizThemeActions()
    data class UpdateBackgroundColor(val color: Color): QuizThemeActions()
    data class UpdateGradientColor(val color: Color): QuizThemeActions()
    data class UpdateBackgroundImage(val bitmap: Bitmap?): QuizThemeActions()
    data class UpdateBackgroundType(val backgroundState: ImageColorState): QuizThemeActions()
    data class UpdateGradientType(val shaderType: ShaderType): QuizThemeActions()
    data class UpdateTextStyle(val targetSelector: Int, val index: Int, val isIncrease: Boolean): QuizThemeActions()
}
