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
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.sampleDateSelectionQuiz
import com.asu1.quiz.ui.CalendarWithFocusDates
import com.asu1.quiz.ui.Quiz2SelectionViewer
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.kizitonwose.calendar.core.yearMonth

@Composable
fun DateSelectionQuizChecker(
    quiz: DateSelectionQuiz,
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
                focusDates = quiz.userDates,
                onDateClick = {
                },
                currentMonth = quiz.centerDate.yearMonth,
                colorScheme = quizTheme.colorScheme,
            )
        }
        item{
            Quiz2SelectionViewer(
                answerDate = quiz.userDates,
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
    DateSelectionQuizChecker(
        quiz = sampleDateSelectionQuiz,
    )
}