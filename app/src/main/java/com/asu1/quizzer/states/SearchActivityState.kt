package com.asu1.quizzer.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.viewModels.MainViewModel
import com.asu1.quizzer.viewModels.SearchViewModel

data class SearchActivityState(
    val searchResult: State<List<QuizCard>?>,
    val search: (String) -> Unit,
    val reset: () -> Unit = {},
)

@Composable
fun rememberSearchActivityState(
    searchViewModel: SearchViewModel = viewModel(),
): SearchActivityState {
    val searchResult by searchViewModel.searchResult.observeAsState(initial = null)

    return SearchActivityState(
        searchResult = rememberUpdatedState(searchResult),
        search = { searchText -> searchViewModel.search(searchText) },
        reset = { searchViewModel.reset() },
    )
}