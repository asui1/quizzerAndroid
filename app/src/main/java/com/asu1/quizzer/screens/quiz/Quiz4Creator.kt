package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.SaveButton
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.viewModels.quizModels.Quiz4ViewModel

@Composable
fun Quiz4Creator(
    quiz: Quiz4ViewModel = viewModel(),
    onSave: (Quiz) -> Unit
) {
    val quizState by quiz.quiz4State.collectAsState()
    var leftDotOffsets by remember { mutableStateOf(List(quizState.answers.size) { Offset.Zero }) }
    var rightDotOffsets by remember { mutableStateOf(List(quizState.answers.size) { Offset.Zero }) }
    var startOffset by remember { mutableStateOf(Offset.Zero) }
    var endOffset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    val color = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DrawLines(leftDotOffsets = leftDotOffsets, rightDotOffsets = rightDotOffsets, connections = quizState.connectionAnswerIndex)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                QuestionTextField(
                    value = quizState.question,
                    onValueChange = { quiz.updateQuestion(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(quizState.answers.size) { index ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    TextField(
                        value = quizState.answers[index],
                        onValueChange = { quiz.updateAnswerAt(index, it) },
                        label = { Text("Answer ${index + 1}") },
                        modifier = Modifier.weight(1f)
                    )
                    DraggableDot(
                        setOffset = {offset ->
                            if (index < leftDotOffsets.size) {
                                leftDotOffsets = leftDotOffsets.toMutableList().also {
                                    it[index] = offset
                                }
                            } else {
                                leftDotOffsets = leftDotOffsets.toMutableList().also {
                                    it.add(offset)
                                }
                            }
                        },
                        pointerEvent = {
                            detectDragGestures(
                                onDragStart = {
                                    startOffset = leftDotOffsets[index]
                                    endOffset = leftDotOffsets[index]
                                    isDragging = true
                                    quiz.updateConnectionAnswerIndex(index, null)
                                },
                                onDragEnd = {
                                    isDragging = false
                                    val rightIndex = rightDotOffsets.indexOfFirst {
                                        it.x in (endOffset.x - 20)..(endOffset.x + 20) &&
                                                it.y in (endOffset.y - 20)..(endOffset.y + 20)
                                    }

                                    if(rightIndex != -1) {
                                        quiz.updateConnectionAnswerIndex(index, rightIndex)
                                    }
                                },
                            ) { change, dragAmount ->
                                change.consume()
                                endOffset = Offset(
                                    x = startOffset.x + dragAmount.x,
                                    y = startOffset.y + dragAmount.y
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(60.dp))
                    DraggableDot(
                        setOffset = {offset ->
                            if (index < rightDotOffsets.size) {
                                rightDotOffsets = rightDotOffsets.toMutableList().also {
                                    it[index] = offset
                                }
                            } else {
                                rightDotOffsets = rightDotOffsets.toMutableList().also {
                                    it.add(offset)
                                }
                            }
                        }
                    )
                    TextField(
                        value = quizState.connectionAnswers[index],
                        onValueChange = { quiz.updateConnectionAnswer(index, it) },
                        label = { Text("Answer ${index + 1}") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            quiz.removeAnswerAt(index)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove answer"
                        )
                    }

                }
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = {
                        quiz.addAnswer()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "Add answer"
                    )
                }
            }
        }
        SaveButton {
            onSave(quizState)
        }
    }
}

@Composable
fun DraggableDot(
    setOffset: (Offset) -> Unit = {},
    pointerEvent: suspend PointerInputScope.() -> Unit = {},
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .padding(4.dp)
            .background(color = MaterialTheme.colorScheme.primary,shape = CircleShape)
            .pointerInput(Unit) {
                pointerEvent()
            }
            .onGloballyPositioned { coordinates ->
                setOffset(Offset(coordinates.size.width / 2f, coordinates.size.height / 2f))
            }
    )
}

@Composable
fun DrawLines(
    leftDotOffsets: List<Offset>,
    rightDotOffsets: List<Offset>,
    connections: List<Int?> = emptyList()
) {
    val color = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.fillMaxSize()) {
        for(i in connections.indices) {
            if(connections[i] == null) continue
            drawLine(
                color = color,
                start = leftDotOffsets[i], // Center of left dot
                end = rightDotOffsets[connections[i]!!], // Center of right dot
                strokeWidth = 4f
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz4CreatorPreview() {
    Quiz4Creator() {}
}