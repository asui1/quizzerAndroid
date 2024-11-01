package com.asu1.quizzer.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.model.ScoreCard

@Composable
fun ScoreCardBackground(
    scoreCard: ScoreCard,
    time: Float
) {
    Image(
        painter = ColorPainter(Color.Transparent),
        contentDescription = "Background",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .then(
                scoreCard.background.asBackgroundModifierForScoreCard(
                    shaderOption = scoreCard.shaderType,
                    time = time
                )
            )
    )
}