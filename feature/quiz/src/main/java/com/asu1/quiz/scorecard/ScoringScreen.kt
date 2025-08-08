package com.asu1.quiz.scorecard

import SnackBarManager
import ToastType
import android.app.Activity
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.customComposable.dialog.ShareDialog
import com.asu1.customComposable.uiUtil.disableImmersiveMode
import com.asu1.customComposable.uiUtil.enableImmersiveMode
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.models.quiz.QuizResult
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import kotlinx.collections.immutable.PersistentList

/* ------- helpers ------- */
val BTN_HEIGHT: Dp = 36.dp
val BTN_WIDTH: Dp = 150.dp
val SPACER: Dp = 8.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoringScreen(
    navController: NavController,
    loadQuiz: (String) -> Unit = {},
) {
    val coordinator: QuizCoordinatorViewModel = viewModel()
    val userVm: UserViewModel = viewModel()
    val email = userVm.userData.value?.email ?: "GUEST"
    val ui by coordinator.quizUIState.collectAsStateWithLifecycle()
    val vmState by coordinator.quizViewModelState.collectAsStateWithLifecycle()

    var showShare by remember { mutableStateOf(false) }
    var immerse by remember { mutableStateOf(false) }
    var movingToChecker by remember { mutableStateOf(false) }
    val activity = LocalActivity.current
    val questions = remember { coordinator.getQuestions() }

    ScreenEffects(
        vmState = vmState,
        activity = activity,
        movingToChecker = movingToChecker,
        onError = { navController.popBackStack(Route.Home, inclusive = false) },
        onDisposeReset = { coordinator.updateQuizCoordinator(QuizCoordinatorActions.ResetQuizResult) }
    )

    AnimatedState(
        state = vmState,
        loading = { LoadingAnimation() }
    ) {
        val scoreCard = ui.scoreCardState.scoreCard
        MaterialTheme(colorScheme = scoreCard.colorScheme) {
            ScoringContent(
                scoreCard = scoreCard,
                quizResult = requireNotNull(ui.quizResult),
                questions = questions,
                isResultView = ui.quizContentState.quizzes.isEmpty(),
                immerse = immerse,
                onToggleImmerse = {
                    if (immerse) activity?.disableImmersiveMode() else activity?.enableImmersiveMode()
                    immerse = !immerse
                },
                onMoveHome = {
                    coordinator.updateQuizCoordinator(QuizCoordinatorActions.ResetQuiz)
                    navController.navigate(Route.Home) {
                        launchSingleTop = true; popUpTo(Route.Home) { inclusive = true }
                    }
                },
                onShare = { showShare = true },
                onGoChecker = {
                    movingToChecker = true
                    navController.navigate(Route.QuizChecker) { launchSingleTop = true }
                },
                loadQuiz = loadQuiz
            )
            ShareSheet(
                visible = showShare,
                quizId = scoreCard.quizUuid.orEmpty(),
                userName = email,
                onDismiss = { showShare = false }
            )
        }
    }
}

/* ---------- Helpers (can live below, not counted toward the screen) ---------- */

@Composable
private fun ScreenEffects(
    vmState: ViewModelState,
    activity: Activity?,
    movingToChecker: Boolean,
    onError: () -> Unit,
    onDisposeReset: () -> Unit,
) {
    LaunchedEffect(vmState) { if (vmState == ViewModelState.ERROR) onError() }
    DisposableEffect(Unit) {
        onDispose {
            activity?.disableImmersiveMode()
            if (!movingToChecker) onDisposeReset()
        }
    }
}

@Composable
private fun AnimatedState(
    state: ViewModelState,
    loading: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(500)) },
        label = "ScoringScreen"
    ) { s -> if (s == ViewModelState.LOADING) loading() else content() }
}

@Composable
private fun ScoringContent(
    scoreCard: ScoreCard,
    quizResult: QuizResult,
    questions: PersistentList<String>,
    isResultView: Boolean,
    immerse: Boolean,
    onToggleImmerse: () -> Unit,
    onMoveHome: () -> Unit,
    onShare: () -> Unit,
    onGoChecker: () -> Unit,
    loadQuiz: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggleImmerse
            )
    ) {
        ScoreCardComposable(
            scoreCard = scoreCard,
            quizResult = quizResult,
            quizQuestions = questions,
            modifier = Modifier.fillMaxWidth().weight(1f)
        )
        if (!immerse) {
            Spacer(Modifier.height(8.dp))
            ScoringScreenBottomRow(
                scoreCard = scoreCard,
                loadQuiz = loadQuiz,
                onClickMoveHome = onMoveHome,
                showShareBottomSheet = onShare,
                isResultView = isResultView,
                navigateToQuizChecker = onGoChecker
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShareSheet(
    visible: Boolean,
    quizId: String,
    userName: String,
    onDismiss: () -> Unit
) {
    if (!visible) return
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.imePadding()
    ) {
        ShareDialog(quizId = quizId, userName = userName, onDismiss = onDismiss)
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
        PrimaryActionButton(
            isResultView = isResultView,
            quizUuid = scoreCard.quizUuid,
            loadQuiz = loadQuiz,
            navigateToQuizChecker = navigateToQuizChecker,
        )

        Spacer(Modifier.width(SPACER))

        SizedButton(
            text = stringResource(R.string.move_home),
            onClick = onClickMoveHome,
        )

        Spacer(Modifier.width(SPACER))

        ShareButton(onClick = showShareBottomSheet)
    }
}

@Composable
private fun PrimaryActionButton(
    isResultView: Boolean,
    quizUuid: String?,
    loadQuiz: (String) -> Unit,
    navigateToQuizChecker: () -> Unit,
) {
    val onClick = remember(isResultView, quizUuid) {
        if (isResultView) {
            {
                if (quizUuid != null) {
                    loadQuiz(quizUuid)
                } else {
                    SnackBarManager.showSnackBar(
                        message = R.string.can_not_load_quiz,
                        type = ToastType.ERROR
                    )
                }
            }
        } else {
            { navigateToQuizChecker() }
        }
    }
    val textRes = if (isResultView) R.string.solve_again else R.string.check_answer

    SizedButton(text = stringResource(textRes), onClick = onClick)
}

@Composable
private fun SizedButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(BTN_HEIGHT).width(BTN_WIDTH)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ShareButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            modifier = Modifier.size(24.dp)
        )
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