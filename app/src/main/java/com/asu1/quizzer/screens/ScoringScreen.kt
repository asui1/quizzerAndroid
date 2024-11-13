package com.asu1.quizzer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.composables.ScoreCardComposable
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.generateUniqueId
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.createSampleQuizLayoutViewModel
import com.asu1.quizzer.viewModels.createSampleScoreCardViewModel
import kotlinx.coroutines.launch

@Composable
fun ScoringScreen(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    scoreCardViewModel: ScoreCardViewModel = viewModel(),
    email: String = "GUEST",
    loadQuiz: (String) -> Unit = {},
) {
    val quizResult by quizLayoutViewModel.quizResult.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val scoreCard by scoreCardViewModel.scoreCard.collectAsState()
    var uniqueId = remember{""}
    val quizLayoutViewModelState by quizLayoutViewModel.viewModelState.observeAsState()
    val scoreCardViewModelState by scoreCardViewModel.viewModelState.observeAsState()

    LaunchedEffect(scoreCard.quizUuid){
        if(scoreCard.quizUuid != null){
            uniqueId = generateUniqueId(email = email, uuid = scoreCard.quizUuid!!)
        }
    }

    LaunchedEffect(quizLayoutViewModelState){
        if(quizLayoutViewModelState == ViewModelState.ERROR){
            Logger().debug("Error in quizLayoutViewModel")
            quizLayoutViewModel.resetViewModelState()
            navController.popBackStack(Route.Home, inclusive = false)
        }
    }

    DisposableEffect(key1 = uniqueId){
        onDispose {
            scoreCardViewModel.resetScoreCard()
            quizLayoutViewModel.resetQuizResult()
        }
    }

    MaterialTheme(
        colorScheme = scoreCard.colorScheme
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            if (quizLayoutViewModelState == ViewModelState.LOADING || scoreCardViewModelState == ViewModelState.LOADING || quizResult == null) {
                CircularProgressIndicator(
                    modifier = Modifier.size(
                        width = screenWidth * 0.8f,
                        height = screenHeight * 0.8f
                    )
                )
            } else {
                ScoreCardComposable(
                    width = screenWidth * 0.8f,
                    height = screenHeight * 0.8f,
                    scoreCard = scoreCard,
                    quizResult = quizResult!!
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            RowWithShares(
                onClickButton1 = {
                    val background = scoreCard.background
                    Logger().debug(background.color.toString())
                    Logger().debug(background.color2.toString())
                    scoreCardViewModel.updateBackground(
                        ImageColor(
                            color = background.color,
                            imageData = scoreCard.background.imageData,
                            color2 = Color.Red,
                            state = ImageColorState.COLOR2
                        )
                    )
                },
                onClickButton2 = {
                    val background = scoreCard.background
                    Logger().debug(background.color.toString())
                    Logger().debug(background.color2.toString())
                    Logger().debug(Color.Red.toString())
                    scoreCardViewModel.updateBackground(
                        ImageColor(
                            color = Color.Red,
                            imageData = scoreCard.background.imageData,
                            color2 = background.color2,
                            state = ImageColorState.COLOR2
                        )
                    )
                },
                onClickButton3 = {
                    scoreCardViewModel.updateImageColorState(
                        ImageColorState.COLOR2
                    )
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.width(screenWidth * 0.7f)
            ) {
                Button(
                    onClick = {
//                        Logger().debug(scoreCard.toString())
                        loadQuiz(scoreCard.quizUuid!!)
                    },
                    modifier = Modifier
                        .height(height = 36.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "Solve Again",
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
                        .weight(1f)
                ) {
                    Text(
                        text = "Move Home",
                        style = MaterialTheme.typography.bodySmall
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