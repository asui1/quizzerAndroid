package com.asu1.quizzer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizCardMainViewModel : ViewModel() {

    data class QuizCards(
        val quizCards1: QuizCardsWithTag,
        val quizCards2: QuizCardsWithTag,
        val quizCards3: QuizCardsWithTag,
    )

    private val emptyQuizCards = QuizCards(
        QuizCardsWithTag("", emptyList()),
        QuizCardsWithTag("", emptyList()),
        QuizCardsWithTag("", emptyList())
    )

    data class QuizCardsWithTag(
        val tag: String,
        val quizCards: List<QuizCard>
    )

    private val _quizCards = MutableStateFlow(
        emptyQuizCards
    )
    val quizCards: StateFlow<QuizCards> get() = _quizCards

    private val _quizCardUpdated = MutableLiveData<Boolean>()
    val quizCardUpdated: LiveData<Boolean> get() = _quizCardUpdated

    private val _bottomBarSelection = MutableLiveData(0)
    val bottomBarSelection: LiveData<Int> get() = _bottomBarSelection

    init{
        _bottomBarSelection.value = 0
    }

    fun setBottomBarSelection(index: Int) {
        if(index < 0 || index > 4) return
        _bottomBarSelection.postValue(index)
    }

    fun fetchQuizCards(language: String) {
        if(_quizCardUpdated.value == true) return
        viewModelScope.launch {
            try {

                val response = RetrofitInstance.api.getRecommendations(language)
                if (response.isSuccessful && response.body() != null) {
                    _quizCards.value =
                        QuizCards(
                            QuizCardsWithTag("Most Viewed", response.body()!!.mostViewed),
                            QuizCardsWithTag("Similar Items", response.body()!!.similarItems),
                            QuizCardsWithTag("Recent Items", response.body()!!.recentItems)
                        )

                    _quizCardUpdated.postValue(true)
                } else {
                    _quizCards.value  =
                        QuizCards(
                            QuizCardsWithTag("Most Viewed", emptyList()),
                            QuizCardsWithTag("Similar Items", emptyList()),
                            QuizCardsWithTag("Recent Items", emptyList())
                        )
                    _quizCardUpdated.postValue(true)
                }
            } catch (e: Exception) {
                _quizCards.value =
                    QuizCards(
                        QuizCardsWithTag("Most Viewed", emptyList()),
                        QuizCardsWithTag("Similar Items", emptyList()),
                        QuizCardsWithTag("Recent Items", emptyList())
                    )
            }
        }
    }
}