package com.asu1.quiz.checker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.Quiz1
import com.asu1.models.sampleQuiz1
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody

@Composable
fun Quiz1Checker(
    quiz: Quiz1,
){
    val result = remember{quiz.gradeQuiz()}
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            AnswerShower(
                isCorrect = result,
                contentAlignment = Alignment.CenterStart
            ){
                QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            BuildBody(
                quizBody = quiz.bodyType,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(quiz.answers.size, key = {it}){ index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                AnswerShower(
                    isCorrect = quiz.ans[index] == quiz.userAns[index],
                    showChecker = quiz.ans[index] || quiz.userAns[index]
                ){
                    Checkbox(
                        checked = quiz.userAns[index],
                        onCheckedChange = {
                        },
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Checkbox at $index, and is ${if(quiz.userAns[index]) "checked" else "unchecked"}"
                            }
                            .scale(1.5f)
                    )
                }
                AnswerTextStyle.GetTextComposable(quiz.shuffledAnswers[index])
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewQuiz1Checker(){
    Quiz1Checker(
        quiz = sampleQuiz1,
    )
}