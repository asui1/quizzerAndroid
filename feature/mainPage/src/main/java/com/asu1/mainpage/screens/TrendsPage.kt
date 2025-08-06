package com.asu1.mainpage.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.mainpage.viewModels.QuizCardViewModel
import com.asu1.quizcard.cardBase.VerticalQuizCardLargeColumn
import com.asu1.quizcardmodel.QuizCard

@Composable
fun TrendsPage(
    cards: List<QuizCard>,
    loadQuiz: (String) -> Unit
) {
    val vm: QuizCardViewModel = viewModel()
    VerticalQuizCardLargeColumn(
        quizCards = cards,
        onClick = loadQuiz,
        getMoreTrends = { vm.getMoreQuizTrends() },
        modifier = Modifier.fillMaxSize()
    )
}
