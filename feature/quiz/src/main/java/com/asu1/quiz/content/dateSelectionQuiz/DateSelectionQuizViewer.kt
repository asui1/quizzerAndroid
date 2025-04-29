package com.asu1.quiz.content.dateSelectionQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.sampleDateSelectionQuiz
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.quiz.content.QuizMode
import com.kizitonwose.calendar.core.yearMonth

@Composable
fun DateSelectionQuizViewer(
    quiz: DateSelectionQuiz,
)
{
    QuizViewerBase(
        quiz = quiz,
        mode = QuizMode.Viewer
    ){
        DateSelectionQuizBody(
            answerDates = quiz.userDates,
            correctAnswers = quiz.answerDate,
            onDateClick = { date ->
                quiz.toggleUserAnswer(date)
            },
            markAnswers = false,
            currentMonth = quiz.centerDate.yearMonth,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateSelectionQuizViewer(){
    DateSelectionQuizViewer(
        quiz = sampleDateSelectionQuiz,
    )
}
