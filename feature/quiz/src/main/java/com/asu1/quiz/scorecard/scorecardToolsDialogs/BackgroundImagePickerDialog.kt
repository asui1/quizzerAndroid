package com.asu1.quiz.scorecard.scorecardToolsDialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModelActions
import com.asu1.resources.QuizzerAndroidTheme

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

@Preview(showBackground = true)
@Composable
fun PreviewBackgroundImagePicker(){
    QuizzerAndroidTheme {
        BackgroundImagePickerDialog(
            scoreCard = sampleScoreCard,
            updateQuizCoordinate = {},
            onDismiss = {},
            screenWidth = 300.dp,
            screenHeight = 600.dp,
        )
    }
}
