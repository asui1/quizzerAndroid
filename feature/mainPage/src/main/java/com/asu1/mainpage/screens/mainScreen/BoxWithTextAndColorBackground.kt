package com.asu1.mainpage.screens.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun BoxWithTextAndColorBackground(
    modifier: Modifier = Modifier,
    backgroundColor: Color, nickname: Char, ) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = nickname.toString(),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxWithTextAndColorBackground(){
    QuizzerAndroidTheme {
        BoxWithTextAndColorBackground(
            modifier = Modifier,
            backgroundColor = Color.Blue,
            nickname = 'H',
        )
    }
}
