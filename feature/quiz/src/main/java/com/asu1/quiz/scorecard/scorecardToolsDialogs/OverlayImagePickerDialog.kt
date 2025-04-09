package com.asu1.quiz.scorecard.scorecardToolsDialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.Switch.LabeledSwitch
import com.asu1.customComposable.imageGetter.ImageGetter
import com.asu1.models.scorecard.ScoreCard
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModelActions
import com.asu1.resources.R

@Composable
fun OverlayImagePickerDialog(
    scoreCard: ScoreCard,
    removeBackground: Boolean,
    updateQuizCoordinate: (QuizCoordinatorActions) -> Unit,
    onDismiss: () -> Unit
) {
    ImageGetter(
        image = scoreCard.background.overlayImage,
        onImageUpdate = { bitmap ->
            updateQuizCoordinate(
                QuizCoordinatorActions.UpdateScoreCard(
                    ScoreCardViewModelActions.UpdateOverlayImage(bitmap)
                )
            )
        },
        topBar = {
            LabeledSwitch(
                label = stringResource(R.string.remove_background),
                checked = removeBackground,
                onCheckedChange = { checked ->
                    updateQuizCoordinate(
                        QuizCoordinatorActions.UpdateScoreCard(
                            ScoreCardViewModelActions.UpdateRemoveBackground(checked)
                        )
                    )
                }
            )
        },
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                2.dp,
                MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(8.dp)
            )
    )
}
