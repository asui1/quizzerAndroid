package com.asu1.quizzer.viewModels

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.QuizCardList
import com.asu1.quizzer.model.UserRequest
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _showToast = MutableLiveData<String?>()
    val showToast: LiveData<String?> get() = _showToast

    private val _searchResult = MutableLiveData<List<QuizCard>?>()
    val searchResult: LiveData<List<QuizCard>?> get() = _searchResult

    fun reset(){
        _searchResult.postValue(null)
    }


    fun search(searchText: String){
        Logger().debug("Searching for: $searchText")
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchQuiz(searchText)
                Logger().debug("Search Response: $response")
                if(response.isSuccessful){
                    val quizCards = response.body()?.quizCards
                    setSearchResult(quizCards!!)
                }
                else{
                    _showToast.postValue("Search No Response")
                }
            }
            catch (e: Exception){
                Logger().debug("Search Failed: $e")
                _showToast.postValue("Search Failed")
            }
        }
    }

    fun setSearchResult(quizCards: List<QuizCard>){
        _searchResult.postValue(quizCards)
    }

    fun toastShown() {
        _showToast.value = null
    }
}
