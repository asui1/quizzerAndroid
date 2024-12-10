package com.asu1.quizzer.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.quizzer.composables.quizcards.VerticalQuizCardLargeColumn
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.constants.sampleQuizCard

@Composable
fun QuizTrendScreen(
    quizTrends: List<QuizCard>,
    loadQuiz: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    VerticalQuizCardLargeColumn(
        quizCards = quizTrends,
        onClick = loadQuiz,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun QuizTrendScreenPreview() {
    val quizCard = sampleQuizCard
    val quizCards = mutableListOf<QuizCard>()
    for (i in 1..5) {
        quizCards.add(
            quizCard
        )
    }
    QuizzerAndroidTheme {
        QuizTrendScreen(
            quizTrends = quizCards
        )
    }
}