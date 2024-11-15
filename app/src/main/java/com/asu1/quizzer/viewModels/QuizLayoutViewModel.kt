package com.asu1.quizzer.viewModels

import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.asu1.quizzer.data.ColorSchemeSerializer
import com.asu1.quizzer.data.QuizDataSerializer
import com.asu1.quizzer.data.QuizResult
import com.asu1.quizzer.data.SendQuizResult
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.data.sampleResult
import com.asu1.quizzer.data.toJson
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.quizzer.model.sampleQuiz1
import com.asu1.quizzer.model.sampleQuiz2
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.network.getErrorMessage
import com.asu1.quizzer.screens.quizlayout.borders
import com.asu1.quizzer.screens.quizlayout.colors
import com.asu1.quizzer.screens.quizlayout.fonts
import com.asu1.quizzer.screens.quizlayout.outlines
import com.asu1.quizzer.ui.theme.LightColorScheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.withErrorColor
import com.asu1.quizzer.util.withOnErrorColor
import com.asu1.quizzer.util.withOnPrimaryColor
import com.asu1.quizzer.util.withOnSecondaryColor
import com.asu1.quizzer.util.withOnTertiaryColor
import com.asu1.quizzer.util.withPrimaryColor
import com.asu1.quizzer.util.withSecondaryColor
import com.asu1.quizzer.util.withSurfaceColor
import com.asu1.quizzer.util.withTertiaryColor
import com.github.f4b6a3.uuid.UuidCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate
import java.util.Base64

@Serializable
data class QuizTheme(
    var backgroundImage: ImageColor = ImageColor(Color.White, byteArrayOf(), Color.White, ImageColorState.COLOR),
    var questionTextStyle: List<Int> = listOf(0, 0, 1, 0, 2),
    var bodyTextStyle: List<Int> = listOf(0, 0, 2, 1, 0),
    var answerTextStyle: List<Int> = listOf(0, 0, 0, 2, 0),
    @Serializable(with = ColorSchemeSerializer::class) var colorScheme: ColorScheme = LightColorScheme,
)

data class QuizData(
    var title: String = "",
    var image: ByteArray = byteArrayOf(),
    var description: String = "",
    var tags: Set<String> = emptySet(),
    var shuffleQuestions: Boolean = false,
    var creator: String = "GUEST",
    var uuid : String? = null,
)

enum class LayoutSteps(value: Int) {
    POLICY(0),
    TITLE(1),
    DESCRIPTION(2),
    TAGS(3),
    IMAGE(4),
    THEME(5),
    TEXTSTYLE(6),;

    operator fun minus(i: Int): LayoutSteps {
        return when (this) {
            POLICY -> POLICY
            TITLE -> POLICY
            DESCRIPTION -> TITLE
            TAGS -> DESCRIPTION
            IMAGE -> TAGS
            THEME -> IMAGE
            TEXTSTYLE -> THEME
        }
    }

    operator fun plus(i: Int): LayoutSteps {
        return when (this) {
            POLICY -> TITLE
            TITLE -> DESCRIPTION
            DESCRIPTION -> TAGS
            TAGS -> IMAGE
            IMAGE -> THEME
            THEME -> TEXTSTYLE
            TEXTSTYLE -> TEXTSTYLE
        }
    }
}

class QuizLayoutViewModel : ViewModel() {
    private val _viewModelState = MutableLiveData(ViewModelState.IDLE)
    val viewModelState: LiveData<ViewModelState> get() = _viewModelState

    private val _quizTheme = MutableStateFlow(QuizTheme())
    val quizTheme: StateFlow<QuizTheme> = _quizTheme.asStateFlow()

    private val _quizData = MutableStateFlow(QuizData())
    val quizData: StateFlow<QuizData> = _quizData.asStateFlow()

    private val _showToast = MutableLiveData<String?>()
    val showToast: LiveData<String?> get() = _showToast

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes: StateFlow<List<Quiz>> get() = _quizzes.asStateFlow()

    private val _policyAgreement = MutableLiveData(false)
    val policyAgreement: LiveData<Boolean> get() = _policyAgreement

    private val _step = MutableLiveData(LayoutSteps.POLICY)
    val step: LiveData<LayoutSteps> get() = _step

    private val _initIndex = MutableLiveData(0)
    val initIndex: LiveData<Int> get() = _initIndex

    private val _quizResult = MutableStateFlow<QuizResult?>(null)
    val quizResult: StateFlow<QuizResult?> = _quizResult.asStateFlow()

    private val textStyleManager = TextStyleManager()

    init {
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

    fun loadQuizResult(resultId: String, scoreCardViewModel: ScoreCardViewModel){
        _viewModelState.value = ViewModelState.LOADING
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getResult(resultId)
                if(response.isSuccessful){
                    val quizResult = response.body()!!
                    scoreCardViewModel.loadScoreCard(quizResult.scoreCard)
                    updateQuizResult(quizResult.quizResult)
                } else {
                    Logger().debug("Failed to load quiz result ${response.errorBody()?.string()}")
                    _viewModelState.value = ViewModelState.ERROR
                    _showToast.postValue("Failed to load quiz result")
                }

            } catch (e: Exception) {
                Logger().debug("Failed to load quiz result $e")
                _viewModelState.value = ViewModelState.ERROR
                _showToast.postValue("Failed to load quiz result")
            }

        }
    }

    fun resetQuizResult(){
        _quizResult.value = null
    }

    fun resetViewModelState(){
        _viewModelState.value = ViewModelState.IDLE
    }

    fun gradeQuiz(email: String, onDone: () -> Unit) {
        var totalScore = 0f
        var currentScore = 0f
        var corrections = quizzes.value!!.map { false }
        for(quiz in quizzes.value!!){
            if(quiz.gradeQuiz()){
                Logger().debug("Quiz ${quizzes.value!!.indexOf(quiz)} is correct")
                currentScore += quiz.point
                corrections = corrections.toMutableList().apply {
                    set(quizzes.value!!.indexOf(quiz), true)
                }
            }
            else{
                Logger().debug("Quiz ${quizzes.value!!.indexOf(quiz)} is wrong")
            }
            totalScore += quiz.point
        }
        val percentageScore = (currentScore / totalScore) * 100
        val result = SendQuizResult(
            email = email,
            quizUuid = quizData.value.uuid!!,
            score = percentageScore,
            correction = corrections,
        )
        viewModelScope.launch {
            try{
                val response = RetrofitInstance.api.submitQuiz(result)
                if(response.isSuccessful){
                    _viewModelState.value = ViewModelState.LOADING
                    onDone()
                    updateQuizResult(response.body()!!)
                } else {
                    Logger().debug("Failed to grade quiz ${response.errorBody()?.string()}")
                    _showToast.postValue("Failed to grade quiz")
                }
            }
            catch (e: Exception){
                Logger().debug("Failed to grade quiz $e")
                _showToast.postValue("Failed to grade quiz")
            }
        }
    }

    fun loadQuiz(quizId: String, scoreCardViewModel: ScoreCardViewModel, onDone: () -> Unit) {
        val thisViewModel = this
        viewModelScope.launch(Dispatchers.IO){
            try {
                Logger().debugFull("Get Quiz : $quizId")
                val response = RetrofitInstance.api.getQuizData(quizId)
                withContext(Dispatchers.IO) {
                    if (response.isSuccessful) {
                        scoreCardViewModel.loadScoreCard(response.body()!!.scoreCard)
                        withContext(Dispatchers.Main) {
                            thisViewModel.loadQuizData(
                                response.body()!!.quizData,
                                response.body()!!.quizTheme,
                                onDone
                            )
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string()?.let { getErrorMessage(it) }
                        Logger().debug("Failed to load quiz: $errorMessage")
                        _showToast.postValue("Failed to load quiz")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Logger().debug("Failed to load quiz: $e")
                    _showToast.postValue("Failed to load quiz")
                }
            }
        }
    }

    fun loadQuizData(quizData: QuizDataSerializer, quizTheme: QuizTheme, loadDone: () -> Unit) {
        _quizData.value = QuizData(
            title = quizData.title,
            image = Base64.getDecoder().decode(quizData.titleImage),
            description = quizData.description,
            tags = quizData.tags,
            shuffleQuestions = quizData.shuffleQuestions,
            creator = quizData.creator,
            uuid = quizData.uuid
        )
        val quiz = mutableListOf<Quiz>()
        for(quizJson in quizData.quizzes){
            val curQuiz = quizJson.toQuiz()
            quiz.add(curQuiz)
        }
        _quizzes.value = quiz
        _quizTheme.value = quizTheme
        _step.postValue(LayoutSteps.THEME)
        initTextStyleManager()
        loadDone()
    }

    fun initQuizLayout(email: String?, colorScheme: ColorScheme) {
        _quizData.value = _quizData.value.copy(creator = email ?: "GUEST")
        _quizTheme.value = _quizTheme.value.copy(colorScheme = colorScheme)
        _policyAgreement.value = false
        _initIndex.value = 0
        _step.value = LayoutSteps.POLICY
    }

    suspend fun tryUpload(navController: NavController, scoreCard: ScoreCard, onUpload: () -> Unit) {
        if(!validateQuizLayout()){
            navController.popBackStack()
            return
        }
        uploadQuizLayout(scoreCard, onUpload)
    }

    private suspend fun uploadQuizLayout(scoreCard: ScoreCard, onUpload: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsoned = toJson(scoreCard)
                val response = RetrofitInstance.api.addQuiz(jsoned)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _showToast.postValue("Quiz uploaded successfully")
                        onUpload()
                    } else {
                        val errorMessage = response.errorBody()?.string()?.let { getErrorMessage(it) }
                        Logger().debug("Failed to upload quiz: $errorMessage")
                        _showToast.postValue("Failed to upload quiz")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _showToast.postValue("Failed to upload quiz")
                }
            }
        }
    }

    suspend fun saveLocal(context: Context, scoreCard: ScoreCard) {
        val jsoned = Json.encodeToString(toJson(scoreCard))
        val fileName = "${quizData.value.uuid}_${quizData.value.creator}_quizSave.json"
        val file = File(context.filesDir, fileName)
        file.writeText(jsoned)
        _showToast.value = "${quizData.value.title} saved"
    }

    fun validateQuizLayout(): Boolean {
        if(_quizData.value.title.isEmpty()) {
            _showToast.value = "Title cannot be empty"
            return false
        }
        if(_quizzes.value!!.isEmpty()) {
            _showToast.value = "Quiz cannot be empty"
            return false
        }
        for(quiz in _quizzes.value!!){
            val check = quiz.validateQuiz()
            if(!check.second){
                _showToast.value = check.first
                updateInitIndex(_quizzes.value!!.indexOf(quiz))
                return false
            }
        }
        return true
    }

    fun updateInitIndex(index: Int) {
        if(index > quizzes.value!!.size){
            _initIndex.value = quizzes.value!!.size
            return
        }
        _initIndex.value = index
    }

    fun toastShown() {
        _showToast.value = null
    }

    fun sendToast(message: String){
        _showToast.value = message
    }

    fun updatePolicyAgreement(agreement: Boolean) {
        _policyAgreement.value = agreement
        updateStep(LayoutSteps.TITLE)
    }

    fun updateStep(step: LayoutSteps) {
        _step.value = step
    }

    fun resetQuizLayout() {
        _quizTheme.value = QuizTheme()
        _quizData.value = QuizData()
        _quizzes.value = emptyList()
        _viewModelState.value = ViewModelState.IDLE
        _policyAgreement.value = false
        _step.value = LayoutSteps.POLICY
    }

    fun setQuizTitle(title: String) {
        _quizData.value = _quizData.value.copy(title = title, uuid = null)
    }

    fun setQuizImage(image: ByteArray) {
        if(image.size > 80000){
            _showToast.value = "Image size too large"
            return
        }
        _quizData.value = _quizData.value.copy(image = image)
    }

    fun setQuizBodyImage(image: ByteArray){
        if(_quizzes.value.isEmpty()){
            return
        }
        _quizzes.update {
            it.toMutableList().apply {
                val firstQuiz = this[0]
                firstQuiz.bodyType = BodyType.IMAGE
                firstQuiz.bodyImage = image
                this[0] = firstQuiz
            }
        }
    }

    fun setQuizDescription(description: String) {
        _quizData.value = _quizData.value.copy(description = description)
    }

    fun addTag(tag: String) {
        val tags = _quizData.value.tags.toMutableSet()
        tags.add(tag)
        _quizData.value = _quizData.value.copy(tags = tags)
    }

    fun removeTag(tag: String) {
        val tags = _quizData.value.tags.toMutableSet()
        tags.remove(tag)
        _quizData.value = _quizData.value.copy(tags = tags)
    }

    fun updateTag(tag: String){
        val tags = _quizData.value.tags.toMutableSet()
        if(tags.contains(tag)){
            tags.remove(tag)
        } else {
            tags.add(tag)
        }
        _quizData.value = _quizData.value.copy(tags = tags)
    }

    fun setBackgroundImage(imageColor: ImageColor) {
        _quizTheme.value = _quizTheme.value.copy(backgroundImage = imageColor, colorScheme = _quizTheme.value.colorScheme.withSurfaceColor(imageColor.color))
    }

    fun setColorScheme(name: String, color: Color){
        val colorScheme = _quizTheme.value.colorScheme
        val updateColorScheme = when(name){
            "Primary Color" -> colorScheme.withPrimaryColor(color)
            "Secondary Color" -> colorScheme.withSecondaryColor(color)
            "Tertiary Color" -> colorScheme.withTertiaryColor(color)
            "onPrimary Color" -> colorScheme.withOnPrimaryColor(color)
            "onSecondary Color" -> colorScheme.withOnSecondaryColor(color)
            "onTertiary Color" -> colorScheme.withOnTertiaryColor(color)
            "Error Color" -> colorScheme.withErrorColor(color)
            "onError Color" -> colorScheme.withOnErrorColor(color)
            else -> colorScheme
        }
        _quizTheme.value = _quizTheme.value.copy(colorScheme = updateColorScheme)
    }

    fun setColorScheme(colorScheme: ColorScheme) {
        _quizTheme.value = _quizTheme.value.copy(colorScheme = colorScheme, backgroundImage = ImageColor(colorScheme.surface, _quizTheme.value.backgroundImage.imageData, _quizTheme.value.backgroundImage.color2, _quizTheme.value.backgroundImage.state))
    }

    fun updateTextStyle(targetSelector: Int, indexSelector: Int, isIncrease: Boolean) {
        when(targetSelector){
            0 -> updateQuestionTextStyle(indexSelector, isIncrease)
            1 -> updateBodyTextStyle(indexSelector, isIncrease)
            2 -> updateAnswerTextStyle(indexSelector, isIncrease)
        }
    }

    fun updateShuffleQuestions(shuffleQuestions: Boolean) {
        _quizData.value = _quizData.value.copy(shuffleQuestions = shuffleQuestions)
    }

    fun textStyleUpdater(textStyle: List<Int>, indexSelector: Int, isIncrease: Boolean): List<Int> {
        val size = when(indexSelector){
            0 -> fonts.size
            1 -> colors.size
            2 -> borders.size
            4 -> outlines.size
            else -> borders.size
        }
        return textStyle.toMutableList().apply {

            if(isIncrease){
                this[indexSelector] = (this[indexSelector] + 1)
            } else {
                this[indexSelector] = (this[indexSelector] - 1)
            }
            if(this[indexSelector] < 0) this[indexSelector] = size - 1
            if(this[indexSelector] >= size) this[indexSelector] = 0
        }
    }

    fun updateQuestionTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.value = _quizTheme.value.copy(questionTextStyle = textStyleUpdater(_quizTheme.value.questionTextStyle, indexSelector, isIncrease))
    }

    fun updateBodyTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.value = _quizTheme.value.copy(bodyTextStyle = textStyleUpdater(_quizTheme.value.bodyTextStyle, indexSelector, isIncrease))
    }

    fun updateAnswerTextStyle(indexSelector: Int, isIncrease: Boolean) {
        _quizTheme.value = _quizTheme.value.copy(answerTextStyle = textStyleUpdater(_quizTheme.value.answerTextStyle, indexSelector, isIncrease))
    }



    fun updateCreator(creator: String) {
        _quizData.value = _quizData.value.copy(creator = creator)
    }

    fun generateUUIDWithTitle() {
        if(_quizData.value.title.isEmpty()) return
        val name = "${_quizData.value.title}-${System.currentTimeMillis()}"
        if(_quizData.value.uuid.isNullOrEmpty()) {
            _quizData.value = _quizData.value.copy(uuid = UuidCreator.getNameBasedSha1(UuidCreator.NAMESPACE_URL, name).toString())
        }
    }

    fun addQuiz(quiz: Quiz, index: Int? = null) {
        if(index == null || index >= quizzes.value!!.size || index == -1){
            val quizzes = quizzes.value!!.toMutableList()
            quizzes.add(quiz)
            _quizzes.value = quizzes
            return
        } else{
            val quizzes = quizzes.value!!.toMutableList()
            quizzes.add(index, quiz)
            _quizzes.value = quizzes
            return
        }
    }

    fun updateQuiz1(quizIndex: Int, answerIndex: Int){
        val quiz = quizzes.value[quizIndex] as Quiz1
        quiz.toggleUserAnswer(answerIndex)
        _quizzes.update {
            it.toMutableList().apply {
                set(quizIndex, quiz)
            }
        }
    }

    fun updateQuiz2(quizIndex: Int, date: LocalDate){
        val quiz = quizzes.value[quizIndex] as Quiz2
        quiz.toggleUserAnswer(date)
        _quizzes.update {
            it.toMutableList().apply {
                set(quizIndex, quiz)
            }
        }
    }

    fun updateQuiz3(quizIndex: Int, from: Int, to: Int){
        val quiz = quizzes.value[quizIndex] as Quiz3
        quiz.swap(from, to)
        _quizzes.update {
            it.toMutableList().apply {
                set(quizIndex, quiz)
            }
        }
    }

    fun updateQuiz4(quizIndex: Int, from: Int, to: Int?){
        val quiz = quizzes.value[quizIndex] as Quiz4
        quiz.updateUserConnection(from, to)
        _quizzes.update {
            it.toMutableList().apply {
                set(quizIndex, quiz)
            }
        }
    }

    fun updateQuiz(quiz: Quiz, index: Int) {
        val quizzes = quizzes.value!!.toMutableList()
        quizzes[index] = quiz
        _quizzes.value = quizzes
    }

    fun removeQuizAt(index: Int) {
        if(index >= quizzes.value!!.size || index < 0){
            return
        }
        _quizzes.value = quizzes.value!!.toMutableList().apply {
            removeAt(index)
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuizLayoutViewModel) return false

        if (_quizTheme.value != other._quizTheme.value) return false
        if (_quizData.value != other._quizData.value) return false
        if (_quizzes.value != other._quizzes.value) return false

        return true
    }
    fun updateQuizResult(quizResult: QuizResult){
        _quizResult.value = quizResult
        _viewModelState.value = ViewModelState.IDLE
    }
}

fun createSampleQuizLayoutViewModel(): QuizLayoutViewModel {
    val viewModel = QuizLayoutViewModel()
    viewModel.addQuiz(sampleQuiz1, null)
    viewModel.addQuiz(sampleQuiz2, null)
    viewModel.setQuizTitle("Sample Quiz")
    viewModel.setQuizDescription("This is a sample quiz description.")
    viewModel.updateCreator("Sample Creator")
    viewModel.updateQuizResult(sampleResult)
    return viewModel
}