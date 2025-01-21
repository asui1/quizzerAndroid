package com.asu1.quizzer.screens.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.sampleQuiz1
import com.asu1.models.sampleQuiz2
import com.asu1.quizzer.composables.animations.LoadingAnimation
import com.asu1.quizzer.model.ImageColorBackground
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.quizzer.util.setTopBarColor
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import java.time.LocalDate

@Composable
fun QuizSolver(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    navigateToScoreCard: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val quizzes by quizLayoutViewModel.quizzes.collectAsStateWithLifecycle()
    val visibleQuizzes by quizLayoutViewModel.visibleQuizzes.collectAsStateWithLifecycle()
    val quizTheme by quizLayoutViewModel.quizTheme.collectAsStateWithLifecycle()
    val quizData by quizLayoutViewModel.quizData.collectAsStateWithLifecycle()
    val viewModelState by quizLayoutViewModel.viewModelState.observeAsState()
    val colorScheme = quizTheme.colorScheme
    val view = LocalView.current
    val pagerState = rememberPagerState(
        initialPage = 0,
    ){
        visibleQuizzes.size + 1
    }
    val textStyleManager by rememberUpdatedState(quizLayoutViewModel.getTextStyleManager())

    LaunchedEffect(Unit){
        quizLayoutViewModel.resetVisibleQuizzes()
        quizLayoutViewModel.loadMoreQuizzes()
    }

    LaunchedEffect(quizzes.size){
        quizLayoutViewModel.loadMoreQuizzes()
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

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect{page ->
                if(page == visibleQuizzes.size-1 && visibleQuizzes.size < quizzes.size){
                    quizLayoutViewModel.loadMoreQuizzes()
                }
            }
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
                    targetState = visibleQuizzes.isEmpty(),
                    modifier = Modifier.fillMaxSize(),
                    label = "Animation for Loading Quiz Solver",
                ) { isEmpty ->
                    if (isEmpty) {
                        LoadingAnimation()
                    } else {
                        QuizViewerPager(
                            pagerState = pagerState,
                            quizSize = quizzes.size,
                            visibleQuizzes = visibleQuizzes,
                            quizTheme = quizTheme,
                            textStyleManager = textStyleManager,
                            updateQuiz1 = { index, answerIndex ->
                                quizLayoutViewModel.updateQuiz1(
                                    index,
                                    answerIndex
                                )
                            },
                            updateQuiz2 = { index, date ->
                                quizLayoutViewModel.updateQuiz2(
                                    index,
                                    date
                                )
                            },
                            updateQuiz3 = { index, from, to ->
                                quizLayoutViewModel.updateQuiz3(
                                    index,
                                    from,
                                    to
                                )
                            },
                            updateQuiz4 = { index, from, to ->
                                quizLayoutViewModel.updateQuiz4(
                                    index,
                                    from,
                                    to
                                )
                            },
                            modifier = Modifier.fillMaxSize(),
                            isPreview = false,
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
    pagerState: PagerState,
    quizSize: Int,
    visibleQuizzes: List<Quiz>,
    quizTheme: QuizTheme = QuizTheme(),
    modifier: Modifier = Modifier,
    textStyleManager: TextStyleManager,
    updateQuiz1: (page: Int, index: Int) -> Unit,
    updateQuiz2: (page: Int, date: LocalDate) -> Unit,
    updateQuiz3: (page: Int, first: Int, second: Int) -> Unit,
    updateQuiz4: (page: Int, first: Int, second: Int?) -> Unit,
    lastElement: @Composable () -> Unit,
    isPreview: Boolean = false,
) {
    HorizontalPager(
        state = pagerState,
        key = { index -> if (index in visibleQuizzes.indices) visibleQuizzes[index].uuid else "Add NewQuiz" },
        modifier = modifier,
    ) { page ->
        if (page in visibleQuizzes.indices) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                QuizViewer(
                    quiz = visibleQuizzes[page],
                    quizTheme = quizTheme,
                    updateQuiz1 = { index ->
                        updateQuiz1(page, index)
                    },
                    updateQuiz2 = { date ->
                        updateQuiz2(page, date)
                    },
                    updateQuiz3 = { first, second ->
                        updateQuiz3(page, first, second)
                    },
                    updateQuiz4 = { first, second ->
                        updateQuiz4(page, first, second)
                    },
                    quizStyleManager = textStyleManager,
                    isPreview = isPreview
                )
                Text(
                    text = buildString {
                        append(visibleQuizzes[page].point)
                        append(stringResource(R.string.pt))
                    },
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.align(Alignment.TopEnd).padding(2.dp)
                )
                Text(
                    text = "${page + 1}/${quizSize}",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(2.dp)
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
            Text(text = stringResource(R.string.end_of_quiz_do_you_want_to_submit_your_answers),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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