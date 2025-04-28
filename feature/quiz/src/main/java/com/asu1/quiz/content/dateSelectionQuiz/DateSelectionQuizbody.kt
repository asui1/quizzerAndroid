package com.asu1.quiz.content.dateSelectionQuiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun DateSelectionQuizBody(
    onDateClick: (LocalDate) -> Unit,
    answerDates: Set<LocalDate>,
    markAnswers: Boolean = false,
    correctAnswers: Set<LocalDate> = emptySet(),
    currentMonth: YearMonth
) {
    CalendarWithFocusDates(
        focusDates   = answerDates,
        onDateClick  = onDateClick,
        currentMonth = currentMonth,
    )
    Spacer(Modifier.height(16.dp))
    SelectedDatesColumn(
        answerDate     = answerDates,
        updateDate     = onDateClick,
        markAnswers    = markAnswers,
        correctAnswers = correctAnswers
    )
}
