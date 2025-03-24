package com.asu1.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.appdata.suggestion.SearchSuggestionRepository
import com.asu1.utils.LanguageSetter
import com.asu1.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchSuggestionRepository: SearchSuggestionRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredSearchSuggestions = searchQuery
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            searchSuggestionRepository.getFilteredSuggestions(query, LanguageSetter.lang)
                .catch { emit(emptyList()) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3000),
            emptyList()
        )

    private val _searchResult = MutableStateFlow<List<com.asu1.quizcardmodel.QuizCard>?>(null)
    val searchResult: StateFlow<List<com.asu1.quizcardmodel.QuizCard>?> get() = _searchResult.asStateFlow()


    fun setSearchText(searchText: String){
        _searchQuery.value = searchText
    }

    fun search(searchText: String){
        _searchQuery.value = searchText
        Logger.debug("searching for $searchText")
        viewModelScope.launch {
            _searchResult.value = emptyList()
            try {
                val response = com.asu1.network.RetrofitInstance.api.searchQuiz(searchText)
                if(response.isSuccessful){
                    val quizCards = response.body()?.searchResult
                    if(quizCards == null){
                        return@launch
                    }else{
                        _searchResult.value = quizCards
                    }
                }
            }
            catch (e: Exception){
                Logger.debug("search error", e)
            }
        }
    }
}