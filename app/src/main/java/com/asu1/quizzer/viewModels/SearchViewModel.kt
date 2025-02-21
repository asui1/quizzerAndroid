package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.resources.R
import com.asu1.resources.SearchBase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _searchResult = MutableStateFlow<List<com.asu1.quizcardmodel.QuizCard>?>(null)
    val searchResult: StateFlow<List<com.asu1.quizcardmodel.QuizCard>?> get() = _searchResult.asStateFlow()

    private val _searchText = MutableLiveData("")
    val searchText: LiveData<String> get() = _searchText

    private var searchBase = emptyList<SearchBase>()

    private val _searchRecommendations = MutableLiveData<List<SearchBase>>()
    val searchRecommendations: LiveData<List<SearchBase>> get() = _searchRecommendations

    fun reset(){
        _searchResult.value = null
    }

    fun setSearchText(searchText: String){
        _searchText.value = searchText
    }

    fun search(searchText: String){
        _searchText.value = searchText
        viewModelScope.launch {
            _searchResult.value = emptyList()
            try {
                val response = com.asu1.network.RetrofitInstance.api.searchQuiz(searchText)
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
                ToastManager.showToast(R.string.search_failed, ToastType.ERROR)
            }
        }
    }

    fun setSearchResult(quizCards: List<com.asu1.quizcardmodel.QuizCard>){
        _searchResult.value = quizCards
    }
}
