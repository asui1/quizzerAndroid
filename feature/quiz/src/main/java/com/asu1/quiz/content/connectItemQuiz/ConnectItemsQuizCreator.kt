package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.customComposable.textField.TextFieldWithDelete
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.quiz.content.DOT_SIZE_DP
import com.asu1.quiz.content.MOVE_OFFSET_DP
import com.asu1.quiz.content.PADDING_SIZE_DP
import com.asu1.quiz.content.quizCommonBuilder.AddAnswer
import com.asu1.quiz.content.quizCommonBuilder.QuizCreatorBase
import com.asu1.quiz.content.STROKE_WIDTH
import com.asu1.quiz.viewmodel.quiz.ConnectItemsQuizViewModel
import com.asu1.quiz.viewmodel.quiz.ConnectItemsQuizAction
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce

@Composable
fun ConnectItemsQuizCreator(
    quizVm: ConnectItemsQuizViewModel = viewModel(),
    onSave: (ConnectItemsQuiz) -> Unit
) {
    val quizState by quizVm.quizState.collectAsStateWithLifecycle()
    val dragState = rememberDragState()
    val leftDotOffsets = quizVm.leftDotOffsets
    val rightDotOffsets = quizVm.rightDotOffsets
    val moveOffset = with(LocalDensity.current) { MOVE_OFFSET_DP.toPx() }

    QuizCreatorBase(
        modifier = Modifier
            .onGloballyPositioned { initCoordinates ->
                dragState.boxPosition = initCoordinates.positionInRoot()
            },
        quiz = quizState,
        testTag = "ConnectItemsQuizCreatorLazyColumn",
        onSave = { onSave(quizState) },
        updateQuiz = {action -> quizVm.onAction(action)},
        boxScopeBehind = {
            DrawLines(
                leftDots = leftDotOffsets, rightDots = rightDotOffsets,
                connections = quizState.connectionAnswerIndex
            )
        },
        boxScopeOnTop = {
            DragLineOverlay(dragState)
        }
    ) {
        connectItemsEditor(
            quizState = quizState, dragState = dragState, leftDotOffsets = leftDotOffsets,
            onQuiz4Update = { action -> quizVm.onQuiz4Update(action)},
            moveOffset = moveOffset
        )
    }
}

private fun LazyListScope.connectItemsEditor(
    quizState: ConnectItemsQuiz,
    dragState: DragState,
    leftDotOffsets: SnapshotStateList<Offset>,
    onQuiz4Update: (ConnectItemsQuizAction) -> Unit,
    moveOffset: Float
) {
    item {
        val focusManager = LocalFocusManager.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Left answers + draggable dots
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f)
            ) {
                quizState.answers.forEachIndexed { idx, ans ->
                    AnswerWithDotRow(
                        answer = ans,
                        index = idx,
                        dragState = dragState,
                        dotOffset = leftDotOffsets[idx],
                        focusManager = focusManager,
                        onQuiz4Update = onQuiz4Update,
                        moveOffset = moveOffset
                    )
                    Spacer(Modifier.height(8.dp))
                }
                AddAnswer(onClick = { onQuiz4Update(ConnectItemsQuizAction.AddLeft) })
            }

            Spacer(Modifier.width(40.dp))

            // Right answers + static dots
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f)
            ) {
                quizState.connectionAnswers.forEachIndexed { idx, ans ->
                    DotWithAnswerRow(
                        answer = ans,
                        index = idx,
                        dragState = dragState,
//                        onUpdateDot = onUpdateRightDot,
//                        onRemove = onRemoveRight,
//                        onChange = onChangeRight,
                        onQuiz4Update = onQuiz4Update,
                        moveOffset = moveOffset
                    )
                    Spacer(Modifier.height(8.dp))
                }
                AddAnswer(onClick = {onQuiz4Update(ConnectItemsQuizAction.AddRight)} )
            }
        }
    }
}

@Composable
private fun AnswerWithDotRow(
    answer: String,
    index: Int,
    dragState: DragState,
    dotOffset: Offset,
    onQuiz4Update: (ConnectItemsQuizAction) -> Unit,
    focusManager: FocusManager,
    moveOffset: Float
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextFieldWithDelete(
            modifier = Modifier
                .weight(1f)
                .testTag("QuizCreatorAnswerLeftTextField$index"),
            value = answer,
            onValueChange = { inputText ->
                onQuiz4Update(ConnectItemsQuizAction.
                UpdateLeftAnswerAt(index, inputText))
            },
            onNext = { focusManager.moveFocus(FocusDirection.Right) },
            deleteAnswer = { onQuiz4Update(
                ConnectItemsQuizAction.RemoveLeft(index)) }
        )
        DraggableDot(
            setOffset = { offset ->
                onQuiz4Update(
                    ConnectItemsQuizAction.
                    UpdateLeftDotOffset(index, offset)) },
            pointerEvent = { detectDragGestures(
                onDragStart = {
                    dragState.startOffset = dotOffset
                    dragState.endOffset = dotOffset
                    onQuiz4Update(
                        ConnectItemsQuizAction.
                        ResetConnectionCreator(index))
                    dragState.initOffset = null
                    dragState.isDragging = true
                },
                onDragEnd = {
                    dragState.isDragging = false
                    onQuiz4Update(
                        ConnectItemsQuizAction.
                        OnDragEndCreator(index, dragState.endOffset))
                    dragState.startOffset = Offset.Zero
                    dragState.endOffset = Offset.Zero
                },
                onDrag = { change, _ ->
                    change.consume()
                    if (dragState.initOffset == null) dragState.initOffset = change.position
                    dragState.initOffset?.let { init ->
                        dragState.endOffset = Offset(
                            x = dragState.startOffset.x + change.position.x - init.x,
                            y = dragState.startOffset.y + change.position.y - init.y
                        )
                    }
                }
            ) },
            boxPosition = dragState.boxPosition,
            dotSize = DOT_SIZE_DP,
            padding = PADDING_SIZE_DP,
            moveOffset = moveOffset,
            key = "QuizCreatorLeftDot$index"
        )
    }
}

@Composable
private fun DotWithAnswerRow(
    answer: String,
    index: Int,
    dragState: DragState,
    onQuiz4Update: (ConnectItemsQuizAction) -> Unit,
    moveOffset: Float
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        DraggableDot(
            setOffset = { offset ->
                onQuiz4Update(
                    ConnectItemsQuizAction.
                    UpdateRightDotOffset(index, offset)) },
            pointerEvent = {  }, // no drag from right side
            boxPosition = dragState.boxPosition,
            dotSize = DOT_SIZE_DP,
            padding = PADDING_SIZE_DP,
            moveOffset = moveOffset,
            key = "QuizCreatorRightDot$index"
        )
        TextFieldWithDelete(
            value = answer,
            onValueChange = { text ->
                onQuiz4Update(ConnectItemsQuizAction.
                UpdateRightAnswerAt(index, text)) },
            modifier = Modifier
                .weight(1f)
                .testTag("QuizCreatorAnswerRightTextField$index")
        )
    }
}

@Composable
private fun DragLineOverlay(dragState: DragState) {
    val color = MaterialTheme.colorScheme.primary
    if (dragState.isDragging) {
        Canvas(Modifier.fillMaxSize()) {
            drawLine(
                color = color,
                start = dragState.startOffset,
                end = dragState.endOffset,
                strokeWidth = STROKE_WIDTH
            )
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun DraggableDot(
    modifier: Modifier = Modifier,
    setOffset: (Offset) -> Unit = {},
    pointerEvent: suspend PointerInputScope.(Offset) -> Unit = {},
    boxPosition: Offset = Offset.Zero,
    dotSize: Dp = 20.dp,
    padding: Dp = 4.dp,
    moveOffset: Float = 0f,
    key: String = "",
) {
    val offsetFlow = remember { MutableSharedFlow<Offset>(extraBufferCapacity = 1) }

    LaunchedEffect(Unit) {
        offsetFlow
            .debounce(200L)  // adjust debounce delay as needed
            .collect { offset ->
                setOffset(offset)
            }
    }

    Box(
        modifier = modifier
            .size(dotSize)
            .padding(padding)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .pointerInput(Unit) {
                val center = Offset((size.width / 2).toFloat(), (size.height / 2).toFloat())
                pointerEvent(center)
            }
            .onGloballyPositioned { coordinates ->
                val globalOffset = coordinates.positionInRoot()
                // Calculate a new relative offset based on your parameters.
                val relativeOffset = globalOffset - boxPosition + Offset(moveOffset, moveOffset)
                // Emit the calculated offset into the flow.
                offsetFlow.tryEmit(relativeOffset)
            }
            .testTag(key)
    )
}

@Composable
fun DrawLines(
    leftDots: List<Offset?> = emptyList(),
    rightDots: List<Offset?> = emptyList(),
    connections: List<Int?> = emptyList(),
    color: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        for (i in connections.indices) {
            val start = leftDots[i]
            val endIndex = connections[i]
            val end = endIndex?.let { rightDots[it] }

            if(start == null || end == null) continue

            drawLine(
                color = color,
                start = start,
                end = end,
                strokeWidth = STROKE_WIDTH
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Quiz4CreatorPreview() {
    ConnectItemsQuizCreator() {}
}
