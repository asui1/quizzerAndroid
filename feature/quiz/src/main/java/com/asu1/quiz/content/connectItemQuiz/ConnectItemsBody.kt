package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.utils.getDragIndex

@Composable
internal fun ConnectItemsBody(
    quiz: ConnectItemsQuiz,
    dragState: DragState?,
    leftDotOffsets: SnapshotStateList<Offset>?,
    rightDotOffsets: SnapshotStateList<Offset>?
) {
    Box(
        Modifier
            .fillMaxWidth()
            .onGloballyPositioned { dragState?.boxPosition = it.positionInRoot() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically) {
            ConnectColumn(
                modifier = Modifier.weight(1f),
                items      = quiz.answers,
                dotOffsets = leftDotOffsets,
                dragState = dragState,
                pointerEvent = {offset , index ->
                    if(dragState == null || leftDotOffsets == null || rightDotOffsets == null) return@ConnectColumn
                    detectDragGestures(
                        onDragStart = {
                            dragState.startOffset = leftDotOffsets[index]
                            dragState.endOffset = leftDotOffsets[index]
                            quiz.userConnectionIndex[index] = null
                            dragState.initOffset = null
                            dragState.isDragging = true
                        },
                        onDragEnd = {
                            dragState.isDragging = false
                            quiz.userConnectionIndex[index] = getDragIndex(dragState.endOffset, rightDotOffsets)
                            dragState.startOffset = Offset(0f, 0f)
                            dragState.endOffset = Offset(0f, 0f)
                        },
                    ) { change, dragAmount ->
                        change.consume()
                        if (dragState.initOffset == null) {
                            dragState.initOffset = change.position
                        }
                        dragState.endOffset = Offset(
                            x = dragState.startOffset.x + change.position.x - dragState.initOffset!!.x,
                            y = dragState.startOffset.y + change.position.y - dragState.initOffset!!.y
                        )
                    }
                },
            )
            Spacer(Modifier.width(40.dp))
            ConnectColumn(
                modifier = Modifier.weight(1f),
                items      = quiz.connectionAnswers,
                dotOffsets = rightDotOffsets,
                reverse    = true,  // draws dot on left
                dragState = dragState,
                pointerEvent = {_, _ -> },
            )
        }

        if(leftDotOffsets != null && rightDotOffsets != null){
            DrawLines(
                leftDots    = leftDotOffsets,
                rightDots   = rightDotOffsets,
                connections = quiz.userConnectionIndex
            )
        }

        if (dragState != null && dragState.isDragging) {
            val color = MaterialTheme.colorScheme.primary
            Canvas(Modifier.matchParentSize()) {
                drawLine(
                    color       = color,
                    start       = dragState.startOffset,
                    end         = dragState.endOffset,
                    strokeWidth = 8f
                )
            }
        }
    }
}
