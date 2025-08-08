package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

@Stable
class DragState(
    startOffset: Offset,
    endOffset: Offset,
    initOffset: Offset?,
    isDragging: Boolean,
    boxPosition: Offset
) {
    var startOffset by mutableStateOf(startOffset)
    var endOffset   by mutableStateOf(endOffset)
    var initOffset  by mutableStateOf(initOffset)
    var isDragging  by mutableStateOf(isDragging)
    var boxPosition by mutableStateOf(boxPosition)
}

@Composable
internal fun rememberDragState(
): DragState {
    return remember {
        DragState(
            startOffset = Offset.Zero,
            endOffset   = Offset.Zero,
            initOffset  = null,
            isDragging  = false,
            boxPosition = Offset.Zero
        )
    }
}

