package com.asu1.quizzer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.UserRank
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizCardMainViewModel : ViewModel() {

    data class QuizCardsWithTag(
        val tag: String,
        val quizCards: List<QuizCard>
    )

    private val _quizCards = MutableStateFlow<List<QuizCardsWithTag>>(emptyList())
    val quizCards: StateFlow<List<QuizCardsWithTag>> get() = _quizCards.asStateFlow()

    private val _quizTrends = MutableStateFlow<List<QuizCard>>(emptyList())
    val quizTrends: StateFlow<List<QuizCard>> get() = _quizTrends.asStateFlow()

    private val _userRanks = MutableStateFlow<List<UserRank>>(emptyList())
    val userRanks: StateFlow<List<UserRank>> get() = _userRanks.asStateFlow()

    fun tryUpdate(index: Int, language: String = "ko"){
        if(index == 0){
            if(_quizCards.value.isEmpty()){
                fetchQuizCards(language)
            }
        }else if (index == 1){
            if(_quizTrends.value.isEmpty()){
                fetchQuizTrends(language)
            }
        }else if (index == 2){
            fetchUserRanks()
        }
    }

    fun fetchUserRanks(){
        _userRanks.value = emptyList()
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUserRanks()
                if (response.isSuccessful && response.body() != null) {
                    _userRanks.value = response.body()!!.searchResult
                } else {
                    _userRanks.value = emptyList()
                    Logger().debug("Search No Response ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Logger().debug("Search Failed: $e")
                _userRanks.value = emptyList()
            }
        }
    }

    fun fetchQuizTrends(language: String){
        _quizTrends.value = emptyList()
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTrends(language)
                if (response.isSuccessful && response.body() != null) {
                    _quizTrends.value = response.body()!!.searchResult
                } else {
                    _quizTrends.value = emptyList()
                    Logger().debug("Search No Response ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Logger().debug("Search Failed: $e")
                _quizTrends.value = emptyList()
            }
        }
    }

    fun resetQuizCards(){
        _quizCards.value = emptyList()
    }

    fun resetQuizTrends(){
        _quizTrends.value = emptyList()
    }

    fun resetUserRanks(){
        _userRanks.value = emptyList()
    }

    fun fetchQuizCards(language: String, email: String = "GUEST") {
        _quizCards.value = emptyList()
        viewModelScope.launch {
            try {

                val response = RetrofitInstance.api.getRecommendations(language, email)
                if (response.isSuccessful && response.body() != null) {
                    _quizCards.value = response.body()!!.searchResult.map {
                        QuizCardsWithTag(it.key, it.items)
                    }
                } else {
                    _quizCards.value = emptyList()
                    Logger().debug("Search No Response ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Logger().debug("Search Failed: $e")
                _quizCards.value = emptyList()
            }
        }
    }
}