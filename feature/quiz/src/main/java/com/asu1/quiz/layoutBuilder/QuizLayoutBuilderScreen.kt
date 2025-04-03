package com.asu1.quiz.layoutBuilder

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
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
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizGeneralActions
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeActions
import com.asu1.resources.LayoutSteps
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import com.asu1.utils.LanguageSetter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizLayoutBuilderScreen(
    navController: NavController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel(),
    navigateToQuizLoad: () -> Unit = {},
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val quizState by quizCoordinatorViewModel.quizUIState.collectAsStateWithLifecycle()
    val quizData = quizState.quizGeneralUiState.quizData
    val quizTheme = quizState.quizTheme
    val policyAgreed = quizState.quizGeneralUiState.isPolicyAgreed
    val quizLayoutViewModelState by quizCoordinatorViewModel.quizViewModelState.collectAsStateWithLifecycle()
    val step = quizState.quizGeneralUiState.step
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState(
        initialPage = step.value,
    ) {
        LayoutSteps.entries.size - 2
    }
    LaunchedEffect(step) {
        when (step) {
            LayoutSteps.POLICY -> pagerState.animateScrollToPage(0)
            LayoutSteps.TITLE -> pagerState.animateScrollToPage(1)
            LayoutSteps.TAGS -> pagerState.animateScrollToPage(1)
            LayoutSteps.DESCRIPTION -> pagerState.scrollToPage(1)
            LayoutSteps.IMAGE -> {
                keyboardController?.hide()
                pagerState.scrollToPage(2)
            }

            LayoutSteps.THEME -> pagerState.scrollToPage(3)
            LayoutSteps.TEXTSTYLE -> pagerState.scrollToPage(4)
        }
    }
    BackHandler {
        if(step > LayoutSteps.TITLE) {
            quizCoordinatorViewModel.updateQuizCoordinator(
                QuizCoordinatorActions.UpdateQuizGeneral(
                    QuizGeneralActions.UpdateStep(step-1)
                )
            )
        } else {
            navController.popBackStack(
                Route.Home,
                inclusive = false
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            keyboardController?.hide()
        }
    }

    AnimatedContent(
        targetState = quizLayoutViewModelState,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "QuizLayout Building Screen",
    ) {targetState ->
        when(targetState){
            ViewModelState.LOADING -> {
                LoadingAnimation()
            }
            else -> {
                if(!policyAgreed) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            navController.popBackStack(Route.Home, inclusive = false)
                        },
                        modifier = Modifier.imePadding()
                    ) {
                        QuizPolicyAgreement(onAgree = {
                            quizCoordinatorViewModel.updateQuizCoordinator(
                                QuizCoordinatorActions.UpdateQuizGeneral(
                                    QuizGeneralActions.UpdatePolicyAgreement
                                )
                            )
                        })
                    }
                }

                fun proceed() {
                    if(step.value < LayoutSteps.entries.size - 1) {
                        quizCoordinatorViewModel.updateQuizCoordinator(
                            QuizCoordinatorActions.UpdateQuizGeneral(
                                QuizGeneralActions.UpdateStep(step+1)
                            )
                        )
                    } else {
                        quizCoordinatorViewModel.updateQuizCoordinator(
                            QuizCoordinatorActions.UpdateQuizTheme(
                                QuizThemeActions.InitTextStyleManager
                            )
                        )
                        navController.navigate(Route.QuizBuilder){
                            launchSingleTop = true
                        }
                    }
                }
                QuizLayoutBuilderScreenBody(
                    step = step,
                    quizData = quizData,
                    quizTheme = quizTheme,
                    navigateToQuizLoad = navigateToQuizLoad,
                    moveBackToHome = {
                        navController.popBackStack(Route.Home, inclusive = false)
                    },
                    proceed = {proceed()},
                    onSaveLocal = {
                        scope.launch {
                            quizCoordinatorViewModel.saveLocal(context)
                        }
                    },
                    pagerState = pagerState,
                    updateQuiz = { action ->
                        quizCoordinatorViewModel.updateQuizCoordinator(action)
                    }
                )
            }
        }
    }
}

//TODO: MAKE QUIZLAYOUTBUILDER BODY
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
    updateQuiz: (QuizCoordinatorActions) -> Unit = {},
){
    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        DialogComposable(
            title = R.string.warning,
            message = R.string.warn_progress_not_saved,
            onContinue = {
                showExitDialog = false
                moveBackToHome()
            },
            onContinueText = R.string.proceed,
            onCancel = { showExitDialog = false },
            onCancelText = R.string.cancel
        )
    }

    Scaffold(
        topBar = {
            StepProgressBar(
                totalSteps = LayoutSteps.entries.size,
                currentStep = step,
                showExitDialog = { showExitDialog = true },
                navigateToQuizLoad = navigateToQuizLoad
            )
        },
        bottomBar = {
            QuizLayoutBottomBar(
                step = step,
                updateStep = { step ->
                    updateQuiz(QuizCoordinatorActions.UpdateQuizGeneral(
                        QuizGeneralActions.UpdateStep(step)
                    ))
                },
                onSaveLocal = onSaveLocal,
                proceed = proceed,
                moveBackToHome = { moveBackToHome() }
            )
        }
    ) {paddingValue ->
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(MaterialTheme.colorScheme.background)
        ){ page ->
            when(page){
                0 -> {}
                1 -> {
                    QuizLayoutTitleDescriptionTag(
                        quizData = quizData,
                        step = step,
                        proceed = { proceed() },
                        onTagUpdate = {
                            updateQuiz(
                                QuizCoordinatorActions.UpdateQuizGeneral(
                                    QuizGeneralActions.ToggleQuizTag(it)
                                )
                            )
                        },
                        onDescriptionUpdate = {
                            updateQuiz(
                                QuizCoordinatorActions.UpdateQuizGeneral(
                                    QuizGeneralActions.UpdateQuizDescription(it)
                                )
                            )
                        },
                        onTitleUpdate = {
                            updateQuiz(
                                QuizCoordinatorActions.UpdateQuizGeneral(
                                    QuizGeneralActions.UpdateQuizTitle(it)
                                )
                            )
                        },
                    )
                }
                2 -> {
                    QuizLayoutSetTitleImage(
                        quizTitleImage = quizData.image,
                        onImageChange = {
                            updateQuiz(
                                QuizCoordinatorActions.UpdateQuizGeneral(
                                    QuizGeneralActions.UpdateQuizImage(it)
                                )
                            )
                        },
                    )
                }
                3 -> {
                    QuizLayoutSetQuizTheme(
                        quizTheme = quizTheme,
                        isTitleImageSet = quizData.image.width > 5,
                        updateQuizCoordinator = { action ->
                            updateQuiz(action)
                        },
                    )
                }
                4 -> {
                    QuizLayoutSetTextStyle(
                        questionStyle = quizTheme.questionTextStyle,
                        bodyStyle = quizTheme.bodyTextStyle,
                        answerStyle = quizTheme.answerTextStyle,
                        updateStyle = { targetSelector, index, isIncrease ->
                            updateQuiz(
                                QuizCoordinatorActions.UpdateQuizTheme(
                                    QuizThemeActions.UpdateTextStyle(
                                        targetSelector, index, isIncrease
                                    )
                                )
                            )
                        },
                        colorScheme = quizTheme.colorScheme,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizLayoutBottomBar(
    step: LayoutSteps,
    updateStep: (LayoutSteps) -> Unit = {},
    onSaveLocal: () -> Unit = {},
    proceed: () -> Unit,
    moveBackToHome: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        IconButtonWithText(
            imageVector = Icons.Default.ArrowBackIosNew,
            text = stringResource(R.string.back),
            onClick = {
                if (step.ordinal > 1) {
                    updateStep(step - 1)
                }
                else{
                    moveBackToHome()
                }
            },
            description = "Move back"
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButtonWithText(
            imageVector = Icons.Default.Save,
            text = stringResource(R.string.temp_save),
            onClick = onSaveLocal,
            description = "Save Local"
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButtonWithText(
            modifier = Modifier.testTag("QuizLayoutBuilderProceedButton"),
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            text = stringResource(R.string.next),
            onClick = proceed,
            description = "Move Forward"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutBuilderScreenPreview() {
    QuizzerAndroidTheme {
        QuizLayoutBottomBar(
            step = LayoutSteps.TITLE,
            proceed = {}
        )
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
    navigateToQuizLoad: () -> Unit = {},
) {
    val progress by animateFloatAsState(targetValue = (currentStep.value + 1) / totalSteps.toFloat(),
        label = "Build QuizLayout Progress Bar"
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
    )
    {
        QuizzerTopBarBase(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
            header = @Composable {
                IconButton(onClick = {
                    showExitDialog()
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.move_back_home)
                    )
                }
            },
            body = @Composable {
                Text(
                    text = "Quizzer",
                    style = QuizzerTypographyDefaults.quizzerHeadlineMedium
                )
            },
            actions = @Composable {
                IconButton(
                    modifier = Modifier.width(30.dp),
                    onClick = {
                        navigateToQuizLoad()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Load Local Save"
                    )
                }
            }
        )
        Text(
            text = buildString {
                append(currentStep.value)
                append(". ")
                append(stringResource(id = currentStep.stringResourceId))
            },
            style = QuizzerTypographyDefaults.quizzerHeadlineSmallNormal,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            maxLines = 1,
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            color = completedColor,
        )
    }
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