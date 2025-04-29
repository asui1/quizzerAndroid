package com.asu1.quiz.content.dateSelectionQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.sampleDateSelectionQuiz
import com.asu1.quiz.content.quizCommonBuilder.QuizBase
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
        DateSelectionQuizBody(
            answerDates = quiz.userDates,
            correctAnswers = quiz.answerDate,
            onDateClick = {},
            markAnswers = false,
            currentMonth = quiz.centerDate.yearMonth,
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