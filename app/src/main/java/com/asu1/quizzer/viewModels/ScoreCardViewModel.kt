package com.asu1.quizzer.viewModels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.imagecolor.BackgroundBase
import com.asu1.imagecolor.Effect
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizData
import com.asu1.models.scorecard.ScoreCard
import com.asu1.resources.ShaderType
import com.asu1.resources.ViewModelState
import com.asu1.utils.images.createEmptyBitmap
import com.asu1.utils.images.processImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScoreCardViewModel : ViewModel() {
    private val _scoreCard = MutableStateFlow(ScoreCard())
    val scoreCard: StateFlow<ScoreCard> = _scoreCard.asStateFlow()

    private val _viewModelState = MutableLiveData(ViewModelState.IDLE)
    val viewModelState: MutableLiveData<ViewModelState> get() = _viewModelState

    fun loadScoreCard(scoreCard: ScoreCard) {
        _scoreCard.value = scoreCard
    }

    fun resetScoreCard() {
        _scoreCard.value = ScoreCard()
        _viewModelState.value = ViewModelState.IDLE
    }

    fun updateTextColor(color: Color){
        _scoreCard.value = _scoreCard.value.copy(textColor = color)
    }

    fun updateColor(color: Color, index: Int){
        val currentBackground = _scoreCard.value.background
        when(index){
            1 -> _scoreCard.update {
                it.copy(background = currentBackground.copy(color2 = color))
            }
            2 -> _scoreCard.update{
                it.copy(background = currentBackground.copy(colorGradient = color))
            }
            else -> _scoreCard.update{
                it.copy(background = currentBackground.copy(color = color))
            }
        }
    }

    fun updateColor1(color: Color){
        val currentBackground = _scoreCard.value.background
        _scoreCard.value = _scoreCard.value.copy(background = currentBackground.copy(color = color))
    }

    fun updateColor2(color: Color){
        val currentBackground = _scoreCard.value.background
        _scoreCard.value = _scoreCard.value.copy(background = currentBackground.copy(color2 = color))
    }

    fun onColorSelection(color: Color){
        val currentBackground = _scoreCard.value.background

        if (currentBackground.color == color) {
            _scoreCard.value = _scoreCard.value.copy(background = currentBackground.copy(color = Color.Transparent))
        } else if (currentBackground.color2 == color) {
            _scoreCard.value = _scoreCard.value.copy(background = currentBackground.copy(color2 = Color.Transparent))
        } else {
            if (currentBackground.color == Color.Transparent) {
                _scoreCard.value = _scoreCard.value.copy(background = currentBackground.copy(color = color))
            } else if (currentBackground.color2 == Color.Transparent) {
                _scoreCard.value = _scoreCard.value.copy(background = currentBackground.copy(color2 = color))
            }
        }

    }

    fun updateBackgroundImage(image: Bitmap?){
        if(image == null){
            _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(imageData = createEmptyBitmap(), state = ImageColorState.IMAGE))
            return
        }
        _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(imageData = image, state = ImageColorState.IMAGE, color = Color.White))
    }

    fun updateBackgroundBase(base: BackgroundBase){
        _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(backgroundBase = base, state = ImageColorState.BASEIMAGE))
    }

    fun updateScoreCard(quizData: QuizData, colorScheme: ColorScheme){
        _scoreCard.value = _scoreCard.value.copy(
            title = quizData.title,
            solver = quizData.creator,
            colorScheme = colorScheme,
        )
    }

    fun updateBackgroundState(imageColorState: ImageColorState){
        _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(state = imageColorState))
    }

    fun updateShaderType(shaderType: ShaderType){
        _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(shaderType = shaderType, state = ImageColorState.GRADIENT))
    }

    fun updateEffect(effect: Effect){
        _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(effect = effect))
    }

    fun updateOverLayImage(bitmap: Bitmap?){
        if(bitmap == null){
            _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(overlayImage = createEmptyBitmap()))
            return
        }
        viewModelScope.launch {
            processImage(bitmap){
                _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(overlayImage = it))
            }
        }
    }

    fun updateImageColorState(imageColorState: ImageColorState){
        _scoreCard.value = _scoreCard.value.copy(background = _scoreCard.value.background.copy(state = imageColorState))
    }

    fun updateScore(score: Float){
        _scoreCard.value = _scoreCard.value.copy(score = score)
    }

    fun updateImageState(imageState: Int){
        _scoreCard.value = _scoreCard.value.copy(imageStateval = imageState)
    }

    fun updateBackground(background: ImageColor){
        _scoreCard.value = _scoreCard.value.copy(background = background)
    }
}

fun createSampleScoreCardViewModel(): ScoreCardViewModel {
    val viewModel = ScoreCardViewModel()
    val sampleQuizLayout = createSampleQuizLayoutViewModel()
    viewModel.updateScoreCard(quizData = sampleQuizLayout.quizData.value, colorScheme = sampleQuizLayout.quizTheme.value.colorScheme)
    viewModel.updateBackgroundState(ImageColorState.COLOR)
    return viewModel
}