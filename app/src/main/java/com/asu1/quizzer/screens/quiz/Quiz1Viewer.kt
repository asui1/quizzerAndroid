package com.asu1.quizzer.screens.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.viewModels.quizModels.Quiz1ViewModel

@Composable
fun Quiz1Viewer(
    quiz: Quiz1ViewModel = viewModel(),
)
{
    val quiz1State by quiz.quiz1State.collectAsState()

}


@Preview(showBackground = true)
@Composable
fun Quiz1ViewerPreview()
{
    Quiz1Viewer()
}