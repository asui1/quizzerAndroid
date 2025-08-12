package com.asu1.quiz.layoutBuilder

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.customComposable.button.IconButtonWithText
import com.asu1.customComposable.dialog.DialogComposable
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.models.quiz.QuizData
import com.asu1.models.quiz.QuizTheme
import com.asu1.quiz.layoutBuilder.quizTextInputs.QuizLayoutTitleDescriptionTag
import com.asu1.quiz.layoutBuilder.quizThemeBuilder.QuizLayoutSetQuizTheme
import com.asu1.quiz.ui.QuizLayoutBottomBar
import com.asu1.quiz.viewmodel.LoadLocalQuizViewModel
import com.asu1.quiz.viewmodel.UserViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizGeneralActions
import com.asu1.resources.LayoutSteps
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.utils.LanguageSetter
import com.asu1.utils.setTopBarColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun QuizLayoutBuilderScreen(
    navController: NavController,
    navigateToQuizLoad: () -> Unit = {}
) {
    // 1) Gather all state & callbacks
    val vm: QuizCoordinatorViewModel       = viewModel()
    val loadVm: LoadLocalQuizViewModel = viewModel()
    val userVm: UserViewModel = viewModel()
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    val uiState     by vm.quizUIState.collectAsStateWithLifecycle()
    val builderStep = uiState.quizGeneralUiState.step
    val policyAgreed= uiState.quizGeneralUiState.isPolicyAgreed
    val theme       = uiState.quizTheme
    val quizData    = uiState.quizGeneralUiState.quizData
    val pagerState  = rememberPagerState(
        initialPage = builderStep.value,
        pageCount   = { LayoutSteps.entries.size - 2 }
    )

    // 2) Side-effects (step→page sync, back-handler, keyboard hide, top-bar color)
    QuizLayoutBuilderSideEffects(
        step       = builderStep,
        pagerState = pagerState,
        navController = navController,
        themeColor   = theme.colorScheme.primaryContainer
    )

    // 3) Top-level AnimatedContent (loading vs builder)
    AnimatedContent(
        targetState = vm.quizViewModelState.collectAsStateWithLifecycle().value,
        transitionSpec = {
            fadeIn(tween(500))
                .togetherWith(fadeOut(tween(500)))
        },
        label = "QuizLayoutBuilder Loading/Active"
    ) { state ->
        when (state) {
            ViewModelState.LOADING -> LoadingAnimation()
            else                   -> QuizLayoutBuilderActive(
                policyAgreed      = policyAgreed,
                onAgreePolicy     = { vm.updateQuizCoordinator(
                    QuizCoordinatorActions.UpdateQuizGeneral(
                        QuizGeneralActions.UpdatePolicyAgreement
                    ))},
                quizData          = quizData,
                quizTheme         = theme,
                step              = builderStep,
                pagerState        = pagerState,
                onLoadLocal       = {
                    loadVm.loadLocalQuiz(context,
                        userVm.userData.value?.email ?: "GUEST")
                    navigateToQuizLoad()
                },
                onBackToHome      = { navController.popBackStack(Route.Home, false) },
                onProceed         = { proceed(
                    step = builderStep,
                    quizCoordinatorViewModel = vm,
                    navController = navController,
                ) },
                onSaveLocal       = { scope.launch { vm.saveLocal(context) } },
                updateQuizAction  = { action -> vm.updateQuizCoordinator(action) }
            )
        }
    }
}

fun proceed(
    step: LayoutSteps,
    quizCoordinatorViewModel: QuizCoordinatorViewModel,
    navController: NavController,
) {
    if(step.value < LayoutSteps.entries.size - 1) {
        quizCoordinatorViewModel.updateQuizCoordinator(
            QuizCoordinatorActions.UpdateQuizGeneral(
                QuizGeneralActions.UpdateStep(step+1)
            )
        )
    } else {
        navController.navigate(Route.QuizBuilder){
            launchSingleTop = true
        }
    }
}
@Composable
private fun QuizLayoutBuilderSideEffects(
    step: LayoutSteps,
    pagerState: PagerState,
    navController: NavController,
    themeColor: Color
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val view     = LocalView.current
    val vm: QuizCoordinatorViewModel       = viewModel()

    // Sync pager → step
    LaunchedEffect(step) {
        val page = when (step) {
            LayoutSteps.POLICY      -> 0
            LayoutSteps.TITLE       -> 1
            LayoutSteps.TAGS        -> 1
            LayoutSteps.DESCRIPTION -> 1
            LayoutSteps.IMAGE       -> { keyboard?.hide(); 2 }
            LayoutSteps.THEME,
            LayoutSteps.TEXTSTYLE   -> 3
        }
        pagerState.scrollToPage(page)
    }

    // Back button: go back a step or pop home
    BackHandler {
        if (step > LayoutSteps.TITLE) vm.updateQuizCoordinator(
            QuizCoordinatorActions.UpdateQuizGeneral(
                QuizGeneralActions.UpdateStep(step-1)
            )
        )
        else navController.popBackStack(Route.Home, false)
    }

    // Hide keyboard when leaving screen
    DisposableEffect(Unit) {
        onDispose { keyboard?.hide() }
    }

    // Tint status bar / top bar
    LaunchedEffect(themeColor) {
        setTopBarColor(view, themeColor)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizLayoutBuilderActive(
    policyAgreed: Boolean,
    onAgreePolicy: () -> Unit,
    quizData: QuizData,
    quizTheme: QuizTheme,
    step: LayoutSteps,
    pagerState: PagerState,
    onLoadLocal: () -> Unit,
    onBackToHome: () -> Unit,
    onProceed: () -> Unit,
    onSaveLocal: () -> Unit,
    updateQuizAction: (QuizCoordinatorActions) -> Unit
) {
    // 2a) Policy sheet
    if (!policyAgreed) {
        ModalBottomSheet(
            onDismissRequest = onBackToHome,
            modifier = Modifier.imePadding()
        ) {
            QuizPolicyAgreement(onAgree = onAgreePolicy)
        }
    }

    // 2b) Main scaffold + pager body
    QuizLayoutBuilderScreenBody(
        step = step,
        quizData = quizData,
        quizTheme = quizTheme,
        navigateToQuizLoad = onLoadLocal,
        moveBackToHome = onBackToHome,
        proceed = onProceed,
        onSaveLocal = onSaveLocal,
        pagerState = pagerState,
        updateQuiz = updateQuizAction
    )
}

@Composable
fun QuizLayoutBuilderScreenBody(
    step: LayoutSteps,
    quizData: QuizData,
    quizTheme: QuizTheme,
    navigateToQuizLoad: () -> Unit = {},
    moveBackToHome: () -> Unit = {},
    proceed: () -> Unit = {},
    onSaveLocal: () -> Unit = {},
    pagerState: PagerState,
    updateQuiz: (QuizCoordinatorActions) -> Unit = {}
) {
    var showExitDialog by remember { mutableStateOf(false) }

    // Exit confirmation dialog
    if (showExitDialog) {
        QuizExitDialog(
            onConfirm = moveBackToHome,
            onDismiss = { showExitDialog = false }
        )
    }

    Scaffold(
        topBar = {
            QuizLayoutTopBar(
                step = step,
                onExitRequested = { showExitDialog = true },
                navigateToQuizLoad = navigateToQuizLoad
            )
        },
        bottomBar = {
            QuizLayoutBottomBar(
                step = step,
                moveBackToHome = moveBackToHome,
                onStepBack = { updateQuiz(QuizCoordinatorActions.UpdateQuizGeneral(
                    QuizGeneralActions.UpdateStep(step - 1)
                )) },
                proceed = proceed,
                onSaveLocal = onSaveLocal
            )
        }
    ) { paddingValue ->
        QuizLayoutPager(
            quizData      = quizData,
            quizTheme     = quizTheme,
            pagerState    = pagerState,
            updateQuiz    = updateQuiz,
            proceed       = proceed,
            modifier      = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
private fun QuizExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    DialogComposable(
        title = R.string.warning,
        message = R.string.warn_progress_not_saved,
        onContinue = {
            onConfirm()
        },
        onContinueText = R.string.proceed,
        onCancel = onDismiss,
        onCancelText = R.string.cancel
    )
}

@Composable
private fun QuizLayoutTopBar(
    step: LayoutSteps,
    onExitRequested: () -> Unit,
    navigateToQuizLoad: () -> Unit
) {
    StepProgressBar(
        totalSteps = LayoutSteps.entries.size,
        currentStep = step,
        showExitDialog = onExitRequested,
        navigateToQuizLoad = navigateToQuizLoad
    )
}

@Composable
private fun QuizLayoutBottomBar(
    step: LayoutSteps,
    moveBackToHome: () -> Unit,
    onStepBack: () -> Unit,
    proceed: () -> Unit,
    onSaveLocal: () -> Unit
) {
    QuizLayoutBottomBar(
        moveBack = {
            if (step.ordinal > 1) onStepBack() else moveBackToHome()
        },
        moveForward = proceed
    ) {
        IconButtonWithText(
            imageVector = Icons.Default.Save,
            text = stringResource(R.string.temp_save),
            onClick = onSaveLocal,
            description = stringResource(R.string.temp_save),
            iconSize = 24.dp
        )
    }
}

@Composable
private fun QuizLayoutPager(
    quizData: QuizData,
    quizTheme: QuizTheme,
    pagerState: PagerState,
    updateQuiz: (QuizCoordinatorActions) -> Unit,
    proceed: () -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) { page ->
        when (page) {
            0 -> {}
            1 -> QuizLayoutTitleDescriptionTag(
                quizData = quizData,
                onTitleChange = { updateQuiz(
                    QuizCoordinatorActions.UpdateQuizGeneral(
                        QuizGeneralActions.UpdateQuizTitle(it)
                    )
                )},
                onDescriptionChange = { updateQuiz(
                    QuizCoordinatorActions.UpdateQuizGeneral(
                        QuizGeneralActions.UpdateQuizDescription(it)
                    )
                )},
                onTagToggle = { updateQuiz(
                    QuizCoordinatorActions.UpdateQuizGeneral(
                        QuizGeneralActions.ToggleQuizTag(it)
                    )
                )},
                proceed = proceed
            )
            2 -> QuizLayoutSetTitleImage(
                quizTitleImage = quizData.image,
                onImageChange = { updateQuiz(
                    QuizCoordinatorActions.UpdateQuizGeneral(
                        QuizGeneralActions.UpdateQuizImage(it)
                    )
                )}
            )
            3 -> QuizLayoutSetQuizTheme(
                quizTheme = quizTheme,
                isTitleImageSet = quizData.image.width > 5,
                updateQuizCoordinator = updateQuiz,
                step = LayoutSteps.THEME
            )
        }
    }
}

@Composable
fun QuizPolicyAgreement(onAgree: () -> Unit) {
    val annotatedString = remember(LanguageSetter.lang) {
        when (LanguageSetter.lang) {
            "ko" -> getTermsOfUseKo()
            else -> getTermsOfUseEn()
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            stringResource(R.string.terms_of_use),
            style = QuizzerTypographyDefaults.quizzerHeadlineMediumBold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            annotatedString,
            style = QuizzerTypographyDefaults.quizzerBodySmallNormal
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = {onAgree()},
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .testTag("QuizLayoutBuilderAgreePolicyButton"),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                stringResource(R.string.agree),
                style = QuizzerTypographyDefaults.quizzerLabelMediumMedium,
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun QuizPolicyAgreementPreview() {
    QuizzerAndroidTheme {
        QuizPolicyAgreement(
            onAgree = {}
        )
    }
}

@Composable
fun StepProgressBar(
    totalSteps: Int,
    currentStep: LayoutSteps,
    modifier: Modifier = Modifier,
    completedColor: Color = MaterialTheme.colorScheme.primary,
    showExitDialog: () -> Unit = {},
    navigateToQuizLoad: () -> Unit = {}
) {
    val fraction = (currentStep.value + 1) / totalSteps.toFloat()
    val progress by animateFloatAsState(
        targetValue = fraction,
        label = "StepProgressBar animation"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        StepHeader(
            onExit = showExitDialog,
            onLoad = navigateToQuizLoad
        )
        StepTitle(
            step = currentStep
        )
        StepProgress(
            progress = progress,
            color = completedColor
        )
    }
}

@Composable
private fun StepHeader(
    onExit: () -> Unit,
    onLoad: () -> Unit
) {
    QuizzerTopBarBase(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        header = {
            IconButton(onClick = onExit) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.move_back_home)
                )
            }
        },
        body = {
            Text(
                text = stringResource(R.string.app_name),
                style = QuizzerTypographyDefaults.quizzerHeadlineMedium
            )
        },
        actions = {
            IconButton(
                modifier = Modifier.width(30.dp),
                onClick = onLoad
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = "Load Local Save"
                )
            }
        }
    )
}

@Composable
private fun StepTitle(
    step: LayoutSteps
) {
    Text(
        text = buildString {
            append(step.value)
            append(". ")
            append(stringResource(id = step.stringResourceId))
        },
        style = QuizzerTypographyDefaults.quizzerHeadlineSmallNormal,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
        maxLines = 1
    )
}

@Composable
private fun StepProgress(
    progress: Float,
    color: Color
) {
    LinearProgressIndicator(
    progress = { progress },
    modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
    color = color,
    trackColor = ProgressIndicatorDefaults.linearTrackColor,
    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}

@Preview(showBackground = true)
@Composable
fun StepProgressBarPreview() {
    QuizzerAndroidTheme {
        StepProgressBar(
            totalSteps = LayoutSteps.entries.size,
            currentStep = LayoutSteps.TAGS,
        )
    }
}
