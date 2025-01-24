package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import android.net.TrafficStats
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.network.RetrofitInstance
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.QuizCardsWithTag
import com.asu1.quizcardmodel.Recommendations
import com.asu1.resources.NetworkTags
import com.asu1.resources.R
import com.asu1.utils.Logger
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class QuizCardMainViewModel : ViewModel() {

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
                val response = com.asu1.network.RetrofitInstance.api.getUserRanks()
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
                val response = com.asu1.network.RetrofitInstance.api.getTrends(language)
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

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
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