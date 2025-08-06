package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.network.RetrofitInstance
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.QuizCardsWithTag
import com.asu1.quizcardmodel.Recommendations
import com.asu1.resources.R
import com.asu1.utils.LanguageSetter
import com.asu1.utils.Logger
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

class QuizCardViewModel : ViewModel() {

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

    private val disposables = CompositeDisposable()

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

    private fun fetchUserRanks(){
        _userRanks.value = emptyList()
        _visibleUserRanks.value = emptyList()
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUserRanks()
                if (response.isSuccessful && response.body() != null) {
                    _userRanks.value = response.body()!!.searchResult
                    _visibleUserRanks.value = response.body()!!.searchResult.take(trendPageCount)
                } else {
                    Logger.debug("Failed to get user ranks")
                    SnackBarManager.showSnackBar(R.string.failed_to_get_user_ranks, ToastType.ERROR)
                }
            } catch (e: IOException) {
                Logger.debug("Network error fetching user ranks", e)
                SnackBarManager.showSnackBar(
                    R.string.failed_to_get_user_ranks,
                    ToastType.ERROR
                )
            } catch (e: HttpException) {
                Logger.debug("Server error fetching user ranks: HTTP ${e.code()}", e)
                SnackBarManager.showSnackBar(
                    R.string.failed_to_get_user_ranks,
                    ToastType.ERROR
                )
            }
        }
    }

    private fun fetchQuizTrends(){
        val language = if (LanguageSetter.isKo) "ko" else "en"
        _quizTrends.value = emptyList()
        _visibleQuizTrends.value = emptyList()
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTrends(language)

                check(response.isSuccessful && response.body() != null) {
                    "Failed to fetch trends: ${response.errorBody()?.string().orEmpty()}"
                }
                val result = response.body()!!.searchResult
                _quizTrends.value = result
                _visibleQuizTrends.value = result.take(trendPageCount)

            }  catch (e: IllegalStateException) {
                Logger.debug("Fetch trends precondition failed: ${e.message}")
            } catch (e: IOException) {
                // Network I/O error
                Logger.debug("Network error fetching quiz trends", e)
            } catch (e: HttpException) {
                // Protocol error (non‐2xx status codes)
                Logger.debug("Server error fetching quiz trends: HTTP ${e.code()}", e)
            }
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
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
        val disposable = Observable.zip(
            Observable.fromCallable{
                runBlocking {
                    RetrofitInstance.api.mostViewed(language).body() ?: Recommendations("", emptyList())
                }
            },
            Observable.fromCallable{
                runBlocking {
                    RetrofitInstance.api.mostRecent(language).body() ?: Recommendations("", emptyList())
                }
            },
            Observable.fromCallable{
                runBlocking {
                    RetrofitInstance.api.getRelated(language).body() ?: Recommendations("", emptyList())
                }
            },
        ){result1, result2, result3 ->
            listOfNotNull(result1, result2, result3)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { results ->
                    _quizCards.value = results
                        .filter { it.items.isNotEmpty() }
                        .map { QuizCardsWithTag(it.key, it.items) }
                },
                { error ->
                    Logger.debug("Failed to fetch quiz cards", error)
                    _quizCards.value = emptyList()
                }
            )

        disposables.add(disposable)
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                TrafficStats.setThreadStatsTag(NetworkTags.FETCH_QUIZ_CARDS_TAG)
//                val response = com.asu1.network.RetrofitInstance.api.getRecommendations(language, email)
//                if (response.isSuccessful && response.body() != null) {
//                    _quizCards.value = response.body()!!.searchResult.map {
//                        QuizCardsWithTag(it.key, it.items)
//                    }
//                } else {
//                    _quizCards.value = emptyList()
//                }
//            } catch (e: Exception) {
//                _quizCards.value = emptyList()
//            } finally {
//                TrafficStats.clearThreadStatsTag()
//            }
//        }
    }
}
