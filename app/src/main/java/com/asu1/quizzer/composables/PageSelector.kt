package com.asu1.quizzer.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun PageSelector(
    modifier: Modifier = Modifier,
    totalPages: Int,
    currentPage: Int,
    moveToPage: (Int) -> Unit
) {
    // Compute which pages to display, up to 5 total
    val pagesToDisplay = remember(totalPages, currentPage) {
        if (totalPages <= 5) {
            (1..totalPages).toList()
        } else {
            when {
                currentPage <= 3 -> (1..5).toList()
                currentPage >= totalPages - 2 -> (totalPages - 4..totalPages).toList()
                else -> (currentPage - 2..currentPage + 2).toList()
            }
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        // '<' button if we're not on the first page
        if (currentPage > 1) {
            TextButton(
                onClick = {
                    moveToPage(currentPage - 1)
                },
            ) {
                Text(
                    text = "<",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        // Page numbers (up to 5)
        pagesToDisplay.forEach { page ->
            val isSelected = (page == currentPage)
            TextButton(
                onClick = {
                    moveToPage(page)
                }
            ) {
                Text(
                    text = page.toString(),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // '>' button if we're not on the last page
        if (currentPage < totalPages) {
            TextButton(
                onClick = {
                    moveToPage(currentPage + 1)
                },
            ) {
                Text(
                    text = ">",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PageSelectorPreview(){
    QuizzerAndroidTheme {
        PageSelector(
            currentPage = 2,
            totalPages = 5,
        ) { }
    }
}