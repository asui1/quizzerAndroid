package com.asu1.quiz.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.quiz.ui.CalendarWithFocusDates
import com.asu1.quiz.ui.Quiz2SelectionViewer
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.kizitonwose.calendar.core.yearMonth

@Composable
fun DateSelectionQuizPreview(
    quiz: DateSelectionQuiz,
    quizTheme: QuizTheme = QuizTheme(),
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        CalendarWithFocusDates(
            focusDates = quiz.userDates,
            onDateClick = { date ->
            },
            currentMonth = quiz.centerDate.yearMonth,
            colorScheme = quizTheme.colorScheme,
        )
        Quiz2SelectionViewer(
            answerDate = quiz.userDates,
            updateDate = { date ->
            }
        )
    }
}
