package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
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
        // 1) layout of both columns
        ConnectItemsLayout(
            quiz = quiz,
            dragState = dragState,
            leftDotOffsets = leftDotOffsets,
            rightDotOffsets = rightDotOffsets
        )

        // 2) persisted connections
        if (leftDotOffsets != null && rightDotOffsets != null) {
            ConnectionLines(
                leftDots = leftDotOffsets,
                rightDots = rightDotOffsets,
                connections = quiz.userConnectionIndex
            )
        }

        // 3) active drag line
        DragLineOverlay(
            modifier = Modifier.matchParentSize(),
            dragState)
    }
}

@Composable
private fun ConnectItemsLayout(
    quiz: ConnectItemsQuiz,
    dragState: DragState?,
    leftDotOffsets: SnapshotStateList<Offset>?,
    rightDotOffsets: SnapshotStateList<Offset>?
) {
    // extract pointerEvent lambdas without DragSide
    val leftPointer: suspend PointerInputScope.(Offset, Int) -> Unit = { offset, index ->
        if (dragState != null && leftDotOffsets != null && rightDotOffsets != null) {
            // do drag setup only when we have non-null state
            dragState.startOffset = leftDotOffsets[index]
            dragState.endOffset   = leftDotOffsets[index]
            quiz.userConnectionIndex[index] = null
            dragState.initOffset  = null
            dragState.isDragging  = true
        }
    }
    val rightPointer: suspend PointerInputScope.(Offset, Int) -> Unit = { _, _ -> }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConnectColumn(
            modifier = Modifier
                .weight(1f)
                .pointerInput(dragState, leftDotOffsets, rightDotOffsets) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            // handled in leftPointer
                        },
                        onDragEnd = {
                            dragState?.let { state ->
                                val i = quiz.userConnectionIndex.indexOfFirst { it == null }
                                quiz.userConnectionIndex[i] = getDragIndex(state.endOffset, rightDotOffsets!!)
                                state.startOffset = Offset.Zero
                                state.endOffset = Offset.Zero
                                state.isDragging = false
                            }
                        }
                    ) { change, _ ->
                        change.consume()
                        dragState?.let { state ->
                            if (state.initOffset == null) state.initOffset = change.position
                            state.endOffset = state.startOffset + (change.position - (state.initOffset!!))
                        }
                    }
                },
            items = quiz.answers,
            dotOffsets = leftDotOffsets,
            dragState = dragState,
            pointerEvent = leftPointer
        )

        Spacer(Modifier.width(40.dp))

        ConnectColumn(
            modifier = Modifier.weight(1f),
            items = quiz.connectionAnswers,
            dotOffsets = rightDotOffsets,
            reverse = true,
            dragState = dragState,
            pointerEvent = rightPointer
        )
    }
}

@Composable
private fun ConnectionLines(
    leftDots: List<Offset>,
    rightDots: List<Offset>,
    connections: List<Int?>
) {
    val color = MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxSize()) {
        connections.forEachIndexed { i, j ->
            j?.let {
                drawLine(
                    color = color,
                    start = leftDots[i],
                    end = rightDots[it],
                    strokeWidth = 8f
                )
            }
        }
    }
}

@Composable
private fun DragLineOverlay(
    modifier: Modifier,
    dragState: DragState?
) {
    val color = MaterialTheme.colorScheme.primary
    if (dragState?.isDragging == true) {
        Canvas(modifier) {
            drawLine(
                color = color,
                start = dragState.startOffset,
                end = dragState.endOffset,
                strokeWidth = 8f
            )
        }
    }
}
