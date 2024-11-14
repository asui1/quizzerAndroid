package com.asu1.quizzer.screens

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.model.asBackgroundModifier
import com.asu1.quizzer.model.sampleQuiz1
import com.asu1.quizzer.model.sampleQuiz2
import com.asu1.quizzer.screens.quiz.QuizViewer
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.viewModels.QuizLayoutViewModel

@Composable
fun QuizSolver(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    initIndex: Int = 0,
    navigateToScoreCard: () -> Unit = {},
) {
    val quizzes by quizLayoutViewModel.quizzes.collectAsState()
    val quizTheme by quizLayoutViewModel.quizTheme.collectAsState()
    val quizData by quizLayoutViewModel.quizData.collectAsState()
    val snapLayoutInfoProvider = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(snapLayoutInfoProvider)
    val colorScheme = quizTheme.colorScheme

    LaunchedEffect(initIndex) {
        snapLayoutInfoProvider.scrollToItem(initIndex)
    }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .asBackgroundModifier(
                    quizTheme.backgroundImage,
                )
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                flingBehavior = snapFlingBehavior,
                state = snapLayoutInfoProvider,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(quizzes.size) {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                    ) {
                        Logger().debug("Trigger RECOMPOSE")
                        QuizViewer(
                            quiz = quizzes[it],
                            quizTheme = quizTheme,
                            updateQuiz1 = { index ->
                                quizLayoutViewModel.updateQuiz1(it, index)
                            },
                            updateQuiz2 = { date ->
                                quizLayoutViewModel.updateQuiz2(it, date)
                            },
                            updateQuiz3 = { first, second ->
                                quizLayoutViewModel.updateQuiz3(it, first, second)
                            },
                            updateQuiz4 = { first, second ->
                                quizLayoutViewModel.updateQuiz4(it, first, second)
                            }
                        )
                        Text(
                            text = "${quizzes[it].point} points",
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                        Text(
                            text = "${it + 1}/${quizzes.size}",
                            modifier = Modifier.align(Alignment.BottomEnd)
                        )
                    }
                }
                item {
                    QuizSubmit(
                        title = quizData.title,
                        modifier = Modifier.fillParentMaxSize(),
                        onSubmit = {
                            navigateToScoreCard()
                        }
                    )
                }
            }
        }
    }
}

//ANSWER SCREEN, Button to Move to Scoring Screen
@Composable
fun QuizSubmit(
    title: String = "Title",
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit = {}
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "End of Quiz.\nDo you want to submit your answers?",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onSubmit) {
                Text(text = "Submit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizSolverPreview() {
    val quizLayoutViewModel: QuizLayoutViewModel = viewModel()
    quizLayoutViewModel.addQuiz(sampleQuiz1, null)
    quizLayoutViewModel.addQuiz(sampleQuiz2, null)
    QuizSolver(
        navController = rememberNavController(),
        quizLayoutViewModel = quizLayoutViewModel,
    )
}

@Preview(showBackground = true)
@Composable
fun QuizSubmitPreView(){
    QuizSubmit()
}