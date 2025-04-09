package com.asu1.quiz.scorecard.scorecardToolsDialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.asu1.models.scorecard.ScoreCard
import com.asu1.quiz.scorecard.ImagePickerWithBaseImages
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModelActions

@Composable
fun BackgroundImagePickerDialog(
    scoreCard: ScoreCard,
    updateQuizCoordinate: (QuizCoordinatorActions) -> Unit,
    onDismiss: () -> Unit,
    screenWidth: Dp,
    screenHeight: Dp
) {
    ImagePickerWithBaseImages(
        modifier = Modifier,
        onBaseImageSelected = { baseImage ->
            updateQuizCoordinate(
                QuizCoordinatorActions.UpdateScoreCard(
                    ScoreCardViewModelActions.UpdateBackgroundImageBase(baseImage)
                )
            )
            onDismiss()
        },
        onImageSelected = { bitmap ->
            updateQuizCoordinate(
                QuizCoordinatorActions.UpdateScoreCard(
                    ScoreCardViewModelActions.UpdateBackgroundImage(bitmap)
                )
            )
            onDismiss()
        },
        imageColorState = scoreCard.background.state,
        currentSelection = scoreCard.background.backgroundBase,
        currentImage = scoreCard.background.imageData,
        width = screenWidth,
        height = screenHeight,
    )
}
