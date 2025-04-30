package com.asu1.quiz.content.quizCommonBuilder

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.models.quizRefactor.Quiz
import com.asu1.quiz.ui.ImageColorBackground
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.utils.setTopBarColor

@Composable
fun QuizSolver(
    modifier: Modifier = Modifier,
    navController: NavController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel(),
    navigateToScoreCard: () -> Unit = {},
) {
    val quizState by quizCoordinatorViewModel.quizUIState.collectAsStateWithLifecycle()
    val quizzes = quizState.quizContentState.quizzes
    val quizTheme = quizState.quizTheme
    val quizData = quizState.quizGeneralUiState.quizData
    val viewModelState by quizCoordinatorViewModel.quizViewModelState.collectAsStateWithLifecycle()
    val colorScheme = quizTheme.colorScheme
    val view = LocalView.current
    val pagerState = rememberPagerState(
        initialPage = 0,
    ){
        quizzes.size + 1
    }

    LaunchedEffect(viewModelState) {
        if(viewModelState == ViewModelState.ERROR){
            navController.popBackStack()
        }
    }

    LaunchedEffect(colorScheme.primaryContainer) {
        setTopBarColor(
            view = view,
            color = colorScheme.primaryContainer
        )
    }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Scaffold { paddingValues ->
            Box(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                ImageColorBackground(
                    imageColor = quizTheme.backgroundImage,
                    modifier = Modifier.fillMaxSize()
                )
                AnimatedContent(
                    targetState = quizzes.isEmpty(),
                    modifier = Modifier.fillMaxSize(),
                    label = "Animation for Loading Quiz Solver",
                ) { isEmpty ->
                    if (isEmpty) {
                        LoadingAnimation()
                    } else {
                        QuizViewerPager(
                            pagerState = pagerState,
                            quizSize = quizzes.size,
                            quizzes = quizzes,
                            modifier = Modifier.fillMaxSize(),
                            lastElement = {
                                QuizSubmit(
                                    title = quizData.title,
                                    modifier = Modifier.fillMaxSize(),
                                    onSubmit = navigateToScoreCard
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizViewerPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    quizSize: Int,
    quizzes: List<Quiz>,
    lastElement: @Composable () -> Unit,
) {
    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1)
    )
    HorizontalPager(
        state = pagerState,
        key = { index -> if (index in quizzes.indices) quizzes[index].uuid else "Add NewQuiz" },
        modifier = modifier,
        beyondViewportPageCount = PagerDefaults.BeyondViewportPageCount,
        flingBehavior = flingBehavior,
    ) { page ->
        if (page in quizzes.indices) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                QuizViewer(
                    quiz = quizzes[page]
                )
                Text(
                    text = "${page + 1}/${quizSize}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)
                )
            }
        } else {
            lastElement()
        }
    }
}

//ANSWER SCREEN, Button to Move to Scoring Screen
@Composable
fun QuizSubmit(
    modifier: Modifier = Modifier,
    title: String = "Title",
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
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = stringResource(R.string.end_of_quiz_do_you_want_to_submit_your_answers),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onSubmit) {
                Text(text = stringResource(R.string.submit))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizSolverPreview() {
}

@Preview(showBackground = true)
@Composable
fun QuizSubmitPreView(){
    QuizSubmit()
}