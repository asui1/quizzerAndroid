package com.asu1.quizzer.screens.quizlayout

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.quizzer.composables.animations.OpenCloseColumn
import com.asu1.quizzer.composables.animations.UploadingAnimation
import com.asu1.quizzer.composables.scorecard.ScoreCardComposable
import com.asu1.quizzer.util.disableImmersiveMode
import com.asu1.quizzer.util.enableImmersiveMode
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import kotlinx.coroutines.launch

val colorNames: List<Int> = listOf(R.string.background_newline,
    R.string.effect, R.string.gradient)

@Composable
fun DesignScoreCardScreen(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    scoreCardViewModel: ScoreCardViewModel = viewModel(),
    onUpload: () -> Unit = { }
) {
    val scoreCard by scoreCardViewModel.scoreCard.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = (configuration.screenWidthDp.dp).coerceAtMost(screenHeight * 0.6f)
    val quizLayoutViewModelState by quizLayoutViewModel.viewModelState.observeAsState()
    var expanded by remember {mutableStateOf(true)}
    var immerseMode by remember { mutableStateOf(false) }
    val localActivity = LocalActivity.current
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose {
            localActivity?.disableImmersiveMode()
        }
    }

    MaterialTheme(
        colorScheme = scoreCard.colorScheme
    ) {
        if(ViewModelState.UPLOADING == quizLayoutViewModelState){
            Dialog(
                onDismissRequest = { },
                properties = DialogProperties(dismissOnBackPress = false)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(200.dp)
                ){
                    UploadingAnimation()
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (immerseMode) {
                        localActivity?.disableImmersiveMode()
                    } else {
                        localActivity?.enableImmersiveMode()
                    }
                    immerseMode = !immerseMode
                }
        ) {
            DesignScoreCardBody(
                scoreCard,
                immerseMode,
                onUpload = {
                    scope.launch {
                        quizLayoutViewModel.tryUpload(navController, scoreCard, onUpload)
                    }
                },
            )
            OpenCloseColumn(
                isOpen = expanded,
                onToggleOpen = { expanded = !expanded },
                height = screenHeight,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ){
                DesignScoreCardTools(
                    scoreCardViewModel,
                    scoreCard,
                    screenWidth,
                    screenHeight
                )
            }
        }
    }
}



@Composable
private fun DesignScoreCardBody(
    scoreCard: ScoreCard,
    immerseMode: Boolean,
    onUpload: () -> Unit = { },
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
                .animateContentSize()
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

