package com.asu1.quizzer.screens.quiz

import ToastManager
import ToastType
import android.app.Activity
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.animations.LoadingAnimation
import com.asu1.quizzer.composables.scorecard.ScoreCardComposable
import com.asu1.quizzer.composables.scorecard.ShareDialog
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.model.sampleScoreCard
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.disableImmersiveMode
import com.asu1.quizzer.util.enableImmersiveMode
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel

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
    val scoreCard by scoreCardViewModel.scoreCard.collectAsStateWithLifecycle()
    val quizLayoutViewModelState by quizLayoutViewModel.viewModelState.observeAsState()
    var showShareBottomSheet by remember{ mutableStateOf(false) }
    var immerseMode by remember { mutableStateOf(false) }
    val context = LocalContext.current as Activity

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
                                        context.disableImmersiveMode()
                                    }else{
                                        context.enableImmersiveMode()
                                    }
                                    immerseMode = !immerseMode
                                }
                        ) {
                            ScoreCardComposable(
                                scoreCard = scoreCard,
                                quizResult = quizResult!!,
                                modifier = Modifier.fillMaxWidth().weight(1f)
                            )
                            if(!immerseMode){
                                Spacer(modifier = Modifier.height(8.dp))
                                ScoringScreenBottomRow(
                                    scoreCard,
                                    loadQuiz,
                                    onClickMoveHome = {
                                        quizLayoutViewModel.resetQuizLayout()
                                        scoreCardViewModel.resetScoreCard()
                                        navController.navigate(Route.Home) {
                                            launchSingleTop = true
                                            popUpTo(Route.Home) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    showShareBottomSheet = { showShareBottomSheet = true }
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
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
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
                .width(150.dp)
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