package com.asu1.quiz.scorecard.scorecardToolsDialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.asu1.imagecolor.ImageBlendMode
import com.asu1.models.scorecard.ScoreCard
import com.asu1.quiz.scorecard.TextColorPickerModalSheet
import com.asu1.quiz.scorecard.ToggleTextWithSwitch
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModelActions
import com.asu1.resources.R

@Composable
fun ScoreCardColorPickerDialog(
    colorIndex: Int,
    scoreCard: ScoreCard,
    updateQuizCoordinate: (QuizCoordinatorActions) -> Unit,
    onDismiss: () -> Unit
) {
    TextColorPickerModalSheet(
        initialColor = when(colorIndex) {
            1 -> scoreCard.background.color2
            2 -> scoreCard.background.colorGradient
            else -> scoreCard.background.color
        },
        onColorSelected = { color ->
            updateQuizCoordinate(
                QuizCoordinatorActions.UpdateScoreCard(
                    ScoreCardViewModelActions.UpdateColor(color, colorIndex)
                )
            )
        },
        text = stringResource(
            when(colorIndex) {
                1 -> R.string.select_color2
                2 -> R.string.select_color3
                else -> R.string.select_color1
            }
        ),
        colorName = stringResource(
            when(colorIndex) {
                1 -> R.string.color2
                2 -> R.string.color3
                else -> R.string.color1
            }
        ),
        onClose = onDismiss,
        toggleBlendMode = {
            if(colorIndex == 0){
                ToggleTextWithSwitch(
                    textA = stringResource(ImageBlendMode.BLENDCOLOR.stringResourceId),
                    textB = stringResource(ImageBlendMode.BLENDHUE.stringResourceId),
                    isBSelected = scoreCard.background.imageBlendMode == ImageBlendMode.BLENDHUE,
                    onClick = {
                        updateQuizCoordinate(
                            QuizCoordinatorActions.UpdateScoreCard(
                                ScoreCardViewModelActions.ChangeBlendMode
                            )
                        )
                    }
                )
            }
        },
        resetToTransparent = {
            ResetToTransparentButton(
                onReset = {
                    updateQuizCoordinate(
                        QuizCoordinatorActions.UpdateScoreCard(
                            ScoreCardViewModelActions.UpdateColor(Color.Transparent, colorIndex)
                        )
                    )
                }
            )
        },
    )
}
