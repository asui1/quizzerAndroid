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
import com.kizitonwose.calendar.core.yearMonth

@Composable
fun DateSelectionQuizChecker(
    quiz: DateSelectionQuiz
) {
    QuizBase(
        quiz = quiz,
        mode = QuizMode.Checker
    ) {
        // calendar: still read‐only
        CalendarWithFocusDates(
            focusDates   = quiz.userDates,
            onDateClick  = { /* no-op */ },
            currentMonth = quiz.centerDate.yearMonth,
        )
        Spacer(Modifier.height(16.dp))
        DateSelectionViewer(
            answerDate      = quiz.userDates,
            updateDate      = { /* no-op */ },
            markAnswers     = true,
            correctAnswers  = quiz.answerDate
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateSelectionQuizChecker(){
    DateSelectionQuizChecker(
        sampleDateSelectionQuiz
    )
}