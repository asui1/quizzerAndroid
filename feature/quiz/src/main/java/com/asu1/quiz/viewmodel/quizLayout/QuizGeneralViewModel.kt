package com.asu1.quiz.viewmodel.quizLayout

import SnackBarManager
import ToastType
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.asu1.models.quiz.QuizData
import com.asu1.models.serializers.QuizDataSerializer
import com.asu1.resources.LayoutSteps
import com.asu1.resources.R
import com.asu1.utils.Logger
import com.asu1.utils.images.createEmptyBitmap
import com.github.f4b6a3.uuid.UuidCreator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Base64

class QuizGeneralViewModel : ViewModel() {
    private val _quizGeneralUIState = MutableStateFlow(QuizGeneralUiState())
    val quizGeneralUiState = _quizGeneralUIState.asStateFlow()


    fun loadQuizData(quizData: QuizDataSerializer) {
        val bitmap = decodeBase64ToBitmap(quizData.titleImage) ?: createEmptyBitmap()

        _quizGeneralUIState.update { currentState ->
            currentState.copy(
                quizData = quizData.toQuizData(bitmap),
                isPolicyAgreed = true,
                step = LayoutSteps.THEME
            )
        }
    }

    private fun QuizDataSerializer.toQuizData(image: Bitmap): QuizData {
        return QuizData(
            title = title,
            image = image,
            description = description,
            tags = tags,
            shuffleQuestions = shuffleQuestions,
            creator = creator,
            uuid = uuid
        )
    }

    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.getDecoder().decode(base64String)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            Logger.debug("Decoding Base64 to Image failed: $e")
            null
        }
    }

    fun initQuizGeneral(email: String?) {
        _quizGeneralUIState.update {
            it.copy(quizData = QuizData(creator = email ?: "GUEST"))
        }
    }

    fun updatePolicyAgreement(agreement: Boolean) {
        _quizGeneralUIState.update {
            it.copy(isPolicyAgreed = agreement, step = LayoutSteps.TITLE)
        }
    }

    fun updateStep(step: LayoutSteps) {
        _quizGeneralUIState.update {
            it.copy(step = step)
        }
    }

    fun resetQuizGeneral(email: String? = "GUEST") {
        Logger.debug("Set Email to : $email")
        _quizGeneralUIState.update{
            QuizGeneralUiState().copy(quizData = QuizData(creator = email ?: "GUEST"))
        }
    }

    fun updateQuizTitle(title: String) {
        _quizGeneralUIState.update {
            it.copy(quizData = it.quizData.copy(title = title))
        }
    }

    fun updateQuizImage(image: Bitmap?) {
        _quizGeneralUIState.update { currentState ->
            if (image == null) {
                return@update currentState.copy(quizData = currentState.quizData.copy(image = createEmptyBitmap()))
            }

            if (image.byteCount > 1024 * 400) { // 80000 * 4
                SnackBarManager.showSnackBar(R.string.image_size_too_large, ToastType.ERROR)
                return@update currentState
            }

            currentState.copy(quizData = currentState.quizData.copy(image = image))
        }
    }

    fun updateQuizDescription(description: String) {
        _quizGeneralUIState.update {
            it.copy(quizData = it.quizData.copy(description = description))
        }
    }

    fun toggleTag(tag: String) {
        _quizGeneralUIState.update { currentState ->
            val updatedTags = currentState.quizData.tags.toMutableSet().apply {
                if (!this.add(tag)) this.remove(
                    tag
                )
            }
            currentState.copy(quizData = currentState.quizData.copy(tags = updatedTags))
        }
    }

    fun getUuidFromTitle(): String {
        _quizGeneralUIState.update { currentState ->
            val quizData = currentState.quizData
            if (quizData.uuid.isNullOrEmpty()) {
                val name = "${quizData.title}-${System.currentTimeMillis()}"
                val newUuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, name).toString()
                return@update currentState.copy(quizData = quizData.copy(uuid = newUuid))
            }
            currentState
        }
        return _quizGeneralUIState.value.quizData.uuid!!
    }

    fun updateQuizGeneralViewModel(action: QuizGeneralActions){
        when(action){
            is QuizGeneralActions.UpdateStep -> updateStep(action.step)
            is QuizGeneralActions.UpdatePolicyAgreement -> updatePolicyAgreement(true)
            is QuizGeneralActions.ToggleQuizTag -> toggleTag(action.tag)
            is QuizGeneralActions.UpdateQuizDescription -> updateQuizDescription(action.description)
            is QuizGeneralActions.UpdateQuizTitle -> updateQuizTitle(action.title)
            is QuizGeneralActions.UpdateQuizImage -> updateQuizImage(action.bitmap)
        }
    }
}

sealed class QuizGeneralActions{
    data class UpdateStep(val step: LayoutSteps): QuizGeneralActions()
    data object UpdatePolicyAgreement: QuizGeneralActions()
    data class ToggleQuizTag(val tag: String): QuizGeneralActions()
    data class UpdateQuizDescription(val description: String): QuizGeneralActions()
    data class UpdateQuizTitle(val title: String): QuizGeneralActions()
    data class UpdateQuizImage(val bitmap: Bitmap?): QuizGeneralActions()
}

data class QuizGeneralUiState(
    val quizData: QuizData = QuizData(),
    val isPolicyAgreed: Boolean = false,
    val step: LayoutSteps = LayoutSteps.POLICY
)