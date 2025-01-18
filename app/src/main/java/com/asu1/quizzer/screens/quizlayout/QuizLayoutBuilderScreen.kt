package com.asu1.quizzer.screens.quizlayout

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.DialogComposable
import com.asu1.quizzer.composables.animations.LoadingAnimation
import com.asu1.quizzer.composables.base.RowWithAppIconAndName
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.keyboardAsState
import com.asu1.quizzer.viewModels.LayoutSteps
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizLayoutBuilderScreen(navController: NavController,
                            quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
                            navigateToQuizLoad: () -> Unit = {},
                            scoreCardViewModel: ScoreCardViewModel = viewModel(),
) {
    val quizData by quizLayoutViewModel.quizData.collectAsStateWithLifecycle()
    val quizTheme by quizLayoutViewModel.quizTheme.collectAsStateWithLifecycle()
    val policyAgreed by quizLayoutViewModel.policyAgreement.observeAsState(false)
    val quizLayoutViewModelState by quizLayoutViewModel.viewModelState.observeAsState()
    val step by quizLayoutViewModel.step.observeAsState(LayoutSteps.POLICY)
    var showExitDialog by remember { mutableStateOf(false) }
    val canProceed = when(step){
        LayoutSteps.POLICY -> false
        LayoutSteps.TITLE -> quizData.title.isNotEmpty()
        else -> true
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = step.value,
    ) {
        LayoutSteps.entries.size - 2
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val stacks = remember { mutableIntStateOf(0) }

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
        Logger.debug("Back Handler Called Pressed")
        if(step > LayoutSteps.TITLE) {
            quizLayoutViewModel.updateStep(step - 1)
        } else {
            navController.popBackStack(
                Route.Home,
                inclusive = false
            )
        }
    }
    // RECOGNIZE BACK PRESSED

    fun onBackPressed(){
        if(pagerState.currentPage != 1){
            return
        }
        if(showExitDialog){
            return
        }
        Logger.debug("Back Pressed Called")
        if(step > LayoutSteps.TITLE && step < LayoutSteps.IMAGE){
            quizLayoutViewModel.updateStep(step - 1)
        }
        else if(step == LayoutSteps.TITLE){
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
                if (showExitDialog) {
                    DialogComposable(
                        titleResource = R.string.warning,
                        messageResource = R.string.warn_progress_not_saved,
                        onContinue = {
                            showExitDialog = false
                            navController.popBackStack()
                        },
                        onContinueResource = R.string.proceed,
                        onCancel = { showExitDialog = false },
                        onCancelResource = R.string.cancel
                    )
                }

                if(!policyAgreed) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            navController.popBackStack(Route.Home, inclusive = false)
                        },
                        modifier = Modifier.imePadding()
                    ) {
                        QuizPolicyAgreement(onAgree = {
                            quizLayoutViewModel.updatePolicyAgreement(true)
                        })
                    }
                }

                fun proceed() {
                    if(step.value < LayoutSteps.entries.size - 1) {
                        if(step.value <= LayoutSteps.TAGS.value) {
                            stacks.intValue++
                        }
                        quizLayoutViewModel.updateStep(step + 1)
                    } else {
                        quizLayoutViewModel.initTextStyleManager()
                        navController.navigate(Route.QuizBuilder){
                            launchSingleTop = true
                        }
                    }
                }

                Scaffold(
                    topBar = {
                        StepProgressBar(
                            totalSteps = LayoutSteps.entries.size,
                            currentStep = step,
                            showExitDialog = { showExitDialog = true },
                            navigateToQuizLoad = { navigateToQuizLoad() }
                        )
                    },
                    bottomBar = {
                        QuizLayoutBottomBar(step, quizLayoutViewModel, scope, context, scoreCardViewModel, canProceed, proceed = { proceed() })
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
                                val isKeyboardOpen by keyboardAsState()
                                LaunchedEffect(isKeyboardOpen) {
                                    Logger.debug("Keyboard is open: $isKeyboardOpen, stacks: ${stacks.intValue}")
                                    if(!isKeyboardOpen && stacks.intValue > 0){
                                        stacks.intValue--
                                        if(stacks.intValue == 0){
                                            keyboardController?.hide()
                                        }
                                        onBackPressed()
                                    }
                                    else{
                                        stacks.intValue++
                                    }
                                    Logger.debug("Keyboard is open end: $isKeyboardOpen, stacks: ${stacks.intValue}")
                                }
                                QuizLayoutTitleDescriptionTag(
                                    quizData = quizData,
                                    step = step,
                                    proceed = { proceed() },
                                    onTagUpdate = { quizLayoutViewModel.updateTag(it) },
                                    onDescriptionUpdate = { quizLayoutViewModel.setQuizDescription(it) },
                                    onTitleUpdate = { quizLayoutViewModel.setQuizTitle(it) },
                                )
                            }
                            2 -> {
                                QuizLayoutSetTitleImage(
                                    quizTitleImage = quizData.image,
                                    onImageChange = { quizLayoutViewModel.setQuizImage(it) }
                                )
                            }
                            3 -> {
                                // Set Color Setting
                                QuizLayoutSetColorScheme(
                                    colorScheme = quizTheme.colorScheme,
                                    isTitleImageSet = quizData.image.isNotEmpty(),
                                    onColorUpdate = {name, color -> quizLayoutViewModel.setColorScheme(name, color) },
                                    backgroundImage = quizTheme.backgroundImage,
                                    onBackgroundColorUpdate = { quizLayoutViewModel.updateBackgroundColor(it) },
                                    onGradientColorUpdate = { quizLayoutViewModel.updateGradientColor(it) },
                                    onImageUpdate = { quizLayoutViewModel.updateBackgroundImage(it) },
                                    onImageColorStateUpdate = { quizLayoutViewModel.updateImageColorState(it) },
                                    generateColorScheme = {base, paletteLevel, contrastLevel, isDark ->
                                        quizLayoutViewModel.generateColorScheme(
                                            base = base,
                                            paletteLevel = paletteLevel,
                                            contrastLevel = contrastLevel,
                                            isDark = isDark
                                        )
                                    },
                                    onGradientTypeUpdate = {
                                        quizLayoutViewModel.updateGradientType(it)
                                    }
                                )
                            }
                            4 -> {
                                QuizLayoutSetTextStyle(
                                    questionStyle = quizTheme.questionTextStyle,
                                    bodyStyle = quizTheme.bodyTextStyle,
                                    answerStyle = quizTheme.answerTextStyle,
                                    updateStyle = { targetSelector, index, isIncrease ->
                                        quizLayoutViewModel.updateTextStyle(targetSelector, index, isIncrease)
                                    },
                                    colorScheme = quizTheme.colorScheme,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizLayoutBottomBar(
    step: LayoutSteps,
    quizLayoutViewModel: QuizLayoutViewModel,
    scope: CoroutineScope,
    context: Context,
    scoreCardViewModel: ScoreCardViewModel,
    enabled: Boolean,
    proceed: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    )
    {
        IconButton(
            onClick = {
                if (step.ordinal > 1) {
                    quizLayoutViewModel.updateStep(step - 1)
                }
            },
            enabled = step.ordinal > 1,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Move Back"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {
                scope.launch {
                    quizLayoutViewModel.saveLocal(context, scoreCardViewModel.scoreCard.value)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save Local."
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {
                proceed()
            },
            enabled = enabled,
            modifier = Modifier.testTag("QuizLayoutBuilderProceedButton")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Move Forward"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutBuilderScreenPreview() {
    QuizzerAndroidTheme {
        QuizLayoutBottomBar(
            step = LayoutSteps.TITLE,
            quizLayoutViewModel = QuizLayoutViewModel(),
            scope = rememberCoroutineScope(),
            context = LocalContext.current,
            scoreCardViewModel = ScoreCardViewModel(),
            enabled = false,
            proceed = {}
        )
    }
}

@Composable
fun QuizPolicyAgreement(onAgree: () -> Unit) {
    // Content of the ModalBottomSheet
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(stringResource(R.string.terms_of_use), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(stringResource(R.string.quizGenPolicyBody), style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = {onAgree()},
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .testTag("QuizLayoutBuilderAgreePolicyButton"),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(stringResource(R.string.agree))
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            RowWithAppIconAndName(
                showBackButton = true,
                onBackPressed = {
                    showExitDialog()
                },
                modifier = Modifier.weight(1f),
            )
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
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = buildString {
                append(currentStep.value)
                append(". ")
                append(stringResource(id = currentStep.stringResourceId))
            },
            style = MaterialTheme.typography.headlineSmall,
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