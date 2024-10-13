package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.viewModels.quizModels.Quiz1ViewModel
import androidx.compose.ui.unit.dp

@Composable
fun Quiz1Viewer(
    quiz: Quiz1ViewModel = viewModel(),
)
{
    val quiz1State by quiz.quiz1State.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            // 질문
        }
        item{
            // BODY
        }
        items(quiz1State.answers.size){
            // 답변
        }

    }



}


@Preview(showBackground = true)
@Composable
fun Quiz1ViewerPreview()
{
    Quiz1Viewer()
}