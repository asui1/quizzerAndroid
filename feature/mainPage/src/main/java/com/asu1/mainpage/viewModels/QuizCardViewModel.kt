package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.appdatausecase.FetchHomeRecommendationsUseCase
import com.asu1.appdatausecase.quizData.GetQuizTrendsUseCase
import com.asu1.appdatausecase.quizData.GetUserRanksUseCase
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.QuizCardsWithTag
import com.asu1.resources.R
import com.asu1.utils.LanguageSetter
import com.asu1.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizCardViewModel @Inject constructor(
    private val getUserRanks: GetUserRanksUseCase,
    private val getQuizTrends: GetQuizTrendsUseCase,
    private val fetchHomeRecommendations: FetchHomeRecommendationsUseCase
) : ViewModel() {
    private val _quizCards = MutableStateFlow<List<QuizCardsWithTag>>(emptyList())
    val quizCards: StateFlow<List<QuizCardsWithTag>> get() = _quizCards.asStateFlow()

    private val _quizTrends = MutableStateFlow<List<QuizCard>>(emptyList())

    private val trendPageCount = 5
    private val _visibleQuizTrends = MutableStateFlow<List<QuizCard>>(emptyList())
    val visibleQuizTrends: StateFlow<List<QuizCard>> get() = _visibleQuizTrends.asStateFlow()

    private val _userRanks = MutableStateFlow<List<com.asu1.userdatamodels.UserRank>>(emptyList())

    private val userRankPageCount = 8
    private val _visibleUserRanks = MutableStateFlow<List<com.asu1.userdatamodels.UserRank>>(emptyList())
    val visibleUserRanks: StateFlow<List<com.asu1.userdatamodels.UserRank>> get() = _visibleUserRanks.asStateFlow()

    fun getMoreQuizTrends(){
        viewModelScope.launch {
            val start = _visibleQuizTrends.value.size
            val end = minOf(start + trendPageCount, _quizTrends.value.size)
            if (start < end) {
                _visibleQuizTrends.update {
                    it.toMutableList().apply {
                        this.addAll(_quizTrends.value.subList(start, end))
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
                        this.addAll(_userRanks.value.subList(start, end))
                    }
                }
            }
        }
    }

    @Suppress("unused")
    fun tryUpdate(index: Int){
        if(index == 0){
            if(_quizCards.value.isEmpty()){
                fetchQuizCards()
            }
        }else if (index == 1){
            if(_quizTrends.value.isEmpty()){
                fetchQuizTrends(LanguageSetter.lang)
            }
        }else if (index == 2){
            fetchUserRanks()
        }
    }

    private fun fetchUserRanks() = viewModelScope.launch {
        _userRanks.value = emptyList()
        _visibleUserRanks.value = emptyList()

        getUserRanks()
            .onSuccess { ranks ->
                _userRanks.value = ranks
                _visibleUserRanks.value = ranks.take(userRankPageCount)
            }
            .onFailure { e ->
                Logger.debug("Fetch user ranks error: ${e.message}")
                SnackBarManager.showSnackBar(R.string.failed_to_get_user_ranks, ToastType.ERROR)
            }
    }

    private fun fetchQuizTrends(language: String) = viewModelScope.launch {
        _quizTrends.value = emptyList()
        _visibleQuizTrends.value = emptyList()

        getQuizTrends(language)
            .onSuccess { trends ->
                _quizTrends.value = trends
                _visibleQuizTrends.value = trends.take(trendPageCount)
            }
            .onFailure { e ->
                Logger.debug("Fetch quiz trends error: ${e.message}")
                SnackBarManager.showSnackBar(R.string.failed_to_fetch_quiz_trends, ToastType.ERROR)
            }
    }

    fun resetQuizTrends(){
        _quizTrends.value = emptyList()
    }

    fun resetUserRanks(){
        _userRanks.value = emptyList()
    }

    fun fetchQuizCards() {
        val language = if (LanguageSetter.isKo) "ko" else "en"
        _quizCards.value = emptyList()

        viewModelScope.launch {
            fetchHomeRecommendations(language)
                .onSuccess { sections -> _quizCards.value = sections }
                .onFailure { e ->
                    Logger.debug("fetchHomeRecommendations error: ${e.message}")
                    SnackBarManager.showSnackBar(R.string.failed_request, ToastType.ERROR)
                }
        }
    }
}
