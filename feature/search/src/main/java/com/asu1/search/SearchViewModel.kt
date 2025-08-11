package com.asu1.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.appdata.suggestion.SearchSuggestionRepository
import com.asu1.network.RetrofitInstance
import com.asu1.network.runApi
import com.asu1.quizcardmodel.QuizCard
import com.asu1.utils.LanguageSetter
import com.asu1.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.asu1.resources.R

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

    private val _searchResult = MutableStateFlow<List<QuizCard>?>(null)
    val searchResult: Flow<PersistentList<QuizCard>> =
        _searchResult.map { it?.toPersistentList() ?: persistentListOf() }


    fun setSearchText(searchText: String){
        _searchQuery.value = searchText
    }

    fun search(searchText: String) {
        _searchQuery.value = searchText
        Logger.debug("searching for $searchText")

        if (searchText.isBlank()) {
            _searchResult.value = emptyList()
            return
        }

        viewModelScope.launch {
            _searchResult.value = emptyList() // 필요 시 로딩 상태 플래그로 교체
            runApi { RetrofitInstance.api.searchQuiz(searchText) }
                .onSuccess { response ->
                    if (response.isSuccessful) {
                        _searchResult.value = response.body()?.searchResult.orEmpty()
                    } else {
                        val err = response.errorBody()?.string()
                        Logger.debug("search failure: $err")
                        SnackBarManager.showSnackBar(R.string.search_failed, ToastType.ERROR)
                    }
                }
                .onFailure { e ->
                    Logger.debug("search error ${e.message}")
                    SnackBarManager.showSnackBar(R.string.search_failed, ToastType.ERROR)
                }
        }
    }
}
