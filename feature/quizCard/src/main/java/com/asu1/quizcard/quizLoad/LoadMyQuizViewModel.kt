package com.asu1.quizcard.quizLoad

import SnackBarManager
import ToastType
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.network.RetrofitInstance
import com.asu1.quizcardmodel.QuizCard
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.utils.Logger
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LoadMyQuizViewModel: ViewModel() {
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

    fun loadUserQuiz(email: String){
        if(email.isEmpty()) return
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMyQuiz(email)
                if(response.isSuccessful){
                    val quizCards = response.body()?.searchResult
                    if(quizCards != null){
                        _myQuizList.value = quizCards as MutableList<QuizCard>?
                    }
                    else{
                        _myQuizList.value = mutableListOf()
                    }
                }
                else{
                    Logger.debug("loadUserQuiz Failure")
                    SnackBarManager.showSnackBar(R.string.search_failed, ToastType.ERROR)
                }
            }
            catch (e: Exception){
                Logger.debug("loadUserQuizFailed ${e.message}")
                SnackBarManager.showSnackBar(R.string.search_failed, ToastType.ERROR)
            }
        }
    }

    fun deleteMyQuiz(uuid: String, email: String){
        if(_myQuizList.value == null) return
        val quiz = _myQuizList.value?.find { it.id == uuid }
        if(quiz == null) return
        viewModelScope.launch {
            val response = RetrofitInstance.api.deleteQuiz(quiz.id, email)
            if(response.isSuccessful){
                val updatedList = _myQuizList.value?.toMutableList()
                updatedList?.remove(quiz)
                _myQuizList.value = updatedList
                SnackBarManager.showSnackBar(R.string.delete_successful, ToastType.SUCCESS)
            }
            else{
                SnackBarManager.showSnackBar(R.string.delete_failed, ToastType.ERROR)
            }
        }
    }
}