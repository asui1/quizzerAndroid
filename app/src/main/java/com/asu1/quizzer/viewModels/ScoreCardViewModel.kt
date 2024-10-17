package com.asu1.quizzer.viewModels

import androidx.compose.material3.ColorScheme
import androidx.lifecycle.ViewModel
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ScoreCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreCardViewModel : ViewModel() {
    private val _scoreCard = MutableStateFlow(ScoreCard())
    val scoreCard: StateFlow<ScoreCard> = _scoreCard.asStateFlow()

    fun resetScoreCard() {
        _scoreCard.value = ScoreCard()
    }

    fun updateScoreCard(quizData: QuizData, colorScheme: ColorScheme){
        _scoreCard.value = _scoreCard.value.copy(
            title = quizData.title,
            creator = quizData.creator,
            colorScheme = colorScheme
        )
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
    return viewModel
}