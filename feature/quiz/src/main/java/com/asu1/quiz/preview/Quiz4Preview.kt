package com.asu1.quiz.preview

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.Quiz4
import com.asu1.quiz.creator.DraggableDot
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody

@Composable
fun Quiz4Preview(
    quiz: Quiz4,
) {
    val dotSizeDp = 20.dp
    val paddingDp = 4.dp
    val boxPadding = 16.dp
    val moveOffsetDp = (dotSizeDp + paddingDp * 2 - boxPadding) / 2
    val moveOffset = with(LocalDensity.current) { moveOffsetDp.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(boxPadding)
    ) {
        Column {
            QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            BuildBody(
                quizBody = quiz.bodyType,
            )
            Spacer(modifier = Modifier.height(8.dp))
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            AnswerTextStyle.GetTextComposable(
                                quiz.answers[index],
                                modifier = Modifier.weight(1f)
                            )
                            DraggableDot(
                                setOffset = { offset ->
                                },
                                pointerEvent = { it ->
                                },
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
                    quiz.connectionAnswers.forEachIndexed{index, answer ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            DraggableDot(
                                setOffset = { offset ->
                                },
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
}