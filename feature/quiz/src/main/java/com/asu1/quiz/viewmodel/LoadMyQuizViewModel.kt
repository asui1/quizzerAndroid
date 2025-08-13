package com.asu1.quiz.viewmodel

import SnackBarManager
import ToastType
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.appdatausecase.quizData.DeleteMyQuizUseCase
import com.asu1.appdatausecase.quizData.GetMyQuizUseCase
import com.asu1.quizcardmodel.QuizCard
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadMyQuizViewModel @Inject constructor(
    private val getMyQuiz: GetMyQuizUseCase,
    private val deleteMyQuizUseCase: DeleteMyQuizUseCase,
    ) : ViewModel() {
    private val _loadMyQuizViewModelState = MutableLiveData(ViewModelState.IDLE)
    val loadMyQuizViewModelState: MutableLiveData<ViewModelState> get() = _loadMyQuizViewModelState

    private val _myQuizList = MutableStateFlow<List<QuizCard>?>(null)
    // ✅ ViewModel
    val myQuizList: Flow<PersistentList<QuizCard>> =
        _myQuizList.map { it?.toPersistentList() ?: persistentListOf() }

    fun reset(){
        _loadMyQuizViewModelState.postValue(ViewModelState.IDLE)
        _myQuizList.value = null
    }

    fun loadUserQuiz(email: String) {
        if (email.isBlank()) return
        viewModelScope.launch {
            getMyQuiz(email)
                .onSuccess { list ->
                    _myQuizList.value = list.toMutableList()
                }
                .onFailure { e ->
                    Logger.debug("loadUserQuiz failed: ${e.message}")
                    SnackBarManager.showSnackBar(R.string.search_failed, ToastType.ERROR)
                }
        }
    }

    fun deleteMyQuiz(uuid: String, email: String) {
        val current = _myQuizList.value ?: return
        val target = current.find { it.id == uuid } ?: return

        viewModelScope.launch {
            deleteMyQuizUseCase(uuid, email)
                .onSuccess {
                    val updated = current.toMutableList().apply { remove(target) }
                    _myQuizList.value = updated
                    SnackBarManager.showSnackBar(R.string.delete_successful, ToastType.SUCCESS)
                }
                .onFailure {
                    SnackBarManager.showSnackBar(R.string.delete_failed, ToastType.ERROR)
                }
        }
    }
}
