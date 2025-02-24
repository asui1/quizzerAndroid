package com.asu1.quizzer.screens.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizcard.QuizCardHorizontalVerticalShareList
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.SearchViewModel
import com.asu1.resources.R
import com.asu1.utils.Logger

@Composable
fun  SearchScreen(navController: NavHostController,
                  searchViewModel: SearchViewModel = viewModel(),
                  onQuizClick: (quizId: String) -> Unit = {}) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember {mutableStateOf(false)}
    var searchTextField by remember { mutableStateOf(TextFieldValue(searchViewModel.searchText.value ?: "")) }
    val searchResult by searchViewModel.searchResult.collectAsStateWithLifecycle()
    val focusRequester = remember{ FocusRequester() }

    fun onBackPressed() {
        searchTextField = TextFieldValue("")
        searchViewModel.setSearchText("")
        focusRequester.requestFocus()
    }

    BackHandler(
        enabled = searchTextField.text.isNotEmpty(),
    ) {
        onBackPressed()
    }

    LaunchedEffect(Unit) {
        if(searchTextField.text.isEmpty()){
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                navController = navController,
                searchText = searchTextField,
                onSearchTextChanged = {textFieldValue ->
                    searchTextField = textFieldValue
                    searchViewModel.setSearchText(textFieldValue.text)
                },
                search = { searchViewModel.search(it) },
                focusManager = focusManager,
                onTextFieldFocused = { isFocused = true },
                onTextFieldUnfocused = { isFocused = false },
                onBackPressed = { onBackPressed() },
                focusRequester = focusRequester
            )
        },
        content = { paddingValues ->
            SearchScreenBody(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp),
                isFocused = isFocused,
                searchResult = searchResult,
                onQuizClick = onQuizClick
            )
        }
    )
}

@Composable
private fun SearchScreenBody(
    modifier: Modifier = Modifier,
    isFocused: Boolean,
    searchResult: List<QuizCard>?,
    onQuizClick: (quizId: String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isFocused || searchResult == null) {
            Text(
                text = stringResource(R.string.searching),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            if (searchResult.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_search_result),
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                QuizCardHorizontalVerticalShareList(
                    quizCards = searchResult,
                    onClick = {
                        onQuizClick(it)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewSearchScreenBody(){
    val quizCards = sampleQuizCardList
    com.asu1.resources.QuizzerAndroidTheme {
        SearchScreenBody(
            isFocused = false,
            searchResult = quizCards,
            onQuizClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreenTopBar(){
    com.asu1.resources.QuizzerAndroidTheme {
        SearchTopBar(
            navController = rememberNavController(),
            searchText = TextFieldValue(""),
            onSearchTextChanged = {},
            search = {},
            focusManager = LocalFocusManager.current,
            onTextFieldFocused = {},
            onTextFieldUnfocused = {},
            focusRequester = remember{ FocusRequester() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    navController: NavHostController,
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    search: (searchText: String) -> Unit,
    focusManager: FocusManager,
    onTextFieldFocused: () -> Unit,
    onTextFieldUnfocused: () -> Unit,
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
                                onTextFieldFocused()
                            } else {
                                onTextFieldUnfocused()
                            }
                        },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            Logger.debug("SearchScreen", "Search: ${searchText.text}")
                            search(searchText.text)
                            focusManager.clearFocus()
                        }
                    ),
                    trailingIcon = {
                        if (searchText.text.isNotEmpty()) {
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
                IconButton(onClick = { navController.popBackStack(
                    Route.Home,
                    inclusive = false
                ) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { search(searchText.text) }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }
    )
}