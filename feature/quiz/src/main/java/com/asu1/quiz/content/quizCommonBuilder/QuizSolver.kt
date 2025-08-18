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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.models.quiz.QuizData
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.sampleQuizList
import com.asu1.quiz.ui.ImageColorBackground
import com.asu1.quiz.viewmodel.UserViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.utils.setTopBarColor

@Composable
fun QuizSolver(
    modifier: Modifier = Modifier,
    navController: NavController,
    hasVisitedRoute: Boolean
) {
    // 1) Collect state & prepare helpers
    val coordinatorVm: QuizCoordinatorViewModel = hiltViewModel()
    val quizState by coordinatorVm.quizUIState.collectAsStateWithLifecycle()
    val userVm: UserViewModel = hiltViewModel()

    // 2) Extract pieces of state
    val quizzes     = quizState.quizContentState.quizzes
    val quizTheme   = quizState.quizTheme
    val quizData    = quizState.quizGeneralUiState.quizData
    val viewModelState by coordinatorVm.quizViewModelState.collectAsStateWithLifecycle()

    // 3) Pager state
    val pagerState = rememberPagerState(
        initialPage = 0
    ) { quizzes.size + 1 }

    // 4) Navigation to scoring
    val navigateToScoreCard = remember(hasVisitedRoute) {
        {
            if (hasVisitedRoute) {
                SnackBarManager.showSnackBar(
                    R.string.can_not_proceed_when_creating_quiz,
                    ToastType.INFO
                )
            } else {
                val email = userVm.userData.value?.email ?: "GUEST"
                coordinatorVm.gradeQuiz(email) {
                    navController.navigate(Route.ScoringScreen) {
                        popUpTo(Route.Home) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    // 5) Side effects
    QuizSolverSideEffects(
        viewModelState = viewModelState,
        color         = quizTheme.colorScheme.primaryContainer,
        navController = navController
    )

    // 6) UI
    MaterialTheme(colorScheme = quizTheme.colorScheme) {
        QuizSolverScreen(
            modifier            = modifier,
            quizzes             = quizzes,
            pagerState          = pagerState,
            quizData            = quizData,
            quizTheme           = quizTheme,
            navigateToScoreCard = navigateToScoreCard
        )
    }
}

@Composable
private fun QuizSolverSideEffects(
    viewModelState: ViewModelState,
    color: Color,
    navController: NavController
) {
    val view = LocalView.current

    // Pop back on error
    LaunchedEffect(viewModelState) {
        if (viewModelState == ViewModelState.ERROR) {
            navController.popBackStack()
        }
    }

    // Update status bar / top bar color
    LaunchedEffect(color) {
        setTopBarColor(view = view, color = color)
    }
}

@Composable
private fun QuizSolverScreen(
    modifier: Modifier,
    quizzes: List<Quiz>,
    pagerState: PagerState,
    quizData: QuizData,
    quizTheme: QuizTheme,
    navigateToScoreCard: () -> Unit
) {
    Scaffold { padding ->
        Box(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // 1) Background
            ImageColorBackground(
                imageColor = quizTheme.backgroundImage,
                modifier   = Modifier.fillMaxSize()
            )

            // 2) Main content: loading or pager
            AnimatedContent(
                targetState = quizzes.isEmpty(),
                modifier    = Modifier.fillMaxSize(),
                label       = "QuizSolverLoadingToggle"
            ) { isEmpty ->
                if (isEmpty) {
                    LoadingAnimation(modifier = Modifier.fillMaxSize())
                } else {
                    QuizSolverPager(
                        pagerState          = pagerState,
                        quizzes             = quizzes,
                        quizData            = quizData,
                        onSubmit            = navigateToScoreCard,
                        modifier            = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizSolverPager(
    pagerState: PagerState,
    quizzes: List<Quiz>,
    quizData: QuizData,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    QuizViewerPager(
        pagerState  = pagerState,
        quizSize    = quizzes.size,
        quizzes     = quizzes,
        modifier    = modifier,
        lastElement = {
            QuizSubmit(
                title    = quizData.title,
                modifier = Modifier.fillMaxSize(),
                onSubmit = onSubmit
            )
        }
    )
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
    val pagerState = rememberPagerState(
        initialPage = 0,
    ){
        sampleQuizList.size
    }
    QuizSolverScreen(
        modifier = Modifier,
        quizzes = sampleQuizList,
        pagerState = pagerState,
        quizData = QuizData(),
        quizTheme = QuizTheme(),
        navigateToScoreCard = {},
    )
}

@Preview(showBackground = true)
@Composable
fun QuizSubmitPreView(){
    QuizSubmit()
}
