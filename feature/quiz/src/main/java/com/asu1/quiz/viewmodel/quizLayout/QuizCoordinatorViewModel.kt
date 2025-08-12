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
import com.asu1.colormodel.ContrastLevel
import com.asu1.colormodel.PaletteLevel
import com.asu1.models.quiz.GetQuizResult
import com.asu1.models.quiz.QuizResult
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quiz.SendQuizResult
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.serializers.QuizDataSerializer
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.network.QuizApi
import com.asu1.network.requireSuccess
import com.asu1.network.runApi
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates
import com.asu1.resources.GenerateWith
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.utils.Logger
import com.asu1.utils.calculateSeedColor
import com.asu1.utils.randomDynamicColorScheme
import com.asu1.utils.toScheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.Base64
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class QuizCoordinatorViewModel @Inject constructor(
    private val quizApi: QuizApi
) : ViewModel() {
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
        paletteLevel: PaletteLevel,
        contrastLevel: ContrastLevel,
        isDark: Boolean,
    ) {
        viewModelScope.launch {
                val newColorScheme = when (base) {
                    GenerateWith.TITLE_IMAGE -> {
                        if (_titleImageColors.value.isEmpty()) return@launch
                        if (paletteLevel.palette == null) {
                            toScheme(
                                primary = _titleImageColors.value[0],
                                secondary = _titleImageColors.value[1],
                                tertiary = _titleImageColors.value[2],
                                isLight = !isDark
                            )
                        } else {
                            randomDynamicColorScheme(_titleImageColors.value[0],
                                paletteLevel.palette!!, contrastLevel.contrast, isDark)
                        }
                    }

                    GenerateWith.COLOR -> {
                        val currentScheme = quizThemeViewModel.quizTheme.value.colorScheme
                        if (paletteLevel.palette == null) {
                            toScheme(
                                primary = currentScheme.primary,
                                secondary = currentScheme.secondary,
                                tertiary = currentScheme.tertiary,
                                isLight = !isDark
                            )
                        } else {
                            randomDynamicColorScheme(currentScheme.primary,
                                paletteLevel.palette!!, contrastLevel.contrast, isDark)
                        }
                    }
                }
                quizThemeViewModel.updateColorScheme(newColorScheme)
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

    fun loadQuizResult(resultId: String) {
        viewModelScope.launch {
            val result = runApi {
                withContext(Dispatchers.IO) {
                    quizApi
                        .getResult(resultId)
                        .requireSuccess() // throws HttpException or NoSuchElementException
                }
            }

            result.onSuccess { quizResult ->
                processQuizResult(quizResult)
            }.onFailure { e ->
                when (e) {
                    is CancellationException      -> throw e
                    is HttpException              -> handleFailure("HTTP ${e.code()} – ${e.message()}")
                    is IOException                -> handleFailure("Network error – ${e.message}")
                    is NoSuchElementException     -> handleFailure(e.message ?: "No quiz result found")
                    is SerializationException     -> handleFailure("Invalid quiz result format ${e.message}")
                    else                          -> handleFailure("Unexpected error: ${e.message}")
                }
            }
        }
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

    fun loadQuiz(
        quizData: QuizDataSerializer,
        quizTheme: QuizTheme,
        scoreCard: ScoreCard
    ) {
        _quizViewModelState.value = ViewModelState.LOADING

        viewModelScope.launch {
            val result = runApi {
                // If these do CPU/disk work, keep them off Main
                withContext(Dispatchers.Default) {
                    quizGeneralViewModel.loadQuizData(quizData)
                    quizThemeViewModel.loadQuizTheme(quizTheme)
                    scoreCardViewModel.loadScoreCard(scoreCard)
                }
            }

            result.onSuccess {
                _quizViewModelState.value = ViewModelState.IDLE
            }.onFailure { e ->
                // runApi already rethrows CancellationException, so no need to handle it here
                handleFailure(e.message ?: "Unexpected error while loading quiz.")
            }
        }
    }

    fun loadQuiz(quizId: String) {
        _quizViewModelState.value = ViewModelState.LOADING

        viewModelScope.launch {
            val result = runApi {
                withContext(Dispatchers.IO) {
                    quizApi
                        .getQuizData(quizId)
                        .requireSuccess()   // throws HttpException / NoSuchElementException
                }
            }

            result.onSuccess { body ->
                processQuizData(body)       // body is QuizLayoutSerializer
                _quizViewModelState.value = ViewModelState.IDLE
            }.onFailure { e ->
                // NOTE: your current runApi wraps errors into RuntimeException(NetworkError.toString()).
                // So we only have a message here. If you want typed handling, return NetworkError instead.
                handleFailure(e.message ?: "Unexpected error while loading quiz.")
            }
        }
    }

    private suspend fun processQuizData(quizSerialized: QuizLayoutSerializer) {
        val result = runApi {
            supervisorScope {
                awaitAll(
                    async { quizGeneralViewModel.loadQuizData(quizSerialized.quizData) },
                    async { quizThemeViewModel.loadQuizTheme(quizSerialized.quizTheme) },
                    async { quizContentViewModel.loadQuizContents(quizSerialized.quizData.quizzes) },
                    async { scoreCardViewModel.loadScoreCard(quizSerialized.scoreCard) },
                )
            }
        }

        result.onSuccess {
            _quizViewModelState.value = ViewModelState.IDLE
        }.onFailure { e ->
            // runApi already rethrows CancellationException, so it won't reach here.
            _quizViewModelState.value = ViewModelState.ERROR
            Logger.debug("Quiz data process failed: ${e.message}")
            SnackBarManager.showSnackBar(R.string.failed_to_load_quiz, ToastType.ERROR)
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
        _quizViewModelState.value = ViewModelState.LOADING

        viewModelScope.launch {
            val result = runApi {
                val json = toJson()
                quizApi.addQuiz(json).requireSuccess()
            }

            result.onSuccess {
                SnackBarManager.showSnackBar(R.string.quiz_uploaded_successfully, ToastType.SUCCESS)
                onUpload()
                _quizViewModelState.value = ViewModelState.IDLE
            }.onFailure { err ->
                Logger.debug("Upload failed: ${err.message}")
                _quizViewModelState.value = ViewModelState.IDLE
                SnackBarManager.showSnackBar(R.string.failed_to_upload_quiz, ToastType.ERROR)
            }
        }
    }

    fun saveLocal(context: Context) {
        _quizViewModelState.value = ViewModelState.LOADING
        val quizJsonData = Json.encodeToString(toJson())
        val fileName = "${quizGeneralViewModel.quizGeneralUiState.value.quizData.uuid}_" +
                "${quizGeneralViewModel.quizGeneralUiState.value.quizData.creator}_quizSave.json"
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
            quizzes = quizContentViewModel.quizContentState.value.quizzes,
            description = quizData.description
        )
        return quizDataSerializer
    }
    fun gradeQuiz(email: String, onDone: () -> Unit) {
        val (correctCount, corrections) = quizContentViewModel.gradeQuiz()
        val uuid = quizGeneralViewModel.quizGeneralUiState.value.quizData.uuid
        if (uuid == null) {
            Logger.debug("gradeQuiz: missing quiz UUID")
            SnackBarManager.showSnackBar(R.string.failed_to_grade_quiz, ToastType.ERROR)
            return
        }

        val payload = SendQuizResult(
            email = email,
            quizUuid = uuid,
            correctProb = correctCount,
            correction = corrections
        )

        viewModelScope.launch {
            val result = runApi {
                withContext(Dispatchers.IO) {
                    quizApi.submitQuiz(payload).requireSuccess()
                }
            }

            result.onSuccess { quizResult ->
                quizResultViewModel.updateQuizResult(quizResult)
                onDone()
            }.onFailure { e ->
                SnackBarManager.showSnackBar(R.string.failed_to_grade_quiz, ToastType.ERROR)
                Logger.debug("gradeQuiz failed: ${e.message}")
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
            is QuizCoordinatorActions.UpdateScoreCard ->
                scoreCardViewModel.updateScoreCardViewModel(action = action.scoreCardAction)
            is QuizCoordinatorActions.UpdateQuizGeneral ->
                quizGeneralViewModel.updateQuizGeneralViewModel(action.quizGeneralAction)
            is QuizCoordinatorActions.UpdateQuizTheme ->
                quizThemeViewModel.updateQuizTheme(action.quizThemeAction)
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
    data class UpdateQuizAt(val quiz: Quiz, val index: Int) : QuizCoordinatorActions()
    data class AddQuizAt(val quiz: Quiz, val index: Int) : QuizCoordinatorActions()
    data object ResetQuizResult: QuizCoordinatorActions()
    data object ResetQuiz: QuizCoordinatorActions()
    data class GenerateColorScheme(
        val generateWith: GenerateWith, val palette: PaletteLevel,
        val contrast: ContrastLevel, val isDark: Boolean): QuizCoordinatorActions()
}

data class QuizCoordinatorState(
    val quizGeneralUiState: QuizGeneralUiState = QuizGeneralUiState(),
    val quizTheme: QuizTheme = QuizTheme(),
    val quizContentState: QuizContentState = QuizContentState(),
    val quizResult: QuizResult? = null,
    val scoreCardState: ScoreCardState = ScoreCardState(),
)
