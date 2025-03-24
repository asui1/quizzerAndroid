package com.asu1.quiz.viewer

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.sampleQuiz4
import com.asu1.quiz.creator.DraggableDot
import com.asu1.quiz.creator.DrawLines
import com.asu1.quiz.ui.TextStyleManager
import com.asu1.quiz.viewmodel.quiz.Quiz4ViewModel
import com.asu1.quiz.viewmodel.quiz.Quiz4ViewModelStates
import com.asu1.resources.TextStyles
import com.asu1.utils.Logger

@Composable
fun Quiz4Viewer(
    quiz: Quiz4ViewModel = viewModel(),
    quizTheme: QuizTheme = QuizTheme(),
    quizStyleManager: TextStyleManager,
    isPreview: Boolean = false,
    onUpdate: (List<Int?>) -> Unit = {},
) {
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    var startOffset by remember { mutableStateOf(Offset(0.0f, 0.0f)) }
    var endOffset by remember { mutableStateOf(Offset(0.0f, 0.0f)) }
    var initOffset by remember { mutableStateOf<Offset?>(null) }
    var isDragging by remember { mutableStateOf(false) }
    var boxPosition by remember { mutableStateOf(Offset.Zero) }
    val color = remember{quizTheme.colorScheme.primary}
    val dotSizeDp = 20.dp
    val paddingDp = 4.dp
    val boxPadding = 16.dp
    val moveOffsetDp = (dotSizeDp + paddingDp * 2 - boxPadding) / 2
    val moveOffset = with(LocalDensity.current) { moveOffsetDp.toPx() }

    DisposableEffect(Unit){
        onDispose {
            Logger.debug("Save")
            onUpdate(quizState.userConnectionIndex)
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
            userScrollEnabled = !isPreview,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                quizStyleManager.GetTextComposable(TextStyles.QUESTION, quizState.question, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
            }
            item{
                BuildBody(
                    quizBody = quizState.bodyType,
                    quizStyleManager = quizStyleManager,
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            ) {
                                quizStyleManager.GetTextComposable(TextStyles.ANSWER,
                                    quizState.answers[index],
                                    modifier = Modifier.weight(1f)
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
                                                    Quiz4ViewModelStates.ResetConnectionViewer(
                                                        index
                                                    )
                                                )
                                                initOffset = null
                                                isDragging = true
                                            },
                                            onDragEnd = {
                                                isDragging = false
                                                quiz.onQuiz4Update(
                                                    Quiz4ViewModelStates.OnDragEndViewer(
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
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
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
                                quizStyleManager.GetTextComposable(
                                    TextStyles.ANSWER,
                                    quizState.connectionAnswers[index],
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        if(!isPreview) {
            DrawLines(
                leftDots = quizState.leftDots,
                rightDots = quizState.rightDots,
                connections = quizState.userConnectionIndex
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
}


@Preview(showBackground = true)
@Composable
fun Quiz4ViewerPreview() {
    val quiz4ViewModel: Quiz4ViewModel = viewModel()
    quiz4ViewModel.loadQuiz(sampleQuiz4)

    Quiz4Viewer(
        quiz = quiz4ViewModel,
        quizTheme = QuizTheme(),
        quizStyleManager = TextStyleManager()
    )
}