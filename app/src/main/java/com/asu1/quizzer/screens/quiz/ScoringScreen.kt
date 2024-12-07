package com.asu1.quizzer.screens.quiz

import ToastManager
import ToastType
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.animations.LoadingAnimation
import com.asu1.quizzer.composables.scorecard.ScoreCardComposable
import com.asu1.quizzer.composables.scorecard.ShareDialog
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.createSampleQuizLayoutViewModel
import com.asu1.quizzer.viewModels.createSampleScoreCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoringScreen(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    scoreCardViewModel: ScoreCardViewModel = viewModel(),
    email: String = "GUEST",
    loadQuiz: (String) -> Unit = {},
) {
    val quizResult by quizLayoutViewModel.quizResult.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = (configuration.screenWidthDp.dp).coerceAtMost(screenHeight/2)
    val scoreCard by scoreCardViewModel.scoreCard.collectAsStateWithLifecycle()
    val quizLayoutViewModelState by quizLayoutViewModel.viewModelState.observeAsState()
    var showShareBottomSheet by remember{ mutableStateOf(false) }

    LaunchedEffect(quizLayoutViewModelState){
        if(quizLayoutViewModelState == ViewModelState.ERROR){
            quizLayoutViewModel.resetViewModelState()
            navController.popBackStack(Route.Home, inclusive = false)
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            scoreCardViewModel.resetScoreCard()
            quizLayoutViewModel.resetQuizResult()
        }
    }


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
        ) {
            AnimatedContent(
                targetState = quizLayoutViewModelState,
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
                            ScoreCardComposable(
                                scoreCard = scoreCard,
                                quizResult = quizResult!!
                            )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.width(screenWidth * 0.9f)
            ) {
                Button(
                    onClick = {
                        if (scoreCard.quizUuid != null) {
                            loadQuiz(scoreCard.quizUuid!!)
                        } else {
                            ToastManager.showToast(
                                message = R.string.can_not_load_quiz,
                                type = ToastType.ERROR,
                            )
                        }
                    },
                    modifier = Modifier
                        .height(height = 36.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.solve_again),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Button(
                    onClick = {
                        NavMultiClickPreventer.navigate(navController, Route.Home) {
                            popUpTo(Route.Home) {
                                inclusive = true
                            }
                        }
                        quizLayoutViewModel.resetQuizLayout()
                        scoreCardViewModel.resetScoreCard()
                    },
                    modifier = Modifier
                        .height(height = 36.dp)
                        .weight(2f)
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
                    showShareBottomSheet = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScoringScreenPreview() {
    val quizLayoutViewModel = createSampleQuizLayoutViewModel()
    val scoreCardViewModel = createSampleScoreCardViewModel()
    ScoringScreen(
        navController = rememberNavController(),
        quizLayoutViewModel = quizLayoutViewModel,
        scoreCardViewModel = scoreCardViewModel,
    )
}