package com.asu1.quizzer.composables.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme

@Composable
fun IconButtonWithText(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    description: String? = null,
    iconSize: Dp = 32.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable {
            if (enabled) onClick()
        }
            .wrapContentHeight()
            .width(iconSize * 1.7f)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IconButtonWithTextPreview() {
    QuizzerAndroidTheme {
        IconButtonWithText(
            imageVector = Icons.Default.Add,
            text = "Add",
            onClick = { },
        )
    }
}