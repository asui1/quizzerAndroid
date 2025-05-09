package com.asu1.quiz.scorecard.scorecardToolsDialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.asu1.models.scorecard.ScoreCard
import com.asu1.quiz.scorecard.TextColorPickerModalSheet
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModelActions
import com.asu1.resources.R

@Composable
fun TextColorPickerDialog(
    scoreCard: ScoreCard,
    updateQuizCoordinate: (QuizCoordinatorActions) -> Unit
) {
    TextColorPickerModalSheet(
        initialColor = scoreCard.textColor,
        onColorSelected = { color ->
            updateQuizCoordinate(
                QuizCoordinatorActions.UpdateScoreCard(
                    ScoreCardViewModelActions.UpdateTextColor(color)
                )
            )
        },
        text = stringResource(R.string.select_text_color),
        colorName = stringResource(R.string.text_color)
    )
}
