package com.asu1.quizzer.screens.quiz

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.asu1.models.sampleQuiz4
import com.asu1.quizzer.composables.AnswerShower
import com.asu1.quizzer.model.Quiz4ViewModelStates
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.quizzer.viewModels.quizModels.Quiz4ViewModel
import com.asu1.resources.TextStyles

@Composable
fun Quiz4Checker(
    quiz: Quiz4ViewModel = viewModel(),
    quizStyleManager: TextStyleManager,
) {
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    var boxPosition by remember { mutableStateOf(Offset.Zero) }
    val dotSizeDp = 20.dp
    val paddingDp = 4.dp
    val boxPadding = 16.dp
    val moveOffsetDp = (dotSizeDp + paddingDp * 2 - boxPadding) / 2
    val moveOffset = with(LocalDensity.current) { moveOffsetDp.toPx() }
    val result = remember{quizState.gradeQuiz()}

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
                AnswerShower(
                    isCorrect = result,
                    contentAlignment = Alignment.CenterStart
                ){
                    quizStyleManager.GetTextComposable(TextStyles.QUESTION, quizState.question, modifier = Modifier.fillMaxWidth())
                }
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
                            AnswerShower(
                                isCorrect = quizState.connectionAnswerIndex[index] == quizState.userConnectionIndex[index],
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    quizStyleManager.GetTextComposable(
                                        TextStyles.ANSWER,
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
                                        boxPosition = boxPosition,
                                        dotSize = dotSizeDp,
                                        padding = paddingDp,
                                        moveOffset = moveOffset,
                                        key = "QuizCreatorLeftDot$index"
                                    )
                                }
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
        DrawLines(
            leftDots = quizState.leftDots,
            rightDots = quizState.rightDots,
            connections = quizState.connectionAnswerIndex,
            color = MaterialTheme.colorScheme.error
        )
        DrawLines(
            leftDots = quizState.leftDots,
            rightDots = quizState.rightDots,
            connections = quizState.userConnectionIndex
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz4CheckerPreview() {
    val quiz4ViewModel: Quiz4ViewModel = viewModel()
    quiz4ViewModel.loadQuiz(sampleQuiz4)

    Quiz4Checker(
        quiz = quiz4ViewModel,
        quizStyleManager = TextStyleManager()
    )
}