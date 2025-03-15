package com.asu1.quizzer.screens.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.asu1.quizcard.QuizCardHorizontalVerticalShareList
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.SearchViewModel
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.utils.Logger
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.map

@Composable
fun  SearchScreen(navController: NavHostController,
                  searchViewModel: SearchViewModel = viewModel(),
                  onQuizClick: (quizId: String) -> Unit = {}) {
    val searchText by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResult by searchViewModel.searchResult
        .map { it?.toPersistentList() }
        .collectAsStateWithLifecycle(null)
    val searchSuggestions by searchViewModel.filteredSearchSuggestions.collectAsStateWithLifecycle()


    SearchScreenBody(
        onMoveBackHome = {
            navController.popBackStack(
                Route.Home,
                inclusive = false
            )
        },
        searchText = searchText,
        onSearchTextChange = { searchText ->
            searchViewModel.setSearchText(searchText)
        },
        search = {searchText ->
            searchViewModel.search(searchText)
        },
        searchSuggestions = searchSuggestions,
        searchResult = searchResult,
        onQuizClick = onQuizClick,
    )
}

@Composable
fun SearchScreenBody(
    onMoveBackHome: () -> Unit = {},
    searchText: String,
    onSearchTextChange: (String) -> Unit = {},
    search: (String) -> Unit = {},
    searchSuggestions: List<String>,
    searchResult: PersistentList<QuizCard>?,
    onQuizClick: (String) -> Unit = {},
){
    val focusManager = LocalFocusManager.current
    var isFocused by remember {mutableStateOf(false)}
    val focusRequester = remember{ FocusRequester() }

    fun onBackPressed() {
        onSearchTextChange("")
        focusRequester.requestFocus()
    }

    BackHandler(
        enabled = searchText.isNotEmpty(),
    ) {
        onBackPressed()
    }

    LaunchedEffect(Unit) {
        if(searchText.isEmpty()){
            focusRequester.requestFocus()
        }
    }

    fun clearFocusAndSearch(text: String){
        focusManager.clearFocus()
        search(text)
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                onMoveBackHome = onMoveBackHome,
                searchText = searchText,
                onSearchTextChanged = {text ->
                    onSearchTextChange(text)
                },
                search = {
                    clearFocusAndSearch(it)
                },
                onFocusChange = {focusChange ->
                    isFocused = focusChange
                },
                focusManager = focusManager,
                onBackPressed = { onBackPressed() },
                focusRequester = focusRequester,
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (isFocused) {
                    true -> {
                        SearchScreenSuggestions(
                            suggestions = searchSuggestions,
                            onSuggestionClick = {suggestion ->
                                clearFocusAndSearch(suggestion)
                            }
                        )
                    }

                    false -> {
                        SearchScreenResults(
                            searchResult = searchResult,
                            onQuizClick = onQuizClick
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun SearchScreenSuggestions(
    modifier: Modifier = Modifier,
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit = {},
){
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(suggestions, key = {it -> it}) { suggestion ->
            SearchScreenSuggestionItem(
                modifier = Modifier.fillMaxWidth()
                    .clickable{
                        onSuggestionClick(suggestion)
                    },
                suggestion = suggestion,
            )
        }
    }
}

@Composable
private fun SearchScreenSuggestionItem(
    modifier: Modifier = Modifier,
    suggestion: String,
){
    Row(
        modifier = modifier.padding(start = 8.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search for $suggestion"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            suggestion,
            maxLines = 1,
            style = QuizzerTypographyDefaults.quizzerBodyMediumNormal,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchScreenSuggestionsPreview(){
    QuizzerAndroidTheme {
        SearchScreenSuggestions(
            suggestions = listOf("Test", "Faker", "Example", "Songs")
        )
    }
}

@Composable
private fun SearchScreenResults(
    searchResult: PersistentList<QuizCard>?,
    onQuizClick: (quizId: String) -> Unit
) {
    when {
        searchResult == null -> {
            Text(
                text = stringResource(R.string.searching),
                style = QuizzerTypographyDefaults.quizzerBodyMediumNormal,
                modifier = Modifier.padding(8.dp)
            )
        }
        searchResult.isEmpty() -> {
            Text(
                text = stringResource(R.string.no_search_result),
                style = QuizzerTypographyDefaults.quizzerBodyMediumNormal,
            )
        }
        else -> {
            QuizCardHorizontalVerticalShareList(
                quizCards = searchResult,
                onClick = {
                    onQuizClick(it)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreenBody(){
    val quizCards = sampleQuizCardList
    QuizzerAndroidTheme {
        SearchScreenResults(
            searchResult = quizCards.toPersistentList(),
            onQuizClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreenTopBar(){
    QuizzerAndroidTheme {
        SearchTopBar(
            searchText = "",
            onSearchTextChanged = {},
            search = {},
            focusManager = LocalFocusManager.current,
            focusRequester = remember{ FocusRequester() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    onMoveBackHome: () -> Unit = {},
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    search: (searchText: String) -> Unit,
    focusManager: FocusManager,
    onFocusChange: (Boolean) -> Unit = {},
    onBackPressed: () -> Unit = {},
    focusRequester: FocusRequester,
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = searchText,
                    shape = RoundedCornerShape(40.dp),
                    onValueChange = onSearchTextChanged,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                onFocusChange(true)
                            } else {
                                onFocusChange(false)
                            }
                        },
                    placeholder = { Text(text = stringResource(R.string.search) )},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            Logger.debug("SearchScreen", "Search: ${searchText}")
                            search(searchText)
                            focusManager.clearFocus()
                        }
                    ),
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = {
                                onBackPressed()
                            }) {
                                Icon(
                                    Icons.Default.RemoveCircleOutline,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onMoveBackHome
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { search(searchText) }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }
    )
}