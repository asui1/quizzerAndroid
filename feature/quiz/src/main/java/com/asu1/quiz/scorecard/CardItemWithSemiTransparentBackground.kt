package com.asu1.quiz.scorecard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun CardItemWithSemiTransparentBackground(
    modifier: Modifier = Modifier,
    textColor: Color,
    content: @Composable ColumnScope.() -> Unit,
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(textColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardItemWithSemiTransparentBackgroundPreview(
){
    QuizzerAndroidTheme {
        CardItemWithSemiTransparentBackground(
            textColor = Color.Blue
        ) {
            Text(
                "TITLE"
            )
            Text(
                "Creator"
            )
        }
    }
}
