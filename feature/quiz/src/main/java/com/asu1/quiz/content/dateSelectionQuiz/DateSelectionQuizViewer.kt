package com.asu1.quiz.content.dateSelectionQuiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.sampleDateSelectionQuiz
import com.asu1.quiz.content.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates
import com.kizitonwose.calendar.core.yearMonth

@Composable
fun DateSelectionQuizViewer(
    quiz: DateSelectionQuiz,
    onUserInput: (QuizUserUpdates) -> Unit
)
{
    QuizBase(
        quiz = quiz,
        mode = QuizMode.Viewer
    ){
        CalendarWithFocusDates(
            focusDates   = quiz.userDates,
            onDateClick  = { date ->
                onUserInput(QuizUserUpdates.DateSelectionQuizUpdate(date))
            },
            currentMonth = quiz.centerDate.yearMonth,
        )
        Spacer(Modifier.height(16.dp))
        DateSelectionViewer(
            answerDate   = quiz.userDates,
            updateDate   = { date ->
                onUserInput(QuizUserUpdates.DateSelectionQuizUpdate(date))
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateSelectionQuizViewer(){
    DateSelectionQuizViewer(
        quiz = sampleDateSelectionQuiz,
        onUserInput = { },
    )
}
