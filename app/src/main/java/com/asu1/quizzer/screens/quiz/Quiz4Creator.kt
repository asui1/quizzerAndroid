package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
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
import com.asu1.models.quiz.Quiz
import com.asu1.quizzer.composables.QuestionTextFieldWithPoints
import com.asu1.quizzer.composables.QuizBodyBuilder
import com.asu1.quizzer.composables.SaveButton
import com.asu1.quizzer.viewModels.quizModels.Quiz4ViewModel
import com.asu1.quizzer.composables.TextFieldWithDelete
import com.asu1.quizzer.model.Quiz4ViewModelStates
import com.asu1.utils.Logger

@Composable
fun Quiz4Creator(
    quiz: Quiz4ViewModel = viewModel(),
    onSave: (Quiz) -> Unit
) {
    val quizState by quiz.quiz4State.collectAsStateWithLifecycle()
    var startOffset by remember { mutableStateOf(Offset(0.0f, 0.0f)) }
    var endOffset by remember { mutableStateOf(Offset(0.0f, 0.0f)) }
    var initOffset by remember { mutableStateOf<Offset?>(null) }
    var isDragging by remember { mutableStateOf(true) }
    val color = MaterialTheme.colorScheme.primary
    val focusManager = LocalFocusManager.current
    val dotSizeDp = 20.dp
    val paddingDp = 4.dp
    val boxPadding = 16.dp
    var boxPosition by remember { mutableStateOf(Offset.Zero) }
    val moveOffsetDp = (dotSizeDp + paddingDp * 2 - boxPadding) / 2
    val moveOffset = with(LocalDensity.current) { moveOffsetDp.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(boxPadding)
            .onGloballyPositioned { coordinates ->
                boxPosition = coordinates.positionInRoot()
            }
    ) {
        DrawLines(leftDots = quizState.leftDots, rightDots = quizState.rightDots, connections = quizState.connectionAnswerIndex)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                QuestionTextFieldWithPoints(
                    question = quizState.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    point = quizState.point,
                    onPointChange = { quiz.setPoint(it) },
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                        focusManager.moveFocus(FocusDirection.Left)
                    },
                    focusManager = focusManager
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quizState.bodyType,
                    updateBody = { quiz.updateBodyState(it) },
                    onBodyTextChange = { quiz.updateBodyText(it) },
                    onImageSelected = { quiz.updateBodyImage(it) },
                    onYoutubeUpdate = { id, time ->
                        quiz.updateBodyYoutube(id, time)
                    },
                )
            }
            item{
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
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
                                    label = "Answer ${index + 1}",
                                    deleteAnswer = {
                                        quiz.onQuiz4Update(Quiz4ViewModelStates.RemoveLeft(index))
                                    }
                                )
                                DraggableDot(
                                    setOffset = { offset ->
                                        quiz.onQuiz4Update(
                                            Quiz4ViewModelStates.UpdateLeftDotOffset(
                                                index,
                                                offset
                                            )
                                        )
                                    },
                                    pointerEvent = { it ->
                                        detectDragGestures(
                                            onDragStart = {
                                                startOffset =
                                                    quizState.leftDots[index] ?: Offset(0f, 0f)
                                                endOffset =
                                                    quizState.leftDots[index] ?: Offset(0f, 0f)
                                                quiz.onQuiz4Update(
                                                    Quiz4ViewModelStates.ResetConnectionCreator(
                                                        index
                                                    )
                                                )
                                                initOffset = null
                                                isDragging = true
                                            },
                                            onDragEnd = {
                                                isDragging = false
                                                quiz.onQuiz4Update(
                                                    Quiz4ViewModelStates.OnDragEndCreator(
                                                        index,
                                                        endOffset
                                                    )
                                                )
                                                startOffset = Offset(0f, 0f)
                                                endOffset = Offset(0f, 0f)
                                            },
                                        ) { change, dragAmount ->
                                            change.consume()
                                            if (initOffset == null) {
                                                initOffset = change.position
                                            }
                                            endOffset = Offset(
                                                x = startOffset.x + change.position.x - initOffset!!.x,
                                                y = startOffset.y + change.position.y - initOffset!!.y
                                            )
                                        }
                                    },
                                    boxPosition = boxPosition,
                                    dotSize = dotSizeDp,
                                    padding = paddingDp,
                                    moveOffset = moveOffset,
                                    key = "QuizCreatorLeftDot$index"
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        IconButton(
                            modifier = Modifier.testTag("QuizCreatorAddAnswerButton"),
                            onClick = {
                                quiz.onQuiz4Update(Quiz4ViewModelStates.AddLeft)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "Add answer"
                            )
                        }
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
                                            Quiz4ViewModelStates.UpdateRightDotOffset(
                                                index,
                                                offset
                                            )
                                        )
                                    },
                                    boxPosition = boxPosition,
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
                                    label = "Answer ${index + 1}",
                                    deleteAnswer = {
                                        quiz.onQuiz4Update(Quiz4ViewModelStates.RemoveRight(index))
                                    },
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        IconButton(
                            modifier = Modifier
                                .testTag("QuizCreatorAddAnswerButton"),
                            onClick = {
                                quiz.onQuiz4Update(Quiz4ViewModelStates.AddRight)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "Add answer"
                            )
                        }
                    }
                }
            }
        }
        SaveButton {
            onSave(quizState)
        }
        if(isDragging) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = color,
                    start = startOffset,
                    end = endOffset,
                    strokeWidth = 4f
                )
            }
        }
    }
}

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
                val relativeOffset = Offset(
                    globalOffset.x - boxPosition.x + moveOffset,
                    globalOffset.y - boxPosition.y + moveOffset
                )
                setOffset(relativeOffset)
            }
            .testTag(key)
    )
}

@Composable
fun DrawLines(
    leftDots: List<Offset?> = emptyList(),
    rightDots: List<Offset?> = emptyList(),
    connections: List<Int?> = emptyList()
) {
    val color = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.fillMaxSize()) {
        for(i in connections.indices) {
            if(connections[i] == null || leftDots[i] == null || rightDots[connections[i]!!] == null) continue
            else{
                drawLine(
                    color = color,
                    start = leftDots[i]!!, // Center of left dot
                    end = rightDots[connections[i]!!]!!, // Center of right dot
                    strokeWidth = 4f
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Quiz4CreatorPreview() {
    Quiz4Creator() {}
}