package com.asu1.quiz.viewmodel.quizLayout

import SnackBarManager
import ToastType
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.asu1.models.quiz.GetQuizResult
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.QuizResult
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quiz.SendQuizResult
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.serializers.QuizDataSerializer
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.network.RetrofitInstance
import com.asu1.network.getErrorMessage
import com.asu1.quiz.ui.TextStyleManager
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates
import com.asu1.resources.GenerateWith
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.resources.contrastSize
import com.asu1.resources.paletteSize
import com.asu1.utils.Logger
import com.asu1.utils.calculateSeedColor
import com.asu1.utils.randomDynamicColorScheme
import com.asu1.utils.toScheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64
import kotlin.collections.map
import kotlin.collections.plus
import kotlin.compareTo

class QuizCoordinatorViewModel : ViewModel() {
    private var _quizViewModelState = MutableStateFlow(ViewModelState.IDLE)
    val quizViewModelState: StateFlow<ViewModelState> = _quizViewModelState

    private val _quizUIState = MutableStateFlow(QuizCoordinatorState())
    val quizUIState: StateFlow<QuizCoordinatorState> = _quizUIState

    private lateinit var quizGeneralViewModel: QuizGeneralViewModel
    private lateinit var quizThemeViewModel: QuizThemeViewModel
    private lateinit var quizContentViewModel: QuizContentViewModel
    private lateinit var quizResultViewModel: QuizResultViewModel
    private lateinit var scoreCardViewModel: ScoreCardViewModel

    private val _titleImageColors = MutableStateFlow<List<Color>>(emptyList())

    fun getQuestions(): PersistentList<String>{
        return quizContentViewModel.quizContentState.value.quizzes.map {
            it.question
        }.toPersistentList()
    }

    fun resetQuizData(email: String?){
        quizGeneralViewModel.resetQuizGeneral(email)
        quizThemeViewModel.resetQuizTheme()
        quizContentViewModel.reset()
        quizResultViewModel.resetQuizResult()
        scoreCardViewModel.resetScoreCard()
    }

    fun updateTitleImageColors(image: Bitmap){
        if(image.width < 3){
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

    fun setViewModels(
        quizGeneral: QuizGeneralViewModel,
        quizTheme: QuizThemeViewModel,
        quizContent: QuizContentViewModel,
        quizResult: QuizResultViewModel,
        scoreCard: ScoreCardViewModel
    ) {
        this.quizGeneralViewModel = quizGeneral
        this.quizThemeViewModel = quizTheme
        this.quizContentViewModel = quizContent
        this.quizResultViewModel = quizResult
        this.scoreCardViewModel = scoreCard

        initializeStateFlows()
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
                        val currentScheme = quizThemeViewModel.quizTheme.value.colorScheme
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
                quizThemeViewModel.updateColorScheme(newColorScheme)
            }
        }
    }

    private fun initializeStateFlows() {
        viewModelScope.launch {
            combine(
                quizThemeViewModel.quizThemeViewModelState,
                quizContentViewModel.quizContentViewModelState,
                quizResultViewModel.quizResultViewModelState,
                scoreCardViewModel.scoreCardViewModelState
            ) { states ->
                when {
                    states.any { it == ViewModelState.ERROR } -> ViewModelState.ERROR
                    states.any { it == ViewModelState.LOADING } -> ViewModelState.LOADING
                    states.any { it == ViewModelState.UPLOADING } -> ViewModelState.UPLOADING
                    states.any { it == ViewModelState.GRADING } -> ViewModelState.GRADING
                    states.any { it == ViewModelState.SUCCESS } -> ViewModelState.SUCCESS
                    states.all { it == ViewModelState.IDLE } -> ViewModelState.IDLE
                    else -> ViewModelState.LOADING
                }
            }.collect { newState ->
                _quizViewModelState.value = newState
            }
        }

        viewModelScope.launch {
            combine(
                quizGeneralViewModel.quizGeneralUiState,
                quizThemeViewModel.quizTheme,
                quizContentViewModel.quizContentState,
                quizResultViewModel.quizResult,
                scoreCardViewModel.scoreCardState
            ) { quizGeneral, quizTheme, quizContentState, quizResult, scoreCardState ->
                QuizCoordinatorState(
                    quizGeneralUiState = quizGeneral,
                    quizTheme = quizTheme,
                    quizContentState = quizContentState,
                    quizResult = quizResult,
                    scoreCardState = scoreCardState
                )
            }.collect { newState ->
                _quizUIState.value = newState
            }
        }

        viewModelScope.launch {
            quizUIState
                .map { it.quizGeneralUiState.quizData.image }
                .distinctUntilChanged()
                .collectLatest { image ->
                    updateTitleImageColors(image)
                }
        }
    }

    fun loadQuizResult(resultId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getResult(resultId)
                if (!response.isSuccessful) throw Exception("Response Failed")
                val quizResult = response.body() ?: throw Exception("Quiz Result is null")
                processQuizResult(quizResult)
            } catch (e: Exception) {
                handleFailure("Failed to load quiz result: ${e.message}")
            }
        }
    }

    fun getTextStyleManager(): TextStyleManager{
        return quizThemeViewModel.getTextStyleManager()
    }

    private suspend fun processQuizResult(quizResult: GetQuizResult) {
        coroutineScope {
            val loadScoreCardDeferred = async { scoreCardViewModel.loadScoreCard(quizResult.scoreCard) }
            val updateQuizResultDeferred = async { quizResultViewModel.updateQuizResult(quizResult.quizResult) }

            loadScoreCardDeferred.await()
            updateQuizResultDeferred.await()
        }
        _quizViewModelState.value = ViewModelState.IDLE
    }

    fun loadQuiz(quizData: QuizDataSerializer, quizTheme: QuizTheme, scoreCard: ScoreCard){
        _quizViewModelState.update {
            ViewModelState.LOADING
        }
        viewModelScope.launch {
            try{
                quizGeneralViewModel.loadQuizData(quizData)
                quizThemeViewModel.loadQuizTheme(quizTheme)
                scoreCardViewModel.loadScoreCard(scoreCard)
                _quizViewModelState.update {
                    ViewModelState.IDLE
                }
            } catch (e: Exception){
                handleFailure("Failed to load Local Quiz $e")
            }
        }
    }

    fun loadQuiz(quizId: String) {
        _quizViewModelState.value = ViewModelState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getQuizData(quizId)

                if (!response.isSuccessful) throw Exception("Response Failed")
                val quizSerialize = response.body() ?: Exception("Response is null")
                processQuizData(quizSerialize as QuizLayoutSerializer)
                _quizViewModelState.update {
                    ViewModelState.IDLE
                }
            } catch (e: Exception) {
                handleFailure("Failed to load quiz: ${e.message}")
            }
        }
    }

    private suspend fun processQuizData(quizSerialized: QuizLayoutSerializer) {
        coroutineScope {
            try{
                val loadQuizGeneralDeferred = async { quizGeneralViewModel.loadQuizData(quizSerialized.quizData) }
                val loadQuizThemeDeferred = async { quizThemeViewModel.loadQuizTheme(quizSerialized.quizTheme) }
                val loadQuizDataDeferred = async { quizContentViewModel.loadQuizContents(quizSerialized.quizData.quizzes) }
                val loadScoreCardDeferred = async { scoreCardViewModel.loadScoreCard(quizSerialized.scoreCard) }

                // Await all deferred tasks
                loadQuizGeneralDeferred.await()
                loadQuizThemeDeferred.await()
                loadQuizDataDeferred.await()
                loadScoreCardDeferred.await()
                _quizViewModelState.value = ViewModelState.IDLE
            } catch (e: Exception){
                _quizViewModelState.update { ViewModelState.ERROR }
                Logger.debug("Quiz Data Process Fail: $e")
                SnackBarManager.showSnackBar(R.string.failed_to_load_quiz, ToastType.ERROR)
            }
        }
    }

    fun tryUpload(navController: NavController, onUpload: () -> Unit) {
        _quizViewModelState.value = ViewModelState.UPLOADING
        if(!quizContentViewModel.validateQuiz()){
            _quizViewModelState.value = ViewModelState.IDLE
            navController.popBackStack()
            return
        }
        uploadQuizLayout(onUpload)
    }

    private fun uploadQuizLayout(onUpload: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonQuizData = toJson()
                val response = RetrofitInstance.api.addQuiz(jsonQuizData)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        SnackBarManager.showSnackBar(R.string.quiz_uploaded_successfully, ToastType.SUCCESS)
                        onUpload()
                        _quizViewModelState.value = ViewModelState.IDLE
                    } else {
                        _quizViewModelState.value = ViewModelState.IDLE
                        val errorMessage = response.errorBody()?.string()?.let {
                            getErrorMessage(
                                it
                            )
                        }
                        Logger.debug("Failed to upload quiz from server $errorMessage")
                        SnackBarManager.showSnackBar(R.string.failed_to_upload_quiz, ToastType.ERROR)
                    }
                }
            } catch (e: Exception) {
                Logger.debug("Failed to upload quiz exception ${e.message}")
                _quizViewModelState.value = ViewModelState.IDLE
                SnackBarManager.showSnackBar(R.string.failed_to_upload_quiz, ToastType.ERROR)
            }
        }
    }
    fun saveLocal(context: Context) {
        _quizViewModelState.value = ViewModelState.LOADING
        val quizJsonData = Json.encodeToString(toJson())
        val fileName = "${quizGeneralViewModel.quizGeneralUiState.value.quizData.uuid}_${quizGeneralViewModel.quizGeneralUiState.value.quizData.creator}_quizSave.json"
        Logger.debug("Save File: $fileName")
        val file = File(context.filesDir, fileName)
        file.writeText(quizJsonData)
        _quizViewModelState.value = ViewModelState.IDLE
        SnackBarManager.showSnackBar(R.string.save_success, ToastType.SUCCESS)
    }

    private fun handleFailure(message: String) {
        Logger.debug(message)
        _quizViewModelState.update{ ViewModelState.ERROR }
        SnackBarManager.showSnackBar(R.string.failed_to_load_quiz, ToastType.ERROR)
    }

    fun toJson(): QuizLayoutSerializer{
        val localQuizData = quizDataToJson()
        val scoreCardCopy = scoreCardViewModel.scoreCardState.value.scoreCard.copy(quizUuid = localQuizData.uuid)
        val quizLayoutSerializer = QuizLayoutSerializer(
            quizData = localQuizData,
            quizTheme = quizThemeViewModel.quizTheme.value,
            scoreCard = scoreCardCopy
        )
        return quizLayoutSerializer
    }

    fun quizDataToJson(): QuizDataSerializer{
        val quizData = quizGeneralViewModel.quizGeneralUiState.value.quizData
        val stream = ByteArrayOutputStream()
        quizData.image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val titleImage = Base64.getEncoder().encodeToString(byteArray)
        val quizDataSerializer = QuizDataSerializer(
            title = quizData.title,
            creator = quizData.creator,
            titleImage = titleImage,
            uuid = quizGeneralViewModel.getUuidFromTitle(),
            tags = quizData.tags,
            quizzes = quizContentViewModel.quizContentState.value.quizzes.map {
                it.changeToJson()
            },
            description = quizData.description
        )
        return quizDataSerializer
    }
    fun gradeQuiz(email: String, onDone: () -> Unit) {
        val (percentageScore, corrections) = quizContentViewModel.gradeQuiz()
        val result = SendQuizResult(
            email = email,
            quizUuid = quizGeneralViewModel.quizGeneralUiState.value.quizData.uuid!!,
            score = percentageScore,
            correction = corrections,
        )

        viewModelScope.launch {
            try{
                val response = RetrofitInstance.api.submitQuiz(result)
                if(!response.isSuccessful) throw Exception("Response Failed")

                val quizResult = response.body() ?: throw Exception("Response is null")
                onDone()
                quizResultViewModel.updateQuizResult(quizResult)

            } catch (e: Exception) {
                SnackBarManager.showSnackBar(R.string.failed_to_grade_quiz, ToastType.ERROR)
                Logger.debug("Quiz result response was unsuccessful or null, $e")
            }
        }
    }
    fun updateQuizCoordinator(action: QuizCoordinatorActions){
        when(action){
            is QuizCoordinatorActions.UpdateQuizAnswer -> {
                quizContentViewModel.updateQuizAnswer(action.index, action.update)
            }
            is QuizCoordinatorActions.RemoveQuizAt -> quizContentViewModel.removeQuizAt(action.index)
            is QuizCoordinatorActions.UpdateQuizAt -> quizContentViewModel.updateQuiz(action.quiz, action.index)
            is QuizCoordinatorActions.AddQuizAt -> quizContentViewModel.addQuiz(action.quiz, action.index)
            is QuizCoordinatorActions.ResetQuizResult ->{
                scoreCardViewModel.resetScoreCard()
                quizResultViewModel.resetQuizResult()
            }
            is QuizCoordinatorActions.ResetQuiz -> {
                quizGeneralViewModel.resetQuizGeneral("2")
                quizThemeViewModel.resetQuizTheme()
                scoreCardViewModel.resetScoreCard()
            }
            is QuizCoordinatorActions.UpdateScoreCard -> scoreCardViewModel.updateScoreCardViewModel(action = action.scoreCardAction)
            is QuizCoordinatorActions.UpdateQuizGeneral -> quizGeneralViewModel.updateQuizGeneralViewModel(action.quizGeneralAction)
            is QuizCoordinatorActions.UpdateQuizTheme -> quizThemeViewModel.updateQuizTheme(action.quizThemeAction)
            is QuizCoordinatorActions.GenerateColorScheme -> generateColorScheme(
                action.generateWith, action.palette, action.contrast, action.isDark
            )
        }
    }
}

sealed class QuizCoordinatorActions{
    data class UpdateQuizGeneral(val quizGeneralAction: QuizGeneralActions): QuizCoordinatorActions()
    data class UpdateQuizTheme(val quizThemeAction: QuizThemeActions): QuizCoordinatorActions()
    data class UpdateScoreCard(val scoreCardAction: ScoreCardViewModelActions) : QuizCoordinatorActions()
    data class UpdateQuizAnswer(val index: Int, val update: QuizUserUpdates) : QuizCoordinatorActions()
    data class RemoveQuizAt(val index: Int) : QuizCoordinatorActions()
    data class UpdateQuizAt(val quiz: Quiz<*>, val index: Int) : QuizCoordinatorActions()
    data class AddQuizAt(val quiz: Quiz<*>, val index: Int) : QuizCoordinatorActions()
    data object ResetQuizResult: QuizCoordinatorActions()
    data object ResetQuiz: QuizCoordinatorActions()
    data class GenerateColorScheme(val generateWith: GenerateWith, val palette: Int, val contrast: Int, val isDark: Boolean): QuizCoordinatorActions()
}

data class QuizCoordinatorState(
    val quizGeneralUiState: QuizGeneralUiState = QuizGeneralUiState(),
    val quizTheme: QuizTheme = QuizTheme(),
    val quizContentState: QuizContentState = QuizContentState(),
    val quizResult: QuizResult? = null,
    val scoreCardState: ScoreCardState = ScoreCardState(),
)