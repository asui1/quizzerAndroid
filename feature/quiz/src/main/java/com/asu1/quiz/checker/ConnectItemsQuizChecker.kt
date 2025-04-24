package com.asu1.quiz.checker

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
import androidx.compose.runtime.mutableStateListOf
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
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.sampleConnectItemsQuiz
import com.asu1.quiz.creator.DraggableDot
import com.asu1.quiz.creator.DrawLines
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody
import com.asu1.quiz.viewer.boxPadding
import com.asu1.quiz.viewer.dotSizeDp
import com.asu1.quiz.viewer.moveOffsetDp
import com.asu1.quiz.viewer.paddingDp

@Composable
fun ConnectItemsQuizChecker(
    quiz: ConnectItemsQuiz
) {
    var boxPosition by remember { mutableStateOf(Offset.Zero) }
    val moveOffset = with(LocalDensity.current) { moveOffsetDp.toPx() }

    val leftDotOffsets = remember {
        mutableStateListOf<Offset>().apply {
            repeat(quiz.answers.size) { add(Offset.Zero) }
        }
    }
    val rightDotOffsets = remember {
        mutableStateListOf<Offset>().apply {
            repeat(quiz.connectionAnswers.size) { add(Offset.Zero) }
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
        ) {
            item {
                AnswerShower(
                    isCorrect = quiz.gradeQuiz(),
                    contentAlignment = Alignment.CenterStart
                ){
                    QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item{
                BuildBody(
                    quizBody = quiz.bodyValue,
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
                        quiz.answers.forEachIndexed { index, answer ->
                            AnswerShower(
                                isCorrect = quiz.connectionAnswerIndex[index] == quiz.userConnectionIndex[index],
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    AnswerTextStyle.GetTextComposable(
                                        quiz.answers[index],
                                        modifier = Modifier.weight(1f)
                                    )
                                    DraggableDot(
                                        setOffset = { offset ->
                                            leftDotOffsets[index] = offset
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
                        quiz.connectionAnswers.forEachIndexed{index, answer ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                DraggableDot(
                                    setOffset = { offset ->
                                        rightDotOffsets[index] = offset
                                    },
                                    boxPosition = boxPosition,
                                    dotSize = dotSizeDp,
                                    padding = paddingDp,
                                    moveOffset = moveOffset,
                                    key = "QuizCreatorRightDot$index"
                                )
                                AnswerTextStyle.GetTextComposable(
                                    quiz.connectionAnswers[index],
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
            leftDots = leftDotOffsets,
            rightDots = rightDotOffsets,
            connections = quiz.connectionAnswerIndex,
            color = MaterialTheme.colorScheme.error
        )
        DrawLines(
            leftDots = leftDotOffsets,
            rightDots = rightDotOffsets,
            connections = quiz.userConnectionIndex
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz4CheckerPreview() {
    ConnectItemsQuizChecker(
        quiz = sampleConnectItemsQuiz,
    )
}