package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.LazyColumnSwipeToDismissDialog
import com.asu1.quizzer.composables.LazyColumnWithSwipeToDismiss
import com.asu1.quizzer.composables.RowWithAppIconAndName
import com.asu1.quizzer.data.QuizDataSerializer
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.viewModels.QuizLoadViewModel
import com.asu1.quizzer.viewModels.QuizTheme
import kotlinx.coroutines.launch
import java.util.Base64

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoadItems(
    navController: NavController,
    quizLoadViewModel: QuizLoadViewModel = viewModel(),
    onClickLoad: (quizData: QuizDataSerializer, quizTheme: QuizTheme, scoreCard: ScoreCard) -> Unit = { quizData, quizTheme, scoreCard -> },
) {
    val quizSerializerList by quizLoadViewModel.quizList.collectAsStateWithLifecycle()
    val quizList = quizSerializerList?.map{
        QuizCard(
            id = it.quizData.uuid,
            title = it.quizData.title,
            creator = it.quizData.creator,
            tags = it.quizData.tags.toList(),
            image = Base64.getDecoder().decode(it.quizData.titleImage),
            count = 0,
        )
    } ?: emptyList()
    val loadComplete by quizLoadViewModel.loadComplete.collectAsStateWithLifecycle()
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
            if (quizSerializerList == null) {
                Text(
                    stringResource(R.string.searching_for_quizzes),
                    style = MaterialTheme.typography.bodyMedium,
                )
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumnWithSwipeToDismiss(
                    quizList = quizList,
                    onLoadQuiz = { index ->
                        val quizLayout = quizSerializerList!![index]
                        onClickLoad(
                            quizLayout.quizData,
                            quizLayout.quizTheme,
                            quizLayout.scoreCard
                        )
                    },
                    getOrPutDismiss = { id, index->
                        dismissStates.getOrPut(id) {
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
                    }
                )
            }
        }
    }

    if (showDialog) {
        LazyColumnSwipeToDismissDialog(
            quizList = quizList,
            deleteIndex = deleteIndex,
            onDelete = {index ->
                quizLoadViewModel.deleteLocalQuiz(deleteIndex)
            },
            updateDialog = { showDialog = it },
            resetDismiss = { uuid ->
                scope.launch {
                    dismissStates[uuid]?.reset()
                }
            }
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
