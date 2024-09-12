package com.asu1.quizzer.composables

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> LimitedFlowRow(
    modifier: Modifier = Modifier,
    maxLines: Int,
    items: List<T>,
    itemContent: @Composable FlowRowScope.(T) -> Unit
) {
    FlowRow(
        modifier = modifier,
        content = {
            var currentLine = 0
            var itemsInCurrentLine = 0

            items.forEach { item ->
                if (currentLine >= maxLines) return@forEach

                if (itemsInCurrentLine == 0) {
                    currentLine++
                }

                itemsInCurrentLine++

                itemContent(item)
            }
        }
    )
}