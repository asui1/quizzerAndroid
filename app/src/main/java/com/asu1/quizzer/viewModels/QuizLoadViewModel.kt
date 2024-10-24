package com.asu1.quizzer.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.data.QuizDataSerializer
import com.asu1.quizzer.data.QuizLayoutSerializer
import com.asu1.quizzer.data.json
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.viewModels.QuizCardMainViewModel.QuizCards
import com.asu1.quizzer.viewModels.QuizCardMainViewModel.QuizCardsWithTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class QuizLoadViewModel: ViewModel() {
    private val _quizList = MutableStateFlow<MutableList<QuizLayoutSerializer>?>(null)
    val quizList: StateFlow<MutableList<QuizLayoutSerializer>?> = _quizList.asStateFlow()

    private val _loadComplete = MutableStateFlow(false)
    val loadComplete: StateFlow<Boolean> = _loadComplete.asStateFlow()

    private val _myQuizList = MutableStateFlow<MutableList<QuizCard>?>(null)
    val myQuizList: StateFlow<MutableList<QuizCard>?> = _myQuizList.asStateFlow()

    private val _showToast = MutableLiveData<String?>()
    val showToast: LiveData<String?> get() = _showToast

    fun reset(){
        _quizList.value = null
        _loadComplete.value = false
        _myQuizList.value = null
    }

    fun loadUserQuiz(email: String){
        if(email.isEmpty()) return
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMyQuiz(email)
                Logger().debug("Response: $response")
                if(response.isSuccessful){
                    Logger().debug("Response Body: ${response.body()}")
                    val quizCards = response.body()?.quizCards
                    _myQuizList.value = quizCards as MutableList<QuizCard>?
                    Logger().debug(_myQuizList.value.toString())
                }
                else{
                    _showToast.value = "Search No Response"
                }
            }
            catch (e: Exception){
                Logger().debug("Search Failed: $e")
                _showToast.value = "Search Failed"
            }
        }
    }

    fun loadComplete(){
        _loadComplete.value = true
    }

    fun deleteLocalQuiz(index: Int){
        if(_quizList.value == null) return
        if(index < 0 || index >= _quizList.value!!.size) return
        viewModelScope.launch(Dispatchers.IO) {
            val quiz = _quizList.value?.get(index)
            if (quiz != null) {
                val file = File("${quiz.quizData.uuid}_quizSave.json")
                if (file.exists()) {
                    file.delete()
                    _quizList.value?.removeAt(index)
                    _quizList.value = _quizList.value
                }
            }
        }
    }

    fun deleteMyQuiz(index: Int){
        TODO("SEND DELETE REQUEST TO SERVER")
    }

    fun loadLocalQuiz(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val directory = context.filesDir
            Logger().debug(directory.toString())
            if (directory.exists() && directory.isDirectory) {
                val files = directory.listFiles { _, name ->
                    Logger().debug(name)
                    name.endsWith("_quizSave.json") }
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
    fun toastShown() {
        _showToast.value = null
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
        val quizCard = QuizCard(
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