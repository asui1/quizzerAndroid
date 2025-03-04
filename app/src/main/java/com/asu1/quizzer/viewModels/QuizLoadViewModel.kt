package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.serializers.QuizDataSerializer
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.models.serializers.json
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class QuizLoadViewModel: ViewModel() {
    private val _quizList = MutableStateFlow<MutableList<QuizLayoutSerializer>?>(null)
    val quizList: StateFlow<MutableList<QuizLayoutSerializer>?> = _quizList.asStateFlow()

    private val _loadComplete = MutableLiveData(ViewModelState.IDLE)
    val loadComplete: MutableLiveData<ViewModelState> get() = _loadComplete

    private val _myQuizList = MutableStateFlow<MutableList<com.asu1.quizcardmodel.QuizCard>?>(null)
    val myQuizList: StateFlow<MutableList<com.asu1.quizcardmodel.QuizCard>?> = _myQuizList.asStateFlow()

    fun reset(){
        _quizList.value = null
        _loadComplete.postValue(ViewModelState.IDLE)
        _myQuizList.value = null
    }

    fun loadUserQuiz(email: String){
        if(email.isEmpty()) return
        viewModelScope.launch {
            try {
                val response = com.asu1.network.RetrofitInstance.api.getMyQuiz(email)
                if(response.isSuccessful){
                    val quizCards = response.body()?.searchResult
                    if(quizCards != null){
                        _myQuizList.value = quizCards as MutableList<com.asu1.quizcardmodel.QuizCard>?
                    }
                    else{
                        _myQuizList.value = mutableListOf()
                    }
                }
                else{
                    Logger.debug("loadUserQuiz Failure")
                    ToastManager.showToast(R.string.search_failed, ToastType.ERROR)
                }
            }
            catch (e: Exception){
                Logger.debug("loadUserQuizFailed ${e.message}")
                ToastManager.showToast(R.string.search_failed, ToastType.ERROR)
            }
        }
    }

    fun loadComplete(){
        _loadComplete.postValue(ViewModelState.SUCCESS)
    }

    fun deleteLocalQuiz(context: Context, uuid: String){
        if(_quizList.value == null) return
        val quiz = quizList.value?.find { it.quizData.uuid == uuid }
        if(quiz == null) return
        viewModelScope.launch(Dispatchers.IO) {
            val fileName = "${quiz.quizData.uuid}_${quiz.quizData.creator}_quizSave.json"
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                file.delete()
                val updatedList = _quizList.value?.toMutableList()
                updatedList?.remove(quiz)
                ToastManager.showToast(R.string.delete_successful, ToastType.SUCCESS)
                _quizList.value = updatedList
            }
        }
    }

    fun deleteMyQuiz(uuid: String, email: String){
        if(_myQuizList.value == null) return
        val quiz = _myQuizList.value?.find { it.id == uuid }
        if(quiz == null) return
        viewModelScope.launch {
            val response = com.asu1.network.RetrofitInstance.api.deleteQuiz(quiz.id, email)
            if(response.isSuccessful){
                val updatedList = _myQuizList.value?.toMutableList()
                updatedList?.remove(quiz)
                _myQuizList.value = updatedList
                ToastManager.showToast(R.string.delete_successful, ToastType.SUCCESS)
            }
            else{
                ToastManager.showToast(R.string.delete_failed, ToastType.ERROR)
            }
        }
    }

    fun loadLocalQuiz(context: Context, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val directory = context.filesDir
            if (directory.exists() && directory.isDirectory) {
                val files = directory.listFiles { _, name ->
                    name.endsWith("${email}_quizSave.json") }
                val loadedQuizzes = mutableListOf<QuizLayoutSerializer>()
                files?.forEach { file ->
                    if (file.isFile) {
                        val text = file.readText()
                        val quiz = json.decodeFromString<QuizLayoutSerializer>(text)
                        loadedQuizzes.add(quiz)
                    }
                }
                _quizList.value = loadedQuizzes
            }
        }
    }

    fun setTest(){
        val quiz = QuizLayoutSerializer(
            quizData = QuizDataSerializer(
                title = "Test",
                uuid = "test",
                quizzes = listOf(),
                description = "testDescription",
            ),
            quizTheme = QuizTheme(),
            scoreCard = ScoreCard()
        )
        _quizList.value = mutableListOf(quiz, quiz, quiz)
        val quizCard = com.asu1.quizcardmodel.QuizCard(
            id = "1",
            title = "Quiz 1",
            tags = listOf("tag1", "tag2", "tag2"),
            creator = "Creator",
            image = null,
            count = 0
        )
        _myQuizList.value = mutableListOf(quizCard, quizCard, quizCard)
    }
}