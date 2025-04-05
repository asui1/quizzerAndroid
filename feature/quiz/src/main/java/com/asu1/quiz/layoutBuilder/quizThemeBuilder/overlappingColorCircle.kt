package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.resources.LightColorScheme

@Composable
fun OverlappingColorCircles(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    foregroundColor: Color,
    label: String,
    size: Dp = 50.dp,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(size * 1.2f)
    ) {
        Box(
            modifier = Modifier
                .size(size)
        ) {
            Box(
                modifier = Modifier
                    .size(size * 0.9f)
                    .align(Alignment.Center)
                    .background(backgroundColor, shape = CircleShape)
            )

            Box(
                modifier = Modifier
                    .size(size * 0.7f)
                    .align(Alignment.BottomEnd)
                    .background(foregroundColor, shape = CircleShape)
                    .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
            )
        }
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOverlappingColorCircles(){
    OverlappingColorCircles(
        backgroundColor = LightColorScheme.primary,
        foregroundColor = LightColorScheme.onPrimary,
        label = "Primary Container1"
    )
}
