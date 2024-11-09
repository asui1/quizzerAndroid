package com.asu1.quizzer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.composables.ScoreCardComposable
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import kotlinx.coroutines.launch

@Composable
fun ScoringScreen(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    scoreCardViewModel: ScoreCardViewModel = viewModel(),
) {
    val score by quizLayoutViewModel.score.observeAsState()
    LaunchedEffect(score) {
        if(score != null) {
            scoreCardViewModel.updateScore(score!!)
        }
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val scoreCard by scoreCardViewModel.scoreCard.collectAsState()

    if(score != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            // TODO MAKE PAGER WITH SCORECARD: 1. SCORE / 2. nth answer CORRECT/WRONG / 3. Percentage of result.
            ScoreCardComposable(
                width = screenWidth * 0.8f,
                height = screenHeight * 0.8f,
                scoreCard = scoreCard,
            )
            Spacer(modifier = Modifier.height(8.dp))
            RowWithShares()
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                },
                modifier = Modifier
                    .size(width = screenWidth * 0.6f, height = 48.dp)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Move Home",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ScoreCardWithFullResults(){

}


@Preview(showBackground = true)
@Composable
fun ScoringScreenPreview() {
    ScoringScreen(
        navController = rememberNavController(),
    )
}