package com.asu1.quiz.content.dateSelectionQuiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.sampleDateSelectionQuiz
import com.asu1.quiz.content.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates
import com.asu1.utils.toggle
import com.kizitonwose.calendar.core.yearMonth
import java.time.LocalDate

@Composable
fun DateSelectionQuizViewer(
    quiz: DateSelectionQuiz,
    updateQuiz: (QuizUserUpdates) -> Unit,
)
{
    val selections = remember {
        mutableStateSetOf<LocalDate>().apply {
            quiz.userDates
        }
    }
    QuizBase(
        quiz = quiz,
        mode = QuizMode.Viewer
    ){
        DateSelectionQuizBody(
            answerDates = selections,
            correctAnswers = quiz.answerDate,
            onDateClick = { date ->
                selections.toggle(date)
                updateQuiz(QuizUserUpdates.DateSelectionQuizUpdate(date))
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
        updateQuiz = {},
    )
}
