package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.GetTextStyle
import com.asu1.quizzer.model.Quiz4
import com.asu1.quizzer.model.sampleQuiz4
import com.asu1.quizzer.viewModels.QuizTheme
import com.asu1.quizzer.viewModels.quizModels.Quiz4ViewModel

@Composable
fun Quiz4Viewer(
    quiz: Quiz4ViewModel = viewModel(),
    quizTheme: QuizTheme = QuizTheme(),
    onExit: (Quiz4) -> Unit = {},
) {
    val quiz4State by quiz.quiz4State.collectAsState()
    var startOffset by remember { mutableStateOf(Offset(0.0f, 0.0f)) }
    var endOffset by remember { mutableStateOf(Offset(0.0f, 0.0f)) }
    var initOffset by remember { mutableStateOf<Offset?>(null) }
    var isDragging by remember { mutableStateOf(false) }
    val color = quizTheme.colorScheme.primary
    var boxPosition by remember { mutableStateOf(Offset.Zero) }
    val dotSizeDp = 20.dp
    val paddingDp = 4.dp
    val boxPadding = 16.dp
    val moveOffsetDp = (dotSizeDp + paddingDp * 2 - boxPadding) / 2
    val moveOffset = with(LocalDensity.current) { moveOffsetDp.toPx() }

    DisposableEffect(Unit) {
        onDispose {
            onExit(quiz4State)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(boxPadding)
            .onGloballyPositioned { coordinates ->
                boxPosition = coordinates.positionInRoot()
            }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                GetTextStyle(
                    quiz4State.question,
                    quizTheme.questionTextStyle,
                    quizTheme.colorScheme,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(quiz4State.answers.size) { index ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    GetTextStyle(
                        quiz4State.answers[index],
                        quizTheme.bodyTextStyle,
                        quizTheme.colorScheme,
                        modifier = Modifier.weight(1f)
                    )
                    DraggableDot(
                        setOffset = {offset ->
                            quiz.updateDotOffset(index, offset, true)
                        },
                        pointerEvent = { it ->
                            detectDragGestures(
                                onDragStart = {
                                    startOffset = quiz4State.dotPairOffsets[index].first ?: Offset(0f, 0f)
                                    endOffset = quiz4State.dotPairOffsets[index].second ?: Offset(0f, 0f)
                                    quiz.updateUserConnection(index, null)
                                    initOffset = null
                                    isDragging = true
                                },
                                onDragEnd = {
                                    isDragging = false
                                    quiz.updateUserConnection(index, endOffset)
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
                    Spacer(modifier = Modifier.weight(1.0f))
                    DraggableDot(
                        setOffset = {offset ->
                            quiz.updateDotOffset(index, offset, false)
                        },
                        boxPosition = boxPosition,
                        dotSize = dotSizeDp,
                        padding = paddingDp,
                        moveOffset = moveOffset,
                        )
                    GetTextStyle(
                        quiz4State.connectionAnswers[index],
                        quizTheme.bodyTextStyle,
                        quizTheme.colorScheme,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        DrawLines(
            dotOffsets = quiz4State.dotPairOffsets,
            connections = quiz4State.userConnectionIndex
        )
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


@Preview(showBackground = true)
@Composable
fun Quiz4ViewerPreview() {
    val quiz4ViewModel: Quiz4ViewModel = viewModel()
    quiz4ViewModel.loadQuiz(sampleQuiz4)

    Quiz4Viewer(
        quiz = quiz4ViewModel
    )
}