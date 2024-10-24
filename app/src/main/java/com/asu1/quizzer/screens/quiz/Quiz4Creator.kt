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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.SaveButton
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.viewModels.quizModels.Quiz4ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@Composable
fun Quiz4Creator(
    quiz: Quiz4ViewModel = viewModel(),
    onSave: (Quiz) -> Unit
) {
    val quizState by quiz.quiz4State.collectAsState()
    var startOffset by remember { mutableStateOf(Offset(0.0f, 0.0f)) }
    var endOffset by remember { mutableStateOf(Offset(0.0f, 0.0f)) }
    var initOffset by remember { mutableStateOf<Offset?>(null) }
    var isDragging by remember { mutableStateOf(true) }
    val color = MaterialTheme.colorScheme.primary
    var focusRequesters = List(quizState.answers.size * 2 + 1) { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var boxPosition by remember { mutableStateOf(Offset.Zero) }
    val dotSizeDp = 20.dp
    val paddingDp = 4.dp
    val boxPadding = 16.dp
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
        DrawLines(dotOffsets = quizState.dotPairOffsets, connections = quizState.connectionAnswerIndex)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                MyTextField(
                    value = quizState.question,
                    onValueChange = { quiz.updateQuestion(it) },
                    focusRequester = FocusRequester(),
                    onNext = {
                        focusRequesters[1].requestFocus()
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(quizState.answers.size) { index ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    MyTextField(
                        value = quizState.answers[index],
                        onValueChange = { quiz.updateAnswerAt(index, it) },
                        focusRequester = focusRequesters[index * 2 + 1],
                        onNext = {
                            focusRequesters[index * 2 + 2].requestFocus()
                        },
                        modifier = Modifier.weight(1f),
                        label = "Answer ${index + 1}"
                    )
                    DraggableDot(
                        setOffset = {offset ->
                            quiz.updateDotOffset(index, offset, true)
                        },
                        pointerEvent = { it ->
                            detectDragGestures(
                                onDragStart = {
                                    startOffset = quizState.dotPairOffsets[index].first ?: Offset(0f, 0f)

                                    endOffset = quizState.dotPairOffsets[index].second ?: Offset(0f, 0f)
                                    quiz.updateConnection(index, null)
                                    initOffset = null
                                    isDragging = true
                                },
                                onDragEnd = {
                                    isDragging = false
                                    quiz.updateConnection(index, endOffset)
                                    startOffset = Offset(0f, 0f)
                                    endOffset = Offset(0f, 0f)
                                },
                            ) { change, dragAmount ->
                                change.consume()
                                if(initOffset == null){
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
                    )
                    Spacer(modifier = Modifier.width(60.dp))
                    DraggableDot(
                        setOffset = {offset ->
                            quiz.updateDotOffset(index, offset, false)
                        },
                        boxPosition = boxPosition,
                        dotSize = dotSizeDp,
                        padding = paddingDp,
                        moveOffset = moveOffset,
                    )
                    MyTextField(
                        value = quizState.connectionAnswers[index],
                        onValueChange = { quiz.updateConnectionAnswer(index, it) },
                        focusRequester = focusRequesters[index * 2 + 2],
                        imeAction = if(index == quizState.answers.size - 1) {
                            ImeAction.Done
                        } else {
                            ImeAction.Next
                        },
                        onNext = {
                            if(index == quizState.answers.size - 1) {
                                focusManager.clearFocus()
                            } else {
                                focusRequesters[index * 2 + 3].requestFocus()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        label = "Answer ${index + 1}"
                    )
                    IconButton(
                        onClick = {
                            if(focusRequesters.size <= 7){
                                return@IconButton
                            }
                            focusRequesters = focusRequesters.toMutableList().also {
                                it.removeAt(index * 2 + 1)
                                it.removeAt(index * 2 + 1)
                            }
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
                        focusRequesters = focusRequesters.toMutableList().also {
                            it.add(FocusRequester())
                            it.add(FocusRequester())
                        }
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
    setOffset: (Offset) -> Unit = {},
    pointerEvent: suspend PointerInputScope.(Offset) -> Unit = {},
    boxPosition: Offset = Offset.Zero,
    dotSize: Dp = 20.dp,
    padding: Dp = 4.dp,
    moveOffset: Float = 0f
) {

    Box(
        modifier = Modifier
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
    )
}

@Composable
fun DrawLines(
    dotOffsets: List<Pair<Offset?, Offset?>> = emptyList(),
    connections: List<Int?> = emptyList()
) {
    val leftDotOffsets = dotOffsets.map { it.first }
    val rightDotOffsets = dotOffsets.map { it.second }
    val color = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.fillMaxSize()) {
        for(i in connections.indices) {
            if(connections[i] == null || leftDotOffsets[i] == null || rightDotOffsets[connections[i]!!] == null) continue
            else{
                drawLine(
                    color = color,
                    start = leftDotOffsets[i]!!, // Center of left dot
                    end = rightDotOffsets[connections[i]!!]!!, // Center of right dot
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