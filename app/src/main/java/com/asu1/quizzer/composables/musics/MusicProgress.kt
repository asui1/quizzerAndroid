package com.asu1.quizzer.composables.musics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.musics.floatToTime
import com.asu1.quizzer.util.musics.msToTime

@Composable
fun MusicProgress(
    progress: Float,
    duration: Long,
    modifier: Modifier = Modifier
){

    val totalTimeText = remember(duration) { msToTime(duration) }

    ConstraintLayout(
        modifier = modifier
    ) {
        val (currentTime, progressBar, totalTime) = createRefs()
        LinearProgressIndicatorWithDraggable(
            currentDuration = progress,
            totalDurationInMS = duration,
            modifier = Modifier.fillMaxWidth()
                .constrainAs(progressBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        Text(
            text = floatToTime(progress),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.constrainAs(currentTime) {
                top.linkTo(progressBar.bottom, margin = 4.dp)
                start.linkTo(parent.start)
            }
        )

        Text(
            text = totalTimeText,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.constrainAs(totalTime) {
                top.linkTo(progressBar.bottom)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
fun LinearProgressIndicatorWithDraggable(
    currentDuration: Float,
    totalDurationInMS: Long,
    strokeCap: StrokeCap = StrokeCap.Round,
    progressedColor: Color = MaterialTheme.colorScheme.primary,
    notProgressedColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    modifier: Modifier = Modifier
){
    val progress = remember(currentDuration, totalDurationInMS) {
        if (totalDurationInMS > 0) {
            (currentDuration / totalDurationInMS * 1000).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
    LaunchedEffect(progress) {
        Logger.debug("Progress: $progress")
    }
    Canvas(
        modifier = modifier.progressSemantics()
            .fillMaxWidth()
            .height(4.dp)
            .semantics(mergeDescendants = true) {
                progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f)
            }
    ){
        val width = size.width
        val height = size.height
        val yOffset = height / 2

        // Draw not progressed part
        drawLine(
            color = notProgressedColor,
            start = Offset(0f, yOffset),
            end = Offset(width, yOffset),
            strokeWidth = height,
            cap = strokeCap
        )

        drawCircle(
            color = progressedColor,
            center = Offset(progress * width, yOffset),
            radius = height * 1.4f
        )

        // Draw progressed part
        drawLine(
            color = progressedColor,
            start = Offset(0f, yOffset),
            end = Offset(progress * width, yOffset),
            strokeWidth = height * 1.2f,
            cap = strokeCap
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MusicProgressPreview() {
    MusicProgress(
        progress = 50000f,
        duration = 150000
    )
}