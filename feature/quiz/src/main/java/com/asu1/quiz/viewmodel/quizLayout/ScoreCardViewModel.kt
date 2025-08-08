package com.asu1.quiz.viewmodel.quizLayout

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.imagecolor.BackgroundBase
import com.asu1.imagecolor.Effect
import com.asu1.imagecolor.ImageBlendMode
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.scorecard.ScoreCard
import com.asu1.resources.ViewModelState
import com.asu1.utils.Logger
import com.asu1.utils.images.createEmptyBitmap
import com.asu1.utils.images.processImage
import com.asu1.utils.shaders.ShaderType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScoreCardViewModel: ViewModel() {
    private val _scoreCardViewModelState = MutableStateFlow(ViewModelState.IDLE)
    val scoreCardViewModelState = _scoreCardViewModelState.asStateFlow()

    private val _scoreCardState = MutableStateFlow(ScoreCardState())
    val scoreCardState = _scoreCardState.asStateFlow()

    fun loadScoreCard(scoreCard: ScoreCard) {
        _scoreCardState.update { currentState ->
            currentState.copy(scoreCard = scoreCard)
        }
    }

    fun resetScoreCard() {
        _scoreCardState.update { currentState ->
            currentState.copy(scoreCard = ScoreCard())
        }
    }

    fun updateTextColor(color: Color) {
        _scoreCardState.update { currentState ->
            currentState.copy(
                scoreCard = currentState.scoreCard.copy(textColor = color)
            )
        }
    }

    fun updateColor(color: Color, index: Int) {
        try {
            require(index in 0..2) { "Invalid background color index: $index" }

            _scoreCardState.update { currentState ->
                val currentBackground = currentState.scoreCard.background
                val updatedBackground = when (index) {
                    1 -> currentBackground.copy(color2 = color)
                    2 -> currentBackground.copy(colorGradient = color)
                    else -> currentBackground.copy(color = color)
                }
                currentState.copy(
                    scoreCard = currentState.scoreCard.copy(background = updatedBackground)
                )
            }
        } catch (e: IllegalArgumentException) {
            Logger.debug("Invalid color index: ${e.message}")
        } catch (e: IllegalStateException) {
            Logger.debug("ScoreCard state update failed: ${e.message}")
        }
    }

    fun updateBackgroundImage(image: Bitmap?){
        if(image == null){
            _scoreCardState.update {
                it.copy(
                    scoreCard = it.scoreCard.copy(
                        background = it.scoreCard.background.copy(imageData = createEmptyBitmap(), state = ImageColorState.IMAGE))
                )
            }
            return
        }
        _scoreCardState.update {
            it.copy(
                scoreCard = it.scoreCard.copy(
                    background = it.scoreCard.background.copy(
                        imageData = image,
                        state = ImageColorState.IMAGE
                    ),
                )
            )
        }
    }

    fun updateBackgroundBase(base: BackgroundBase) {
        _scoreCardState.update { currentState ->
            currentState.copy(
                scoreCard = currentState.scoreCard.copy(
                    background = currentState.scoreCard.background.copy(
                        backgroundBase = base,
                        state = ImageColorState.BASEIMAGE
                    )
                )
            )
        }
    }

    fun updateShaderType(shaderType: ShaderType) {
        _scoreCardState.update { currentState ->
            currentState.copy(
                scoreCard = currentState.scoreCard.copy(
                    background = currentState.scoreCard.background.copy(
                        shaderType = shaderType,
                        state = ImageColorState.GRADIENT
                    )
                )
            )
        }
    }

    fun updateEffect(effect: Effect) {
        _scoreCardState.update { currentState ->
            currentState.copy(
                scoreCard = currentState.scoreCard.copy(
                    background = currentState.scoreCard.background.copy(effect = effect)
                )
            )
        }
    }

    fun updateOverLayImage(bitmap: Bitmap?) {
        _scoreCardState.update { currentState ->
            val updatedImage = bitmap ?: createEmptyBitmap()
            currentState.copy(
                overlayImage = bitmap,
                scoreCard = currentState.scoreCard.copy(
                    background = currentState.scoreCard.background.copy(overlayImage = updatedImage)
                )
            )
        }

        if (bitmap != null) {
            viewModelScope.launch {
                if (_scoreCardState.value.removeOverLayImageBackground) {
                    processImage(bitmap, viewModelScope) { processedBitmap ->
                        _scoreCardState.update { state ->
                            state.copy(
                                overlayImage = bitmap,
                                scoreCard = state.scoreCard.copy(
                                    background = state.scoreCard.background.copy(overlayImage = processedBitmap)
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateRemoveOverlayImageBackground(remove: Boolean) {
        val currentOverlay = _scoreCardState.value.overlayImage

        _scoreCardState.update { currentState ->
            if (currentOverlay == null) {
                currentState.copy(removeOverLayImageBackground = remove)
            } else {
                if (remove) {
                    viewModelScope.launch(Dispatchers.Default) {
                        processImage(currentOverlay, viewModelScope) { processedBitmap ->
                            _scoreCardState.update { state ->
                                state.copy(
                                    scoreCard = state.scoreCard.copy(
                                        background = state.scoreCard.background.copy(overlayImage = processedBitmap)
                                    ),
                                    removeOverLayImageBackground = true
                                )
                            }
                        }
                    }
                    currentState
                } else {
                    currentState.copy(
                        scoreCard = currentState.scoreCard.copy(
                            background = currentState.scoreCard.background.copy(overlayImage = currentOverlay)
                        ),
                        removeOverLayImageBackground = false
                    )
                }
            }
        }
    }
    fun toggleBlendMode() {
        _scoreCardState.update { currentState ->
            val currentBlendMode = currentState.scoreCard.background.imageBlendMode
            // Toggle between BlendMode.Color and BlendMode.Hue.
            val newBlendMode = if (currentBlendMode == ImageBlendMode.BLENDHUE) {
                ImageBlendMode.BLENDCOLOR
            } else {
                ImageBlendMode.BLENDHUE
            }

            // Return a new state with the updated blend mode.
            currentState.copy(
                scoreCard = currentState.scoreCard.copy(
                    background = currentState.scoreCard.background.copy(
                        imageBlendMode = newBlendMode
                    )
                )
            )
        }
    }

    fun updateScoreCardViewModel(action: ScoreCardViewModelActions){
        when(action){
            is ScoreCardViewModelActions.UpdateColor -> updateColor(action.color, action.colorIndex)
            is ScoreCardViewModelActions.UpdateOverlayImage -> updateOverLayImage(action.bitmap)
            is ScoreCardViewModelActions.UpdateRemoveBackground -> updateRemoveOverlayImageBackground(action.checked)
            is ScoreCardViewModelActions.UpdateTextColor -> updateTextColor(action.color)
            is ScoreCardViewModelActions.UpdateBackgroundImageBase -> updateBackgroundBase(action.baseImage)
            is ScoreCardViewModelActions.UpdateBackgroundImage -> updateBackgroundImage(action.bitmap)
            is ScoreCardViewModelActions.UpdateEffect -> updateEffect(action.effect)
            is ScoreCardViewModelActions.UpdateShaderType -> updateShaderType(action.shaderType)
            is ScoreCardViewModelActions.ChangeBlendMode -> toggleBlendMode()
        }
    }
}

sealed class ScoreCardViewModelActions{
    data class UpdateColor(val color: Color, val colorIndex: Int): ScoreCardViewModelActions()
    data class UpdateOverlayImage(val bitmap: Bitmap?): ScoreCardViewModelActions()
    data class UpdateRemoveBackground(val checked: Boolean) : ScoreCardViewModelActions()
    data class UpdateTextColor(val color: Color): ScoreCardViewModelActions()
    data class UpdateBackgroundImageBase(val baseImage: BackgroundBase): ScoreCardViewModelActions()
    data class UpdateBackgroundImage(val bitmap: Bitmap?): ScoreCardViewModelActions()
    data class UpdateEffect(val effect: Effect): ScoreCardViewModelActions()
    data class UpdateShaderType(val shaderType: ShaderType): ScoreCardViewModelActions()
    data object ChangeBlendMode: ScoreCardViewModelActions()
}

data class ScoreCardState(
    val scoreCard: ScoreCard = ScoreCard(),
    val overlayImage: Bitmap? = null,
    val removeOverLayImageBackground: Boolean = true,
)