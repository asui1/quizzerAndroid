package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.appdata.suggestion.SearchSuggestionRepository
import com.asu1.network.QuizApi
import com.asu1.network.RecommendationApi
import com.asu1.network.runApi
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.QuizCardsWithTag
import com.asu1.quizcardmodel.Recommendations
import com.asu1.resources.R
import com.asu1.utils.LanguageSetter
import com.asu1.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class QuizCardViewModel @Inject constructor(
    private val quizApi: QuizApi,
    private val recommendationApi: RecommendationApi,
) : ViewModel() {
    private val _loadResultId = MutableLiveData<String?>(null)
    val loadResultId: MutableLiveData<String?> get() = _loadResultId

    private val _loadQuizId = MutableLiveData<String?>(null)
    val loadQuizId: MutableLiveData<String?> get() = _loadQuizId

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

    fun setLoadResultId(resultId: String?){
        _loadResultId.value = resultId
    }

    fun setLoadQuizId(quizId: String?){
        _loadQuizId.value = quizId
    }

    @Suppress("unused")
    fun tryUpdate(index: Int){
        if(index == 0){
            if(_quizCards.value.isEmpty()){
                fetchQuizCards()
            }
        }else if (index == 1){
            if(_quizTrends.value.isEmpty()){
                fetchQuizTrends()
            }
        }else if (index == 2){
            fetchUserRanks()
        }
    }

    private fun fetchUserRanks() {
        _userRanks.value = emptyList()
        _visibleUserRanks.value = emptyList()

        viewModelScope.launch {
            runApi { quizApi.getUserRanks() }      // Result<Response<UserRankList>>
                .mapCatching { resp ->
                    if (!resp.isSuccessful) throw HttpException(resp)
                    resp.body()?.searchResult ?: throw NoSuchElementException("Empty body")
                }
                .onSuccess { ranks ->
                    _userRanks.value = ranks
                    _visibleUserRanks.value = ranks.take(trendPageCount)
                }
                .onFailure { e ->
                    Logger.debug("Fetch user ranks error: ${e.message}")
                    SnackBarManager.showSnackBar(R.string.failed_to_get_user_ranks, ToastType.ERROR)
                }
        }
    }

    private fun fetchQuizTrends() {
        val language = if (LanguageSetter.isKo) "ko" else "en"
        _quizTrends.value = emptyList()
        _visibleQuizTrends.value = emptyList()

        viewModelScope.launch {
            runApi { quizApi.getTrends(language) }          // Result<Response<QuizCardList>>
                .mapCatching { resp ->
                    if (!resp.isSuccessful) throw HttpException(resp)
                    resp.body()?.searchResult ?: throw NoSuchElementException("Empty body")
                }
                .onSuccess { result ->
                    _quizTrends.value = result
                    _visibleQuizTrends.value = result.take(trendPageCount)
                }
                .onFailure { e ->
                    Logger.debug("Fetch trends error: ${e.message}")
                }
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
            val results = supervisorScope {
                awaitAll(
                    async { fetchRec { recommendationApi.mostViewed(language) } },
                    async { fetchRec { recommendationApi.mostRecent(language) } },
                    async { fetchRec { recommendationApi.getRelated(language) } },
                ).filterNotNull()
            }

            _quizCards.value = results
                .filter { it.items.isNotEmpty() }
                .map { QuizCardsWithTag(it.key, it.items) }
        }
    }

    /** One place for status/body checks + runApi error mapping. */
    private suspend fun fetchRec(
        call: suspend () -> retrofit2.Response<Recommendations>
    ): Recommendations? {
        return runApi { call() }                       // Result<Response<Recommendations>>
            .mapCatching { resp ->
                if (!resp.isSuccessful) throw retrofit2.HttpException(resp)
                resp.body() ?: throw NoSuchElementException("Empty body")
            }
            .onFailure { e -> Logger.debug("fetchRec failed: ${e.message}") }
            .getOrNull()                               // null -> this leg is skipped
    }
}
