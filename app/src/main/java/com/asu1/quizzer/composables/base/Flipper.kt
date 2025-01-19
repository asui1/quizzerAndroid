package com.asu1.quizzer.composables.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.quizzer.R
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun Flipper(items: List<Any>, currentIndex: Int, onNext: (Int) -> Unit, onPrevious: (Int) -> Unit,
            key: String = "",
            ) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier.testTag("${key}FlipperPrevious"),
            onClick = { onPrevious(currentIndex) }
        ) {
            Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = stringResource(R.string.previous))
        }
        Text(
            text = if (items[currentIndex] is Int) {
                    stringResource(id = items[currentIndex] as Int)
            } else {
                items[currentIndex].toString()
            },
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(
            modifier = Modifier.testTag("${key}FlipperNext"),
            onClick = { onNext(currentIndex) }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = stringResource(
                R.string.next
            )
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewFlipper() {
    com.asu1.resources.QuizzerAndroidTheme {
        Flipper(
            items = listOf("Item 1", "Item 2", "Item 3"),
            currentIndex = 0,
            onNext = {},
            onPrevious = {}
        )
    }
}