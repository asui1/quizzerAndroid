package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset

data class DragState(
    var startOffset: Offset,
    var endOffset: Offset,
    var initOffset: Offset?,
    var isDragging: Boolean,
    var boxPosition: Offset
)

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

