package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import android.net.TrafficStats
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.R
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizzer.model.UserRank
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.constants.NetworkTags
import com.asu1.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizCardMainViewModel : ViewModel() {

    data class QuizCardsWithTag(
        val tag: String,
        val quizCards: List<com.asu1.quizcardmodel.QuizCard>
    )

    private val _loadResultId = MutableLiveData<String?>(null)
    val loadResultId: MutableLiveData<String?> get() = _loadResultId

    private val _loadQuizId = MutableLiveData<String?>(null)
    val loadQuizId: MutableLiveData<String?> get() = _loadQuizId

    private val _quizCards = MutableStateFlow<List<QuizCardsWithTag>>(emptyList())
    val quizCards: StateFlow<List<QuizCardsWithTag>> get() = _quizCards.asStateFlow()

    private val _quizTrends = MutableStateFlow<List<com.asu1.quizcardmodel.QuizCard>>(emptyList())

    private val trendPageCount = 5
    private val _visibleQuizTrends = MutableStateFlow<List<com.asu1.quizcardmodel.QuizCard>>(emptyList())
    val visibleQuizTrends: StateFlow<List<com.asu1.quizcardmodel.QuizCard>> get() = _visibleQuizTrends.asStateFlow()

    private val _userRanks = MutableStateFlow<List<UserRank>>(emptyList())

    private val userRankPageCount = 8
    private val _visibleUserRanks = MutableStateFlow<List<UserRank>>(emptyList())
    val visibleUserRanks: StateFlow<List<UserRank>> get() = _visibleUserRanks.asStateFlow()

    fun getMoreQuizTrends(){
        viewModelScope.launch {
            val start = _visibleQuizTrends.value.size
            val end = minOf(start + trendPageCount, _quizTrends.value.size)
            if (start < end) {
                _visibleQuizTrends.update {
                    it.toMutableList().apply {
                        addAll(_quizTrends.value.subList(start, end))
                    }
                }
            }
        }
    }

    fun getMoreUserRanks(){
        viewModelScope.launch {
            val start = _visibleUserRanks.value.size
            val end = minOf(start + userRankPageCount, _userRanks.value.size)
            if (start < end) {
                _visibleUserRanks.update {
                    it.toMutableList().apply {
                        addAll(_userRanks.value.subList(start, end))
                    }
                }
            }
        }
    }

    fun setLoadResultId(resultId: String?){
        _loadResultId.value = resultId
    }

    fun setLoadQuizId(quizId: String?){
        _loadQuizId.value = quizId
    }

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
        _visibleUserRanks.value = emptyList()
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUserRanks()
                if (response.isSuccessful && response.body() != null) {
                    _userRanks.value = response.body()!!.searchResult
                    _visibleUserRanks.value = response.body()!!.searchResult.take(trendPageCount)
                } else {
                    ToastManager.showToast(R.string.failed_to_get_user_ranks, ToastType.ERROR)
                }
            } catch (e: Exception) {
                ToastManager.showToast(R.string.failed_to_get_user_ranks, ToastType.ERROR)
            }
        }
    }

    fun fetchQuizTrends(language: String){
        _quizTrends.value = emptyList()
        _visibleQuizTrends.value = emptyList()
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTrends(language)
                if (response.isSuccessful && response.body() != null) {
                    _quizTrends.value = response.body()!!.searchResult
                    _visibleQuizTrends.value = response.body()!!.searchResult.take(userRankPageCount)
                } else {
                    ToastManager.showToast(R.string.failed_to_fetch_quiz_trends, ToastType.ERROR)
                }
            } catch (e: Exception) {
                Logger.debug("Failed to fetch quiz trends", e)
                ToastManager.showToast(R.string.failed_to_fetch_quiz_trends, ToastType.ERROR)
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                TrafficStats.setThreadStatsTag(NetworkTags.FETCH_QUIZ_CARDS_TAG)
                val response = RetrofitInstance.api.getRecommendations(language, email)
                if (response.isSuccessful && response.body() != null) {
                    _quizCards.value = response.body()!!.searchResult.map {
                        QuizCardsWithTag(it.key, it.items)
                    }
                } else {
                    _quizCards.value = emptyList()
                }
            } catch (e: Exception) {
                _quizCards.value = emptyList()
            } finally {
                TrafficStats.clearThreadStatsTag()
            }
        }
    }
}