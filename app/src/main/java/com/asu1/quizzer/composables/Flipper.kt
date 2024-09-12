package com.asu1.quizzer.composables

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme

@Composable
fun Flipper(items: List<Any>, currentIndex: Int, onNext: (Int) -> Unit, onPrevious: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount.x > 0) {
                        onPrevious(currentIndex)
                    } else {
                        onNext(currentIndex)
                    }
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { onPrevious(currentIndex) }) {
            Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Previous")
        }
        Text(
            text = items[currentIndex].toString(),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = { onNext(currentIndex) }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Next")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewFlipper() {
    QuizzerAndroidTheme {
        Flipper(
            items = listOf("Item 1", "Item 2", "Item 3"),
            currentIndex = 0,
            onNext = {},
            onPrevious = {}
        )
    }
}