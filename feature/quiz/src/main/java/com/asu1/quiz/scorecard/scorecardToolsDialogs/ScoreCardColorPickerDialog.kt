package com.asu1.quiz.scorecard.scorecardToolsDialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.imagecolor.ImageBlendMode
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.quiz.scorecard.TextColorPickerModalSheet
import com.asu1.quiz.ui.ToggleTextWithTabRow
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModelActions
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import kotlinx.collections.immutable.persistentListOf

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
                ToggleTextWithTabRow(
                    tabTexts = persistentListOf(stringResource(ImageBlendMode.BLENDCOLOR.stringResourceId), stringResource(ImageBlendMode.BLENDHUE.stringResourceId)),
                    selectedItem = if(scoreCard.background.imageBlendMode == ImageBlendMode.BLENDHUE) 1 else 0,
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
                modifier = Modifier.fillMaxWidth(),
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

@Preview(showBackground = true)
@Composable
fun PreviewScoreCardColorPickerDialog(){
    QuizzerAndroidTheme {
        ScoreCardColorPickerDialog(
            colorIndex = 0,
            scoreCard = sampleScoreCard,
            updateQuizCoordinate = {},
            onDismiss = {},
        )
    }
}