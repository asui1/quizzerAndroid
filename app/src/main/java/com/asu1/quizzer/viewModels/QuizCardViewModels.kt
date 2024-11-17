package com.asu1.quizzer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.QuizCard
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

    private val _bottomBarSelection = MutableLiveData(0)
    val bottomBarSelection: LiveData<Int> get() = _bottomBarSelection

    init{
        _bottomBarSelection.value = 0
    }

    fun setBottomBarSelection(index: Int) {
        if(index < 0 || index > 4) return
        _bottomBarSelection.postValue(index)
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