package com.asu1.quizzer.screens.mainScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TopAppBarDefaults
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
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.quizcards.QuizCardHorizontalList
import com.asu1.quizzer.composables.quizcards.QuizCardHorizontalVerticalShareList
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.getSampleQuizCard
import com.asu1.quizzer.model.getSampleQuizCardList
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.SearchViewModel

@Composable
fun  SearchScreen(navController: NavHostController, searchViewModel: SearchViewModel = viewModel(),
                 onQuizClick: (quizId: String) -> Unit = {}, searchText: String = "") {
    val focusManager = LocalFocusManager.current
    var isFocused by remember {mutableStateOf(false)}
    var searchTextField by remember {mutableStateOf(TextFieldValue(searchText))}
    val searchResult by searchViewModel.searchResult.collectAsStateWithLifecycle()
    val focusRequester = remember{ FocusRequester() }

    LaunchedEffect(Unit) {
        if(searchText.isNotEmpty()){
            searchViewModel.search(searchText)
        }
        else{
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                navController = navController,
                searchText = searchTextField,
                onSearchTextChanged = { searchTextField = it },
                search = { searchViewModel.search(it) },
                focusManager = focusManager,
                onTextFieldFocused = { isFocused = true },
                onTextFieldUnfocused = { isFocused = false },
                focusRequester = focusRequester
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

                if (isFocused || searchResult == null) {
                    Text(
                        text = stringResource(R.string.searching),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } else {
                    if (searchResult?.isEmpty() != false) {
                        Text(
                            text = stringResource(R.string.no_search_result),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    } else {
                        QuizCardHorizontalVerticalShareList(
                            quizCards = searchResult ?: emptyList(),
                            onClick = {
                                searchViewModel.reset()
                                onQuizClick(it)
                            }
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewSearchScreen(){
    val searchViewModel: SearchViewModel = viewModel()
    val quizCards = getSampleQuizCardList()
    searchViewModel.setSearchResult(
        quizCards
    )

    QuizzerAndroidTheme {
        SearchScreen(
            navController = rememberNavController(),
            searchViewModel = searchViewModel
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
    focusRequester: FocusRequester,
) {
    TopAppBar(
        modifier = Modifier.height(80.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    value = searchText,
                    shape = RoundedCornerShape(50.dp),
                    onValueChange = onSearchTextChanged,
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(50.dp))
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
                            search(searchText.text)
                            focusManager.clearFocus()
                        }
                    ),
                    trailingIcon = {
                        if (searchText.text.isNotEmpty()) {
                            IconButton(onClick = { onSearchTextChanged(TextFieldValue("")) }) {
                                Icon(
                                    Icons.Default.RemoveCircleOutline,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    }
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
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