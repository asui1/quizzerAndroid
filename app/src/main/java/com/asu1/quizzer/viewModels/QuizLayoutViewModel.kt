package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.Quiz3
import com.asu1.models.quiz.Quiz4
import com.asu1.models.sampleQuiz1
import com.asu1.models.sampleQuiz2
import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.quizzer.R
import com.asu1.quizzer.data.ColorSchemeSerializer
import com.asu1.quizzer.data.QuizDataSerializer
import com.asu1.quizzer.data.QuizResult
import com.asu1.quizzer.data.SendQuizResult
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.data.sampleResult
import com.asu1.quizzer.data.toJson
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.model.ShaderType
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.network.getErrorMessage
import com.asu1.quizzer.screens.quizlayout.randomDynamicColorScheme
import com.asu1.quizzer.ui.theme.LightColorScheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.byteArrayToImageBitmap
import com.asu1.quizzer.util.calculateSeedColor
import com.asu1.quizzer.util.constants.ColorList
import com.asu1.quizzer.util.constants.GenerateWith
import com.asu1.quizzer.util.constants.borders
import com.asu1.quizzer.util.constants.colors
import com.asu1.quizzer.util.constants.contrastSize
import com.asu1.quizzer.util.constants.fonts
import com.asu1.quizzer.util.constants.outlines
import com.asu1.quizzer.util.constants.paletteSize
import com.asu1.quizzer.util.toScheme
import com.asu1.quizzer.util.withPrimaryColor
import com.asu1.quizzer.util.withSecondaryColor
import com.asu1.quizzer.util.withTertiaryColor
import com.github.f4b6a3.uuid.UuidCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
    var backgroundImage: ImageColor = ImageColor(
        imageData = byteArrayOf(),
        color = Color.White,
        color2 = Color.White,
        colorGradient = Color.White,
        state = ImageColorState.COLOR
    ),
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

enum class LayoutSteps(val value: Int, val stringResourceId: Int) {
    POLICY(0, R.string.agree_policy),
    TITLE(1, R.string.set_quiz_title),
    DESCRIPTION(2, R.string.set_quiz_description),
    TAGS(3, R.string.set_quiz_tags),
    IMAGE(4, R.string.set_quiz_image),
    THEME(5, R.string.set_color_setting),
    TEXTSTYLE(6, R.string.set_text_setting),;

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

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes: StateFlow<List<Quiz>> get() = _quizzes.asStateFlow()

    private val _visibleQuizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val visibleQuizzes: StateFlow<List<Quiz>> = _visibleQuizzes

    private val pageSize = 4

    private val _policyAgreement = MutableLiveData(false)
    val policyAgreement: LiveData<Boolean> get() = _policyAgreement

    private val _step = MutableLiveData(LayoutSteps.POLICY)
    val step: LiveData<LayoutSteps> get() = _step

    private val _initIndex = MutableLiveData(0)
    val initIndex: LiveData<Int> get() = _initIndex

    private val _quizResult = MutableStateFlow<QuizResult?>(null)
    val quizResult: StateFlow<QuizResult?> = _quizResult.asStateFlow()

    private val textStyleManager = TextStyleManager()

    private val _titleImageColors = MutableStateFlow<List<Color>>(emptyList())
    val titleImageColors: StateFlow<List<Color>> get() = _titleImageColors.asStateFlow()

    fun initialize(){
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

    fun resetVisibleQuizzes(){
        _visibleQuizzes.update {
            it.toMutableList().apply {
                clear()
            }
        }
    }

    fun loadMoreQuizzes(size: Int){
        viewModelScope.launch {
            val start = _visibleQuizzes.value.size
            val end = minOf(start + size, _quizzes.value.size)
            if (start < end) {
                _visibleQuizzes.update {
                    it.toMutableList().apply {
                        addAll(_quizzes.value.subList(start, end))
                    }
                }
            }
        }
    }

    fun loadMoreQuizzes() {
        loadMoreQuizzes(pageSize)
    }

    fun getTextStyleManager(): TextStyleManager {
        return textStyleManager
    }

    fun loadQuizResult(resultId: String, scoreCardViewModel: ScoreCardViewModel){
        _viewModelState.value = ViewModelState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getResult(resultId)
                if (response.isSuccessful) {
                    val quizResult = response.body()!!
                    coroutineScope {
                        val loadScoreCardDeferred = async { scoreCardViewModel.loadScoreCard(quizResult.scoreCard) }
                        val updateQuizResultDeferred = async { updateQuizResult(quizResult.quizResult) }
                        loadScoreCardDeferred.await()
                        updateQuizResultDeferred.await()
                        _viewModelState.postValue(ViewModelState.IDLE)
                    }
                } else {
                    Logger.debug("Failed to load quiz result ${response.errorBody()?.string()}")
                    _viewModelState.postValue(ViewModelState.ERROR)
                    ToastManager.showToast(R.string.failed_to_load_quiz_result, ToastType.ERROR)
                }
            } catch (e: Exception) {
                _viewModelState.postValue(ViewModelState.ERROR)
                ToastManager.showToast(R.string.failed_to_load_quiz_result, ToastType.ERROR)
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
        var corrections = quizzes.value.map { false }
        for(quiz in quizzes.value){
            if(quiz.gradeQuiz()){
                currentScore += quiz.point
                corrections = corrections.toMutableList().apply {
                    set(quizzes.value.indexOf(quiz), true)
                }
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
                    _viewModelState.postValue(ViewModelState.IDLE)
                    ToastManager.showToast(R.string.failed_to_grade_quiz, ToastType.ERROR)
                }
            }
            catch (e: Exception){
                _viewModelState.postValue(ViewModelState.IDLE)
                ToastManager.showToast(R.string.failed_to_grade_quiz, ToastType.ERROR)
            }
        }
    }

    fun loadQuiz(quizId: String, scoreCardViewModel: ScoreCardViewModel) {
        _viewModelState.postValue(ViewModelState.LOADING)
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = RetrofitInstance.api.getQuizData(quizId)
                Logger.debug("RESPONSE SUCCESS")
                if (response.isSuccessful) {
                    coroutineScope{
                        val loadScoreCardDeferred = async { scoreCardViewModel.loadScoreCard(response.body()!!.scoreCard) }
                        val loadQuizDataDeferred = async { loadQuizData(response.body()!!.quizData, response.body()!!.quizTheme) }
                        loadScoreCardDeferred.await()
                        loadQuizDataDeferred.await()
                        _viewModelState.postValue(ViewModelState.IDLE)
                    }
                } else {
                    ToastManager.showToast(R.string.failed_to_load_quiz, ToastType.ERROR)
                    _viewModelState.postValue(ViewModelState.ERROR)
                }
            } catch (e: Exception) {
                Logger.debug("Failed to load quiz ${e.message}")
                withContext(Dispatchers.Main) {
                    ToastManager.showToast(R.string.failed_to_load_quiz, ToastType.ERROR)
                    _viewModelState.value = ViewModelState.ERROR
                }
            }
        }
    }

    fun loadQuizData(quizData: QuizDataSerializer, quizTheme: QuizTheme) {
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
    }

    fun initQuizLayout(email: String?, colorScheme: ColorScheme) {
        _quizData.value = _quizData.value.copy(creator = email ?: "GUEST")
        _visibleQuizzes.value = emptyList()
        _quizTheme.value = _quizTheme.value.copy(colorScheme = colorScheme)
        _policyAgreement.value = false
        _initIndex.value = 0
        _step.value = LayoutSteps.POLICY
    }

    fun tryUpload(navController: NavController, scoreCard: ScoreCard, onUpload: () -> Unit) {
        _viewModelState.value = ViewModelState.UPLOADING
        if(!validateQuizLayout()){
            _viewModelState.postValue(ViewModelState.IDLE)
            navController.popBackStack()
            return
        }
        uploadQuizLayout(scoreCard, onUpload)
    }

    private fun uploadQuizLayout(scoreCard: ScoreCard, onUpload: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsoned = toJson(scoreCard)
                val response = RetrofitInstance.api.addQuiz(jsoned)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        ToastManager.showToast(R.string.quiz_uploaded_successfully, ToastType.SUCCESS)
                        onUpload()
                        _viewModelState.postValue(ViewModelState.IDLE)
                    } else {
                        _viewModelState.postValue(ViewModelState.IDLE)
                        val errorMessage = response.errorBody()?.string()?.let { getErrorMessage(it) }
                        ToastManager.showToast(R.string.failed_to_upload_quiz, ToastType.ERROR)
                    }
                }
            } catch (e: Exception) {
                _viewModelState.postValue(ViewModelState.IDLE)
                ToastManager.showToast(R.string.failed_to_upload_quiz, ToastType.ERROR)
            }
        }
    }

    suspend fun saveLocal(context: Context, scoreCard: ScoreCard) {
        _viewModelState.postValue(ViewModelState.LOADING)
        val jsoned = Json.encodeToString(toJson(scoreCard))
        val fileName = "${quizData.value.uuid}_${quizData.value.creator}_quizSave.json"
        val file = File(context.filesDir, fileName)
        file.writeText(jsoned)
        _viewModelState.postValue(ViewModelState.IDLE)
        ToastManager.showToast(R.string.save_success, ToastType.SUCCESS)
    }

    fun validateQuizLayout(): Boolean {
        if(_quizData.value.title.isEmpty()) {
            ToastManager.showToast(R.string.title_cannot_be_empty, ToastType.ERROR)
            return false
        }
        if(_quizzes.value.isEmpty()) {
            ToastManager.showToast(R.string.quiz_cannot_be_empty, ToastType.ERROR)
            return false
        }
        for(quiz in _quizzes.value){
            val check = quiz.validateQuiz()
            when(check){
                QuizError.EMPTY_QUESTION -> {
                    ToastManager.showToast(R.string.question_cannot_be_empty, ToastType.ERROR)
                    updateInitIndex(_quizzes.value.indexOf(quiz))
                    return false
                }
                QuizError.EMPTY_ANSWER -> {
                    ToastManager.showToast(R.string.answers_cannot_be_empty, ToastType.ERROR)
                    updateInitIndex(_quizzes.value.indexOf(quiz))
                    return false
                }
                QuizError.EMPTY_OPTION -> {
                    ToastManager.showToast(R.string.correct_answer_not_selected, ToastType.ERROR)
                    updateInitIndex(_quizzes.value.indexOf(quiz))
                    return false
                }
                QuizError.NO_ERROR -> {}
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
        _visibleQuizzes.value = emptyList()
        _viewModelState.value = ViewModelState.IDLE
        _policyAgreement.value = false
        _step.value = LayoutSteps.POLICY
    }

    fun setQuizTitle(title: String) {
        _quizData.value = _quizData.value.copy(title = title, uuid = null)
    }

    fun setQuizImage(image: ByteArray) {
        if(image.size > 80000){
            ToastManager.showToast(R.string.image_size_too_large, ToastType.ERROR)
            return
        }
        _quizData.value = _quizData.value.copy(image = image)
        if(image.isEmpty()){
            _titleImageColors.value = emptyList()
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            val seedColor = calculateSeedColor(byteArrayToImageBitmap(image))
            if(seedColor.size < 3){
                _titleImageColors.value = seedColor + List(3 - seedColor.size) { seedColor[0] }
            }
            _titleImageColors.value = seedColor
        }
    }

    fun setQuizBodyImage(image: ByteArray){
        if(_quizzes.value.isEmpty()){
            return
        }
        _quizzes.update {
            it.toMutableList().apply {
                val firstQuiz = this[0]
                firstQuiz.bodyType = BodyType.IMAGE(image)
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

    fun updateBackgroundImage(image: ByteArray){
        val imageColor = _quizTheme.value.backgroundImage
        _quizTheme.value = _quizTheme.value.copy(backgroundImage = imageColor.copy(imageData = image))
    }

    fun updateBackgroundColor(color: Color){
        val imageColor = _quizTheme.value.backgroundImage
        _quizTheme.value = _quizTheme.value.copy(backgroundImage = imageColor.copy(color = color))
    }

    fun updateImageColorState(state: ImageColorState){
        val imageColor = _quizTheme.value.backgroundImage
        _quizTheme.value = _quizTheme.value.copy(backgroundImage = imageColor.copy(state = state))
    }

    fun updateGradientColor(gradientColor: Color){
        val imageColor = _quizTheme.value.backgroundImage
        _quizTheme.value = _quizTheme.value.copy(backgroundImage = imageColor.copy(colorGradient = gradientColor))
    }

    fun updateGradientType(shaderType: ShaderType){
        val imageColor = _quizTheme.value.backgroundImage
        _quizTheme.value = _quizTheme.value.copy(backgroundImage = imageColor.copy(shaderType = shaderType))
    }

    fun setColorScheme(name: String, color: Color){
        val colorScheme = _quizTheme.value.colorScheme
        val updateColorScheme = when(name){
            ColorList[0] -> colorScheme.withPrimaryColor(color)
            ColorList[1] -> colorScheme.withSecondaryColor(color)
            ColorList[2] -> colorScheme.withTertiaryColor(color)
            else -> colorScheme
        }
        _quizTheme.value = _quizTheme.value.copy(colorScheme = updateColorScheme)
    }

    fun generateColorScheme(
        base: GenerateWith,
        paletteLevel: Int,
        contrastLevel: Int,
        isDark: Boolean,
    ){
        viewModelScope.launch {
            if(contrastLevel in 0 until contrastSize && paletteLevel in 0 .. paletteSize){
                when(base){
                    GenerateWith.TITLE_IMAGE -> {
                        if(_titleImageColors.value.isEmpty()){
                            return@launch
                        }
                        val titleImageColorScheme =
                            if(paletteLevel == paletteSize){ toScheme(
                                primary = _titleImageColors.value[0],
                                secondary = _titleImageColors.value[1],
                                tertiary = _titleImageColors.value[2],
                                isLight = !isDark
                            )
                            }else{
                                randomDynamicColorScheme(titleImageColors.value[0], paletteLevel, contrastLevel, isDark)
                            }
                        setColorScheme(titleImageColorScheme)
                    }
                    GenerateWith.COLOR -> {
                        val colorScheme = _quizTheme.value.colorScheme
                        val primaryColorScheme =
                            if (paletteLevel == paletteSize) {
                                toScheme(
                                    primary = colorScheme.primary,
                                    secondary = colorScheme.secondary,
                                    tertiary = colorScheme.tertiary,
                                    isLight = !isDark
                                )
                            } else {
                                randomDynamicColorScheme(
                                    colorScheme.primary,
                                    paletteLevel,
                                    contrastLevel,
                                    isDark
                                )
                            }
                        setColorScheme(primaryColorScheme)
                    }
                }
            }
        }
    }

    fun setColorScheme(colorScheme: ColorScheme) {
        _quizTheme.update {
            it.copy(colorScheme = colorScheme,
                backgroundImage = it.backgroundImage.copy(color = colorScheme.surface)
            )
        }
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
        quiz.toggleUserAnswerAt(answerIndex)
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
        _viewModelState.postValue(ViewModelState.IDLE)
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