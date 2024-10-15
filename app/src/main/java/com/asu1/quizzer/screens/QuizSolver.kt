package com.asu1.quizzer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.model.sampleQuiz1
import com.asu1.quizzer.model.sampleQuiz2
import com.asu1.quizzer.screens.quiz.QuizViewer
import com.asu1.quizzer.viewModels.QuizLayoutViewModel

@Composable
fun QuizSolver(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    initIndex: Int = 0,
) {
    val quizzes by quizLayoutViewModel.quizzes.observeAsState(emptyList())
    val quizTheme by quizLayoutViewModel.quizTheme.collectAsState()
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
                .then(quizTheme.backgroundImage.asBackgroundModifier())
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
                        QuizViewer(
                            quiz = quizzes[it],
                            quizTheme = quizTheme,
                            updateUserInput = { newQuiz ->
                                quizLayoutViewModel.updateUserAnswer(newQuiz)
                            },
                            isPreview = false,
                        )
                    }
                }
                item {
                    //ANSWER SCREEN, Button to Move to Scoring Screen
                }
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