package com.asu1.quiz.scorecard

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.animations.OpenCloseColumn
import com.asu1.customComposable.animations.UploadingAnimation
import com.asu1.customComposable.uiUtil.disableImmersiveMode
import com.asu1.customComposable.uiUtil.enableImmersiveMode
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.quiz.viewmodel.LoadMyQuizViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModel
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

val colorNames: List<Int> = listOf(R.string.background_newline,
    R.string.effect, R.string.gradient)

@Composable
fun DesignScoreCardScreen(
    navController: NavController
) {
    // 1️⃣ Gather all your state & callbacks
    val coordinatorVm: QuizCoordinatorViewModel   = viewModel()
    val loadVm: LoadMyQuizViewModel = viewModel()
    val scoreVm:         ScoreCardViewModel       = viewModel()
    val uiState by       coordinatorVm.quizUIState.collectAsStateWithLifecycle()
    val scoreCardState   = uiState.scoreCardState
    val viewModelState by coordinatorVm.quizViewModelState.collectAsStateWithLifecycle()
    val quizQuestions    = remember { coordinatorVm.getQuestions() }
    val contextActivity  = LocalActivity.current
    val scope            = rememberCoroutineScope()

    // 2️⃣ Side-effects (immersive cleanup, reset on upload finish)
    DesignScoreCardSideEffects(
        viewModelState = viewModelState,
        activity        = contextActivity,
        onUploadFinish = {
            navController.popBackStack(Route.Home, false)
            loadVm.reset()
            scoreVm.resetScoreCard()
        }
    )

    // 3️⃣ UI
    MaterialTheme(colorScheme = scoreCardState.scoreCard.colorScheme) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 3a) Show upload‐in‐progress dialog if needed
            DesignScoreCardUploadDialog(viewModelState)

            // 3b) Content with immersive toggle on tap
            ImmersiveToggleBox(activity = contextActivity) {
                DesignScoreCardContent(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    scoreCard    = scoreCardState.scoreCard,
                    questions    = quizQuestions,
                    onUpload     = {
                        scope.launch {
                            coordinatorVm.tryUpload(navController) { /* handled in side-effect */ }
                        }
                    },
                    removeBackground = scoreCardState.removeOverLayImageBackground,
                )
            }
        }
    }
}

@Composable
private fun DesignScoreCardSideEffects(
    viewModelState: ViewModelState,
    activity: Activity?,
    onUploadFinish: () -> Unit
) {
    // Pop back / reset when upload completes
    LaunchedEffect(viewModelState) {
        if (viewModelState == ViewModelState.SUCCESS) {
            onUploadFinish()
        }
    }

    // Always disable immersive mode when leaving
    DisposableEffect(Unit) {
        onDispose {
            activity?.disableImmersiveMode()
        }
    }
}

@Composable
private fun DesignScoreCardUploadDialog(
    viewModelState: ViewModelState
) {
    if (viewModelState == ViewModelState.UPLOADING) {
        Dialog(onDismissRequest = {}) {
            Box(Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                UploadingAnimation()
            }
        }
    }
}

@Composable
private fun ImmersiveToggleBox(
    activity: Activity?,
    content: @Composable () -> Unit
) {
    var immerseMode by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (immerseMode) activity?.disableImmersiveMode()
                else             activity?.enableImmersiveMode()
                immerseMode = !immerseMode
            }
    ) {
        content()
    }
}

@Composable
private fun DesignScoreCardContent(
    modifier: Modifier,
    scoreCard: ScoreCard,
    questions: PersistentList<String>,
    onUpload:  () -> Unit,
    removeBackground: Boolean,
) {
    // screen dimensions for the tools panel
    val windowInfo = LocalWindowInfo.current
    val density    = LocalDensity.current
    val height     = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.height.toDp() }
    }
    val width      = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.width.toDp() }
            .coerceAtMost(height * 0.6f)
    }
    val quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel()

    // content + slide-out tools
    OpenCloseColumn(
        isOpen      = true,
        onToggleOpen= {},
        height      = height,
        openWidth   = 80.dp,
        modifier    = modifier
    ) {
        DesignScoreCardTools(
            scoreCard      = scoreCard,
            screenWidth    = width,
            screenHeight   = height,
            updateQuizCoordinate = { action ->
                quizCoordinatorViewModel.updateQuizCoordinator(action)
            },
            removeBackground  = removeBackground
        )
    }

    DesignScoreCardBody(
        scoreCard   = scoreCard,
        immerseMode = false,
        onUpload    = onUpload,
        questions   = questions
    )
}

@Composable
private fun DesignScoreCardBody(
    scoreCard: ScoreCard,
    immerseMode: Boolean,
    onUpload: () -> Unit = { },
    questions: PersistentList<String> = persistentListOf()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ) {
        ScoreCardComposable(
            scoreCard = scoreCard,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .animateContentSize(),
            quizQuestions = questions,
        )
        if (!immerseMode) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = {
                        onUpload()
                    },
                    modifier = Modifier
                        .size(width = 200.dp, height = 48.dp)
                        .padding(8.dp)
                        .testTag("DesignScoreCardUploadButton")
                ) {
                    Text(
                        text = stringResource(R.string.upload),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DesignScoreCardPreview() {
    val scoreCard = sampleScoreCard
    DesignScoreCardBody(
        scoreCard = scoreCard,
        immerseMode = false,
        onUpload = {},
    )
}

