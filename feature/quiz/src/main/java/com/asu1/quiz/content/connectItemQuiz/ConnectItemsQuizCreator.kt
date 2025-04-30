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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
import com.asu1.quiz.content.dotSizeDp
import com.asu1.quiz.content.moveOffsetDp
import com.asu1.quiz.content.paddingDp
import com.asu1.quiz.content.quizCommonBuilder.AddAnswer
import com.asu1.quiz.content.quizCommonBuilder.QuizCreatorBase
import com.asu1.quiz.content.strokeWidth
import com.asu1.quiz.viewmodel.quiz.ConnectItemsQuizViewModel
import com.asu1.quiz.viewmodel.quiz.ConnectItemsQuizViewModelStates
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce

@Composable
fun ConnectItemsQuizCreator(
    quiz: ConnectItemsQuizViewModel = viewModel(),
    onSave: (ConnectItemsQuiz) -> Unit
) {
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    val dragState = rememberDragState()
    val leftDotOffsets = quiz.leftDotOffsets
    val rightDotOffsets = quiz.rightDotOffsets
    val focusManager = LocalFocusManager.current
    val moveOffset = with(LocalDensity.current) { moveOffsetDp.toPx() }

    QuizCreatorBase(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                dragState.boxPosition = coordinates.positionInRoot()
            },
        quiz = quizState,
        testTag = "ConnectItemsQuizCreatorLazyColumn",
        onSave = { onSave(quizState) },
        focusManager = focusManager,
        updateQuestion = { it -> quiz.updateQuestion(it) },
        updateBodyState = { it -> quiz.updateBodyState(it) },
        header = {
            DrawLines(leftDots = leftDotOffsets, rightDots = rightDotOffsets, connections = quizState.connectionAnswerIndex)
        },
        footer = {
            if(dragState.isDragging) {
                val color = MaterialTheme.colorScheme.primary
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLine(
                        color = color,
                        start = dragState.startOffset,
                        end = dragState.endOffset,
                        strokeWidth = strokeWidth
                    )
                }
            }
        },
    ) {
        item{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    quizState.answers.forEachIndexed { index, answer ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextFieldWithDelete(
                                value = answer,
                                onValueChange = { quiz.updateAnswerAt(index, it) },
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Right)
                                },
                                modifier = Modifier.weight(1f).testTag(
                                    "QuizCreatorAnswerLeftTextField$index"
                                ),
                                deleteAnswer = {
                                    quiz.onQuiz4Update(ConnectItemsQuizViewModelStates.RemoveLeft(index))
                                }
                            )
                            DraggableDot(
                                setOffset = { offset ->
                                    quiz.onQuiz4Update(
                                        ConnectItemsQuizViewModelStates.UpdateLeftDotOffset(
                                            index,
                                            offset
                                        )
                                    )
                                },
                                pointerEvent = { it ->
                                    detectDragGestures(
                                        onDragStart = {
                                            dragState.startOffset =
                                                leftDotOffsets[index]
                                            dragState.endOffset =
                                                leftDotOffsets[index]
                                            quiz.onQuiz4Update(
                                                ConnectItemsQuizViewModelStates.ResetConnectionCreator(
                                                    index
                                                )
                                            )
                                            dragState.initOffset = null
                                            dragState.isDragging = true
                                        },
                                        onDragEnd = {
                                            dragState.isDragging = false
                                            quiz.onQuiz4Update(
                                                ConnectItemsQuizViewModelStates.OnDragEndCreator(
                                                    index,
                                                    dragState.endOffset
                                                )
                                            )
                                            dragState.startOffset = Offset(0f, 0f)
                                            dragState.endOffset = Offset(0f, 0f)
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            if (dragState.initOffset == null) {
                                                dragState.initOffset = change.position
                                            }
                                            dragState.initOffset?.let { start ->
                                                dragState.endOffset = Offset(
                                                    x = dragState.startOffset.x + change.position.x - start.x,
                                                    y = dragState.startOffset.y + change.position.y - start.y
                                                )
                                            }
                                        }
                                    )
                                },
                                boxPosition = dragState.boxPosition,
                                dotSize = dotSizeDp,
                                padding = paddingDp,
                                moveOffset = moveOffset,
                                key = "QuizCreatorLeftDot$index"
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    AddAnswer(
                        onClick = {
                            quiz.onQuiz4Update(ConnectItemsQuizViewModelStates.AddLeft)
                        }
                    )
                }
                Spacer(modifier = Modifier.width(40.dp))
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    quizState.connectionAnswers.forEachIndexed{index, answer ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DraggableDot(
                                setOffset = { offset ->
                                    quiz.onQuiz4Update(
                                        ConnectItemsQuizViewModelStates.UpdateRightDotOffset(
                                            index,
                                            offset
                                        )
                                    )
                                },
                                boxPosition = dragState.boxPosition,
                                dotSize = dotSizeDp,
                                padding = paddingDp,
                                moveOffset = moveOffset,
                                key = "QuizCreatorRightDot$index"
                            )
                            TextFieldWithDelete(
                                value = quizState.connectionAnswers[index],
                                onValueChange = { quiz.updateConnectionAnswerAt(index, it) },
                                isLast = index == quizState.connectionAnswers.size - 1,
                                onNext = {
                                    if (index == quizState.answers.size - 1) {
                                        focusManager.clearFocus()
                                    } else {
                                        focusManager.moveFocus(FocusDirection.Left)
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                },
                                modifier = Modifier.weight(1f).testTag("QuizCreatorAnswerRightTextField$index"),
                                deleteAnswer = {
                                    quiz.onQuiz4Update(ConnectItemsQuizViewModelStates.RemoveRight(index))
                                },
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    AddAnswer(
                        onClick = {
                            quiz.onQuiz4Update(ConnectItemsQuizViewModelStates.AddRight)
                        }
                    )
                }
            }
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
            .background(color = MaterialTheme.colorScheme.primary,
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
                strokeWidth = strokeWidth
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Quiz4CreatorPreview() {
    ConnectItemsQuizCreator() {}
}