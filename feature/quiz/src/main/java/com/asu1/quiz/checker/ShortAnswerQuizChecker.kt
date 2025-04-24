package com.asu1.quiz.checker

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.sampleShortAnswerQuiz
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun ShortAnswerQuizChecker(
    quiz: ShortAnswerQuiz,
) {
    val result = remember { quiz.gradeQuiz() }
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
                quizBody = quiz.bodyValue,
            )
        }
        item{
            AnswerShower(
                isCorrect = result,
                content = {
                    AnswerTextStyle.GetTextComposable(
                        quiz.userAnswer
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShortAnswerQuizChecker(){
    QuizzerAndroidTheme {
        ShortAnswerQuizChecker(
            sampleShortAnswerQuiz
        )
    }
}