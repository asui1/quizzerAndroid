package com.asu1.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.activityNavigation.Route
import com.asu1.quizcard.cardBase.QuizCardHorizontalVerticalShareList
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun  SearchScreen(navController: NavController,
                  onQuizClick: (quizId: String) -> Unit = {}) {
    val searchViewModel: SearchViewModel = viewModel()
    val searchText by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResult by searchViewModel.searchResult
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
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val clearFocusAndSearch = remember(search, focusManager) { { text: String ->
        focusManager.clearFocus()
        search(text)
    } }

    val onBackPressed = remember(onSearchTextChange, focusRequester) {
        {
            focusRequester.requestFocus()
            onSearchTextChange("")
        }
    }

    BackHandler(enabled = searchText.isNotEmpty()) { onBackPressed() }

    LaunchedEffect(searchText.isEmpty()) {
        if (searchText.isEmpty()) focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                onMoveBackHome = onMoveBackHome,
                searchText = searchText,
                onSearchTextChanged = onSearchTextChange,
                search = clearFocusAndSearch,
                onFocusChange = { isFocused = it },
                focusManager = focusManager,
                onBackPressed = onBackPressed,
                focusRequester = focusRequester,
            )
        }
    ) { padding ->
        SearchContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp),
            isFocused = isFocused,
            suggestions = searchSuggestions,
            onSuggestionClick = clearFocusAndSearch,
            results = searchResult,
            onQuizClick = onQuizClick
        )
    }
}

/* -------- content switch -------- */

@Composable
private fun SearchContent(
    modifier: Modifier,
    isFocused: Boolean,
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    results: PersistentList<QuizCard>?,
    onQuizClick: (String) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isFocused) {
            SearchScreenSuggestions(
                suggestions = suggestions,
                onSuggestionClick = onSuggestionClick
            )
        } else {
            SearchScreenResults(
                searchResult = results,
                onQuizClick = onQuizClick
            )
        }
    }
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
        items(suggestions, key = {suggestion -> suggestion}) { suggestion ->
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
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search for $suggestion",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            suggestion,
            maxLines = 1,
            style = QuizzerTypographyDefaults.quizzerBodyMediumBold,
            fontSize = 18.sp,
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
    search: (String) -> Unit,
    focusManager: FocusManager,
    onFocusChange: (Boolean) -> Unit = {},
    onBackPressed: () -> Unit = {},
    focusRequester: FocusRequester,
) {
    val onPerformSearch = remember(searchText, search, focusManager) {
        { searchTextNonNull: String ->
            search(searchTextNonNull)
            focusManager.clearFocus()
        }
    }

    TopAppBar(
        title = {
            SearchField(
                value = searchText,
                onValueChange = onSearchTextChanged,
                onFocusChange = onFocusChange,
                onSearch = { onPerformSearch(searchText) },
                onClear = onBackPressed,
                focusRequester = focusRequester
            )
        },
        navigationIcon = { BackButton(onClick = onMoveBackHome) },
        actions = { SearchAction { onPerformSearch(searchText) } }
    )
}

/* ---------- pieces ---------- */

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
) {
    val tfColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
        modifier = Modifier
            .focusRequester(focusRequester)
            .height(55.dp)
            .onFocusChanged { onFocusChange(it.isFocused) },
        placeholder = { Text(stringResource(R.string.search), fontSize = 14.sp) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        colors = tfColors
    )
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
    }
}

@Composable
private fun SearchAction(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Default.Search, contentDescription = "Search")
    }
}
