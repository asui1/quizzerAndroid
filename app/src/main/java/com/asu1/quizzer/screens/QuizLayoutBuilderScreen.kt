package com.asu1.quizzer.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.DialogComposable
import com.asu1.quizzer.composables.RowWithAppIconAndName
import com.asu1.quizzer.screens.quizlayout.QuizLayoutSetColorScheme
import com.asu1.quizzer.screens.quizlayout.QuizLayoutSetDescription
import com.asu1.quizzer.screens.quizlayout.QuizLayoutSetTags
import com.asu1.quizzer.screens.quizlayout.QuizLayoutSetTextStyle
import com.asu1.quizzer.screens.quizlayout.QuizLayoutSetTitleImage
import com.asu1.quizzer.screens.quizlayout.QuizLayoutTitle
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.LayoutSteps
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizLayoutBuilderScreen(navController: NavController,
                            quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
                            navigateToQuizLoad: () -> Unit = {},
                            scoreCardViewModel: ScoreCardViewModel = viewModel(),
) {
    val quizData by quizLayoutViewModel.quizData.collectAsState()
    val quizTheme by quizLayoutViewModel.quizTheme.collectAsState()
    val policyAgreed by quizLayoutViewModel.policyAgreement.observeAsState(false)
    val step by quizLayoutViewModel.step.observeAsState(LayoutSteps.POLICY)
    var showExitDialog by remember { mutableStateOf(false) }
    val layoutSteps = listOf(
        stringResource(R.string.set_quiz_title),
        stringResource(R.string.set_quiz_description),
        stringResource(R.string.set_quiz_tags),
        stringResource(R.string.set_quiz_image),
        stringResource(R.string.set_color_setting),
        stringResource(R.string.set_text_setting),
    )
    val enabled = when(step){
        LayoutSteps.POLICY -> false
        LayoutSteps.TITLE -> quizData.title.isNotEmpty()
        else -> true
    }
    val context = LocalContext.current

    BackHandler {
        if(step > LayoutSteps.TITLE) {
            quizLayoutViewModel.updateStep(step - 1)
        } else {
            navController.popBackStack()
        }
    }

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
                navController.popBackStack()
            },
            modifier = Modifier.imePadding()
        ) {

            QuizPolicyAgreement(onAgree = {
                quizLayoutViewModel.updatePolicyAgreement(true)
            })
        }
    }

    fun proceed() {
        if(step.ordinal < 6) {
            quizLayoutViewModel.updateStep(step + 1)
        } else {
            NavMultiClickPreventer.navigate(navController, Route.QuizBuilder)
        }
    }

    Scaffold(
        topBar = {
            StepProgressBar(
                navController = navController,
                totalSteps = 6,
                currentStep = if(step == LayoutSteps.POLICY) 1 else step.ordinal,
                layoutSteps = layoutSteps,
                showExitDialog = { showExitDialog = true },
                navigateToQuizLoad = { navigateToQuizLoad() }
            )
        }
    ) {paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column {
                when(step.ordinal) {
                    0 -> {}
                    1 -> {
                        QuizLayoutTitle(
                            title = quizData.title,
                            onTitleChange = { quizLayoutViewModel.setQuizTitle(it) },
                            proceed = {proceed()},
                        )
                    }
                    2 ->{
                        QuizLayoutSetDescription(
                            quizDescription = quizData.description,
                            onDescriptionChange = { quizLayoutViewModel.setQuizDescription(it) },
                            proceed = {proceed()}
                        )
                    }
                    3 -> {
                        QuizLayoutSetTags(
                            quizTags = quizData.tags,
                            onTagChange = { quizLayoutViewModel.updateTag(it) },
                            proceed = {proceed()})
                    }
                    4 -> {
                        QuizLayoutSetTitleImage(
                            quizTitleImage = quizData.image,
                            onImageChange = { quizLayoutViewModel.setQuizImage(it) },
                            proceed = {proceed()})
                    }
                    5 -> {
                        // Set Color Setting
                        QuizLayoutSetColorScheme(
                            colorScheme = quizTheme.colorScheme,
                            onColorUpdate = {name, color -> quizLayoutViewModel.setColorScheme(name, color) },
                            onColorSchemeUpdate = { quizLayoutViewModel.setColorScheme(it) },
                            backgroundImage = quizTheme.backgroundImage,
                            onBackgroundImageUpdate = { quizLayoutViewModel.setBackgroundImage(it) },
                            proceed = {proceed()})
                    }
                    6 -> {
                        QuizLayoutSetTextStyle(
                            questionStyle = quizTheme.questionTextStyle,
                            bodyStyle = quizTheme.bodyTextStyle,
                            answerStyle = quizTheme.answerTextStyle,
                            updateStyle = { targetSelector, index, isIncrease ->
                                quizLayoutViewModel.updateTextStyle(targetSelector, index, isIncrease)
                            },
                            colorScheme = quizTheme.colorScheme,
                            proceed = {proceed()})
                        // Set Text Setting
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                        .padding(16.dp))
                {
                    IconButton(onClick = { if(step.ordinal > 1){
                        quizLayoutViewModel.updateStep(step - 1)
                    }},
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
                            quizLayoutViewModel.saveLocal(context, scoreCardViewModel.scoreCard.value)
                        }
                    ){
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
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Move Forward"
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun QuizLayoutBuilderScreenPreview() {
    QuizzerAndroidTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            QuizLayoutBuilderScreen(
                navController = rememberNavController(),
            )
        }
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
                .imePadding(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(stringResource(R.string.agree))
        }
    }
}

@Preview
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
    navController: NavController,
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier,
    layoutSteps: List<String>,
    completedColor: Color = MaterialTheme.colorScheme.primary,
    pendingColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    showExitDialog: () -> Unit = {},
    navigateToQuizLoad: () -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            RowWithAppIconAndName(
                showBackButton = true,
                onBackPressed = {
                    showExitDialog()
                }
            )
            IconButton(
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
        Text(
            text = currentStep.toString()+ ". " + layoutSteps[currentStep - 1],
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp),
            maxLines = 1,
        )
        Row(modifier = modifier.fillMaxWidth()) {
            for (step in 1..totalSteps) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .background(if (step <= currentStep) completedColor else pendingColor)
                        .padding(horizontal = 2.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun StepProgressBarPreview() {
    QuizzerAndroidTheme {
        StepProgressBar(
            navController = rememberNavController(),
            totalSteps = 6,
            currentStep = 3,
            layoutSteps = listOf(
                stringResource(R.string.set_quiz_title),
                stringResource(R.string.set_quiz_image),
                stringResource(R.string.set_quiz_description),
                stringResource(R.string.set_quiz_tags),
                stringResource(R.string.set_color_setting),
                stringResource(R.string.set_text_setting),
            )

        )
    }
}