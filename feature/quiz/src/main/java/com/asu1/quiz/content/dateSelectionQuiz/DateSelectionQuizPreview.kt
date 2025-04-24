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
import com.asu1.resources.QuizzerAndroidTheme
import com.kizitonwose.calendar.core.yearMonth

@Composable
fun DateSelectionQuizPreview(
    quiz: DateSelectionQuiz
) {
    QuizBase(
        quiz = quiz,
        mode = QuizMode.Preview
    ) {
        // calendar: clicks are no‐ops in preview
        CalendarWithFocusDates(
            focusDates   = quiz.userDates,
            onDateClick  = { /* no-op */ },
            currentMonth = quiz.centerDate.yearMonth,
        )
        Spacer(Modifier.height(16.dp))
        // show selections, but non‐interactive
        DateSelectionViewer(
            answerDate    = quiz.userDates,
            updateDate    = { /* no-op */ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateSelectionQuizPreview(){
    QuizzerAndroidTheme {
        DateSelectionQuizPreview(
            sampleDateSelectionQuiz
        )
    }
}