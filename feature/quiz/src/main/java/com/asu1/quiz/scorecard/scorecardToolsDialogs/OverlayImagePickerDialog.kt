package com.asu1.quiz.scorecard.scorecardToolsDialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.imageGetter.ImageGetter
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModelActions
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun OverlayImagePickerDialog(
    scoreCard: ScoreCard,
    removeBackground: Boolean,
    updateQuizCoordinate: (QuizCoordinatorActions) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.set_overlay_image),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.remove_background),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = removeBackground,
                    onCheckedChange = { checked ->
                        updateQuizCoordinate(
                            QuizCoordinatorActions.UpdateScoreCard(
                                ScoreCardViewModelActions.UpdateRemoveBackground(checked)
                            )
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ImageGetter(
                image = scoreCard.background.overlayImage,
                onImageUpdate = { bitmap ->
                    updateQuizCoordinate(
                        QuizCoordinatorActions.UpdateScoreCard(
                            ScoreCardViewModelActions.UpdateOverlayImage(bitmap)
                        )
                    )
                },
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOverlayImagePicker(){
    QuizzerAndroidTheme {
        OverlayImagePickerDialog(
            scoreCard = sampleScoreCard,
            removeBackground = true,
        ) { }
    }
}
