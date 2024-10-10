package com.asu1.quizzer.screens

import QuizCardHorizontalList
import android.app.Activity
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.SearchViewModel
import loadImageAsByteArray

@Composable
fun SearchScreen(navController: NavHostController, searchViewModel: SearchViewModel = viewModel()) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember {mutableStateOf(false)}
    var searchText by remember {mutableStateOf(TextFieldValue(""))}
    val searchResult by searchViewModel.searchResult.observeAsState()

//    LaunchedEffect(Unit){
//        searchViewModel.reset()
//    }

    Scaffold(
        topBar = {
            SearchTopBar(
                searchText = searchText,
                onSearchTextChanged = { searchText = it },
                search = { searchViewModel.search(it) },
                focusManager = focusManager,
                onTextFieldFocused = { isFocused = true },
                onTextFieldUnfocused = { isFocused = false }
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
                        text = "Searching...",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } else {
                    if (searchResult!!.isEmpty()) {
                        Text(
                            text = "No search result",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    } else {
                        QuizCardHorizontalList(
                            quizCards = searchResult!!,
                            onClick = { /* Handle click */ }
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
    val context = LocalContext.current
    val imageByte = loadImageAsByteArray(context, R.drawable.question2)
    val searchViewModel: SearchViewModel = viewModel()
    val quizCard = QuizCard(
        id = "1",
        title = "Quiz 1",
        tags = listOf("tag1", "tag2"),
        creator = "Creator",
        image = imageByte,
        count = 0
    )
    searchViewModel.setSearchResult(
        listOf(quizCard, quizCard, quizCard, quizCard, quizCard)
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
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    search: (searchText: String) -> Unit,
    focusManager: FocusManager,
    onTextFieldFocused: () -> Unit,
    onTextFieldUnfocused: () -> Unit,
) {
    val context = LocalContext.current

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
                IconButton(onClick = { (context as? Activity)?.finish() }) {
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