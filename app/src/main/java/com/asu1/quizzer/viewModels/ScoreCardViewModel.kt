package com.asu1.quizzer.viewModels

import androidx.compose.material3.ColorScheme
import androidx.lifecycle.ViewModel
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.model.ShaderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreCardViewModel : ViewModel() {
    private val _scoreCard = MutableStateFlow(ScoreCard())
    val scoreCard: StateFlow<ScoreCard> = _scoreCard.asStateFlow()

    fun resetScoreCard() {
        _scoreCard.value = ScoreCard()
    }

    fun updateBackgroundImage(image: ByteArray){
        _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(image = image, state = ImageColorState.IMAGE))
    }

    fun updateScoreCard(quizData: QuizData, colorScheme: ColorScheme){
        val currentColorScheme = _scoreCard.value.colorScheme
        val imageColor = _scoreCard.value.background

        if (currentColorScheme != colorScheme) {
            _scoreCard.value = _scoreCard.value.copy(
                title = quizData.title,
                creator = quizData.creator,
                colorScheme = colorScheme,
                background = ImageColor(
                    color = colorScheme.primary,
                    image = imageColor.image,
                    color2 = colorScheme.secondary,
                    state = imageColor.state
                )
            )
        } else {
            _scoreCard.value = _scoreCard.value.copy(
                title = quizData.title,
                creator = quizData.creator,
                colorScheme = colorScheme
            )
        }
    }

    fun updateBackgroundState(imageColorState: ImageColorState){
        _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(state = imageColorState))
    }

    fun updateShaderType(shaderType: ShaderType){
        _scoreCard.value = _scoreCard.value.copy(shaderType = shaderType)
    }

    fun updateImageColorState(imageColorState: ImageColorState){
        _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(state = imageColorState))
    }

    fun updateScore(score: Int){
        _scoreCard.value = _scoreCard.value.copy(score = score)
    }

    fun updateImageState(imageState: Int){
        _scoreCard.value = _scoreCard.value.copy(imageStateval = imageState)
    }

    fun updateBackground(background: ImageColor){
        _scoreCard.value = _scoreCard.value.copy(background = background)
    }

    fun updateRatio(x: Float, y: Float){
        _scoreCard.value = _scoreCard.value.copy(xRatio = x, yRatio = y)
    }

    fun updateSize(size: Float){
        _scoreCard.value = _scoreCard.value.copy(size = size)
    }
}

fun createSampleScoreCardViewModel(): ScoreCardViewModel {
    val viewModel = ScoreCardViewModel()
    val sampleQuizLayout = createSampleQuizLayoutViewModel()
    viewModel.updateScoreCard(quizData = sampleQuizLayout.quizData.value, colorScheme = sampleQuizLayout.quizTheme.value.colorScheme)
    viewModel.updateBackgroundState(ImageColorState.COLOR)
    return viewModel
}