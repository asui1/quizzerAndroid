package com.asu1.quizzer.screens.quizlayout

import QuizCardHorizontal
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.composables.RowWithAppIconAndName
import com.asu1.quizzer.data.QuizDataSerializer
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.viewModels.QuizLoadViewModel
import com.asu1.quizzer.viewModels.QuizTheme

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.DialogComposable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoadItems(
    navController: NavController,
    quizLoadViewModel: QuizLoadViewModel = viewModel(),
    onClickLoad: (quizData: QuizDataSerializer, quizTheme: QuizTheme, scoreCard: ScoreCard) -> Unit = { quizData, quizTheme, scoreCard -> },
) {
    val quizList by quizLoadViewModel.quizList.collectAsState()
    val loadComplete by quizLoadViewModel.loadComplete.collectAsState()
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var deleteIndex by remember { mutableStateOf(-1) }
    val dismissStates = remember { mutableStateMapOf<String, DismissState>() }

    LaunchedEffect(loadComplete) {
        if (loadComplete) {
            navController.popBackStack()
            quizLoadViewModel.reset()
        }
    }

    Scaffold(
        topBar = {
            RowWithAppIconAndName(
                showBackButton = true,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (quizList == null) {
                Text(
                    "Searching for quizzes...",
                    style = MaterialTheme.typography.bodyMedium,
                )
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        Text(
                            text = "Load Quiz",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                    if (quizList!!.isEmpty()) {
                        item {
                            Text(
                                "No quizzes found",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    } else {
                        items(quizList!!.size) { index ->
                            val quizData = quizList!![index].quizData
                            val quizCard = QuizCard(
                                id = quizData.uuid,
                                title = quizData.title,
                                creator = quizData.creator,
                                tags = quizData.tags.toList(),
                                image = quizData.titleImage,
                                count = 0,
                            )
                            val currentDismissState = dismissStates.getOrPut(quizCard.id) {
                                rememberDismissState(
                                    confirmStateChange = {
                                        if (it == DismissValue.DismissedToStart) {
                                            deleteIndex = index
                                            showDialog = true
                                        }
                                        true
                                    }
                                )
                            }
                            SwipeToDismiss(
                                state = currentDismissState,
                                directions = setOf(DismissDirection.EndToStart),
                                background = {
                                    val color = when (currentDismissState.dismissDirection) {
                                        DismissDirection.EndToStart -> MaterialTheme.colorScheme.surfaceContainer
                                        else -> Color.Transparent
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color)
                                            .padding(16.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White
                                        )
                                    }
                                },
                                dismissContent = {
                                    QuizCardHorizontal(
                                        quizCard = quizCard,
                                        onClick = {
                                            val quizLayout = quizList!![index]
                                            onClickLoad(
                                                quizLayout.quizData,
                                                quizLayout.quizTheme,
                                                quizLayout.scoreCard
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        DialogComposable(
            titleResource = R.string.delete_save,
            messageResource = R.string.delete_save_body,
            onContinue = {
                scope.launch {
                    quizLoadViewModel.deleteLocalQuiz(deleteIndex)
                }
                showDialog = false
            },
            onContinueResource = R.string.delete,
            onCancel = { showDialog = false
                scope.launch {
                    dismissStates[quizList!![deleteIndex].quizData.uuid]?.reset()
                }
            },
            onCancelResource = R.string.cancel
        )
    }
}


fun getSampleQuizLoadViewModel(): QuizLoadViewModel{
    val quizLoadViewModel = QuizLoadViewModel()
    quizLoadViewModel.setTest()
    return quizLoadViewModel
}

@Preview(showBackground = true)
@Composable
fun loadItemsPreview(){
    val quizLoadViewModel = getSampleQuizLoadViewModel()
    LoadItems(
        navController = rememberNavController(),
        quizLoadViewModel = quizLoadViewModel
    )
}
