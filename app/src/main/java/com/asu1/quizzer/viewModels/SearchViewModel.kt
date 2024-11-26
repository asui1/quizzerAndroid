package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.R
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _searchResult = MutableStateFlow<List<QuizCard>?>(null)
    val searchResult: StateFlow<List<QuizCard>?> get() = _searchResult.asStateFlow()

    fun reset(){
        _searchResult.value = null
    }


    fun search(searchText: String){
        viewModelScope.launch {
            _searchResult.value = emptyList()
            try {
                val response = RetrofitInstance.api.searchQuiz(searchText)
                if(response.isSuccessful){
                    val quizCards = response.body()?.searchResult
                    if(quizCards == null){
                        return@launch
                    }else{
                        _searchResult.value = quizCards
                    }
                }
                else{
                    ToastManager.showToast(R.string.search_failed, ToastType.ERROR)
                }
            }
            catch (e: Exception){
                Logger().debug("Search Response: $e")
                ToastManager.showToast(R.string.search_failed, ToastType.ERROR)
            }
        }
    }

    fun setSearchResult(quizCards: List<QuizCard>){
        _searchResult.value = quizCards
    }
}
