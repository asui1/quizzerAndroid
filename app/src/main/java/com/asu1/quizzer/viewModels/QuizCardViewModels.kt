package com.asu1.quizzer.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import kotlinx.coroutines.launch

class QuizCardMainViewModel(application: Application) : AndroidViewModel(application) {

    data class QuizCards(
        val quizCards1: QuizCardsWithTag,
        val quizCards2: QuizCardsWithTag,
        val quizCards3: QuizCardsWithTag,
    )

    data class QuizCardsWithTag(
        val tag: String,
        val quizCards: List<QuizCard>
    )

    private val _quizCards = MutableLiveData<QuizCards>()
    val quizCards: LiveData<QuizCards> get() = _quizCards

    private val _quizCardUpdated = MutableLiveData<Boolean>()
    val quizCardUpdated: LiveData<Boolean> get() = _quizCardUpdated


    fun fetchQuizCards(language: String) {
        if(_quizCardUpdated.value == true) return
        viewModelScope.launch {
            try {

                val response = RetrofitInstance.api.getRecommendations(language)
                if (response.isSuccessful && response.body() != null) {
                    Logger().debug("Quiz cards fetched successfully")
                    _quizCards.postValue(
                        QuizCards(
                            QuizCardsWithTag("Most Viewed", response.body()!!.mostViewed),
                            QuizCardsWithTag("Similar Items", response.body()!!.similarItems),
                            QuizCardsWithTag("Recent Items", response.body()!!.recentItems)
                        )
                    )
                    _quizCardUpdated.postValue(true)
                } else {
                    Logger().debug("Quiz cards fetch failed")
                    _quizCards.postValue(
                        QuizCards(
                            QuizCardsWithTag("Most Viewed", emptyList()),
                            QuizCardsWithTag("Similar Items", emptyList()),
                            QuizCardsWithTag("Recent Items", emptyList())
                        )
                    )
                    _quizCardUpdated.postValue(true)
                }
            } catch (e: Exception) {
                Logger().debug("Quiz cards fetch failed: $e")
                _quizCards.postValue(
                    QuizCards(
                        QuizCardsWithTag("Most Viewed", emptyList()),
                        QuizCardsWithTag("Similar Items", emptyList()),
                        QuizCardsWithTag("Recent Items", emptyList())
                    )
                )
            }
        }
    }
}