package com.asu1.quizzer.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.data.QuizDataSerializer
import com.asu1.quizzer.data.QuizLayoutSerializer
import com.asu1.quizzer.data.json
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.util.Logger
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

    fun reset(){
        _quizList.value = null
        _loadComplete.value = false
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
    }
}