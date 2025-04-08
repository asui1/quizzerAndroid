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
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.sampleQuiz2
import com.asu1.quiz.ui.CalendarWithFocusDates
import com.asu1.quiz.ui.Quiz2SelectionViewer
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle

@Composable
fun Quiz2Checker(
    quiz: Quiz2,
    quizTheme: QuizTheme = QuizTheme(),
)
{
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
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            CalendarWithFocusDates(
                focusDates = quiz.userAnswerDate,
                onDateClick = {
                },
                currentMonth = quiz.centerDate,
                colorScheme = quizTheme.colorScheme,
                isPreview = true,
            )
        }
        item{
            Quiz2SelectionViewer(
                answerDate = quiz.userAnswerDate,
                updateDate = {},
                markAnswers = true,
                correctAnswers = quiz.answerDate,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz2CheckerPreview(){
    Quiz2Checker(
        quiz = sampleQuiz2,
    )
}