package com.asu1.quiz.checker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.Quiz3
import com.asu1.models.sampleQuiz3
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody

@Composable
fun Quiz3Checker(
    quiz: Quiz3,
) {
    val result = remember{quiz.gradeQuiz()}
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
                QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(16.dp))
            BuildBody(
                quizBody = quiz.bodyType,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnswerTextStyle.GetTextComposable(quiz.shuffledAnswers[0], modifier = Modifier.fillMaxWidth().padding(8.dp))
        }
        items(quiz.answers.size-1, key = {it}){it ->
            val newIndex = it + 1
            val item = quiz.shuffledAnswers[newIndex].replace(Regex("Q!Z2\\d+$"), "")
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Reorder",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                AnswerShower(
                    isCorrect = quiz.answers[newIndex] == item,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AnswerTextStyle.GetTextComposable(
                            item,
                            modifier = Modifier.weight(1f).padding(8.dp)
                        )
                        IconButton(
                            onClick = {},
                        ) {
                            Icon(Icons.Rounded.DragHandle, contentDescription = "Reorder")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz3CheckerPreview(){
    Quiz3Checker(
        quiz = sampleQuiz3,
    )
}