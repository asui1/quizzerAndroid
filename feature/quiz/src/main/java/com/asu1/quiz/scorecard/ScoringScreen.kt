package com.asu1.quiz.scorecard

import SnackBarManager
import ToastType
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.customComposable.dialog.ShareDialog
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.uiUtil.disableImmersiveMode
import com.asu1.customComposable.uiUtil.enableImmersiveMode
import com.asu1.resources.R
import com.asu1.resources.ViewModelState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoringScreen(
    navController: NavController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel(),
    email: String = "GUEST",
    loadQuiz: (String) -> Unit = {},
) {
    val quizState by quizCoordinatorViewModel.quizUIState.collectAsStateWithLifecycle()
    val quizzes = quizState.quizContentState.quizzes
    val quizResult = quizState.quizResult
    val scoreCard = quizState.scoreCardState.scoreCard
    val quizViewModelState by quizCoordinatorViewModel.quizViewModelState.collectAsStateWithLifecycle()
    var showShareBottomSheet by remember{ mutableStateOf(false) }
    var immerseMode by remember { mutableStateOf(false) }
    val localActivity = LocalActivity.current
    var movingToQuizChecker = false
    val quizQuestions = remember(Unit){quizCoordinatorViewModel.getQuestions()}

    LaunchedEffect(quizViewModelState){
        if(quizViewModelState == ViewModelState.ERROR){
            navController.popBackStack(Route.Home, inclusive = false)
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            localActivity?.disableImmersiveMode()
            if(!movingToQuizChecker){
                quizCoordinatorViewModel.updateQuizCoordinator(
                    QuizCoordinatorActions.ResetQuizResult
                )
            }
        }
    }

    AnimatedContent(
        targetState = quizViewModelState,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "Design Scorecard",

        ) { targetState ->
        when(targetState) {
            ViewModelState.LOADING -> {
                LoadingAnimation()
            }
            else -> {
                if(scoreCard.quizUuid != null)
                    MaterialTheme(
                        colorScheme = scoreCard.colorScheme
                    ) {
                        if(showShareBottomSheet) {
                            ModalBottomSheet(onDismissRequest = {showShareBottomSheet = false },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.imePadding()
                            ) {
                                ShareDialog(
                                    quizId = scoreCard.quizUuid ?: "",
                                    userName = email,
                                    onDismiss = {
                                        showShareBottomSheet = false
                                    }
                                )
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    if(immerseMode) {
                                        localActivity?.disableImmersiveMode()
                                    }else{
                                        localActivity?.enableImmersiveMode()
                                    }
                                    immerseMode = !immerseMode
                                }
                        ) {
                            ScoreCardComposable(
                                scoreCard = scoreCard,
                                quizResult = quizResult!!,
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                quizQuestions = quizQuestions,
                            )
                            if(!immerseMode){
                                Spacer(modifier = Modifier.height(8.dp))
                                ScoringScreenBottomRow(
                                    scoreCard,
                                    loadQuiz,
                                    onClickMoveHome = {
                                        quizCoordinatorViewModel.updateQuizCoordinator(
                                            QuizCoordinatorActions.ResetQuiz
                                        )
                                        navController.navigate(Route.Home) {
                                            launchSingleTop = true
                                            popUpTo(Route.Home) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    showShareBottomSheet = { showShareBottomSheet = true },
                                    isResultView = quizzes.isEmpty(),
                                    navigateToQuizChecker = {
                                        movingToQuizChecker = true
                                        navController.navigate(Route.QuizChecker){
                                            launchSingleTop = true
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
            }
        }
    }
}

@Composable
private fun ScoringScreenBottomRow(
    scoreCard: ScoreCard,
    loadQuiz: (String) -> Unit,
    onClickMoveHome: () -> Unit,
    showShareBottomSheet: () -> Unit,
    isResultView: Boolean = false,
    navigateToQuizChecker: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        if(isResultView){
            Button(
                onClick = {
                    if (scoreCard.quizUuid != null) {
                        loadQuiz(scoreCard.quizUuid!!)
                    } else {
                        SnackBarManager.showSnackBar(
                            message = R.string.can_not_load_quiz,
                            type = ToastType.ERROR,
                        )
                    }
                },
                modifier = Modifier
                    .height(height = 36.dp)
                    .width(150.dp)
            ) {
                Text(
                    text = stringResource(R.string.solve_again),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }else{
            Button(
                onClick = {
                    navigateToQuizChecker()
                },
                modifier = Modifier
                    .height(height = 36.dp)
                    .width(150.dp)
            ) {
                Text(
                    text = stringResource(R.string.check_answer),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Button(
            onClick = {
                onClickMoveHome()
            },
            modifier = Modifier
                .height(height = 36.dp)
                .width(150.dp)
        ) {
            Text(
                text = stringResource(R.string.move_home),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        IconButton(onClick = {
            showShareBottomSheet()
        }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScoringScreenBottomPreview() {
    val scoreCard = sampleScoreCard
    ScoringScreenBottomRow(
        scoreCard = scoreCard,
        loadQuiz = {},
        onClickMoveHome = {},
        showShareBottomSheet = {}
    )
}