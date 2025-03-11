package com.asu1.quizzer.viewModels.quizModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.models.serializers.json
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.collections.remove

class LoadLocalQuizViewModel: ViewModel() {
    private val _localQuizList = MutableStateFlow<List<QuizLayoutSerializer>?>(null)
    val localQuizList: StateFlow<List<QuizLayoutSerializer>?> = _localQuizList.asStateFlow()

    private val _loadLocalQuizViewModelState = MutableLiveData(ViewModelState.IDLE)
    val loadLocalQuizViewModelState: MutableLiveData<ViewModelState> get() = _loadLocalQuizViewModelState

    fun reset(){
        _loadLocalQuizViewModelState.postValue(ViewModelState.IDLE)
        _localQuizList.value = null
    }

    fun loadComplete(){
        _loadLocalQuizViewModelState.postValue(ViewModelState.SUCCESS)
    }

    fun deleteLocalQuiz(context: Context, uuid: String){
        if(_localQuizList.value == null) return
        val quiz = localQuizList.value?.find { it.quizData.uuid == uuid }
        if(quiz == null) return
        viewModelScope.launch(Dispatchers.IO) {
            val fileName = "${quiz.quizData.uuid}_${quiz.quizData.creator}_quizSave.json"
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                file.delete()
                val updatedList = _localQuizList.value?.toMutableList()
                updatedList?.remove(quiz)
                ToastManager.showToast(R.string.delete_successful, ToastType.SUCCESS)
                _localQuizList.value = updatedList
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
                _localQuizList.value = loadedQuizzes
            }
        }
    }
}

