package com.asu1.quiz.viewer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.sampleDateSelectionQuiz
import com.asu1.quiz.ui.CalendarWithFocusDates
import com.asu1.quiz.ui.Quiz2SelectionViewer
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewmodel.quiz.DateSelectionQuizViewModel
import com.kizitonwose.calendar.core.yearMonth
import java.time.LocalDate

@Composable
fun DateSelectionQuizViewer(
    quiz: DateSelectionQuiz,
    quizTheme: QuizTheme = QuizTheme(),
    onUserInput: (LocalDate) -> Unit = {},
)
{
    val userAnswers = remember { mutableStateListOf(*quiz.userDates.toTypedArray()) }
    fun updateLocalUserAnswer(localDate: LocalDate){
        if(userAnswers.contains(localDate)) {
            userAnswers.remove(localDate)
        }else{
            userAnswers.add(localDate)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
                CalendarWithFocusDates(
                    focusDates = userAnswers.toSet(),
                    onDateClick = { date ->
                        onUserInput(date)
                        updateLocalUserAnswer(date)
                    },
                    currentMonth = quiz.centerDate.yearMonth,
                    colorScheme = quizTheme.colorScheme,
                )
        }
        item{
            Quiz2SelectionViewer(
                answerDate = userAnswers.toSet(),
                updateDate = { date ->
                    onUserInput(date)
                    updateLocalUserAnswer(date)
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewDateSelectionQuizViewer(){
    val dateSelectionQuizViewModel: DateSelectionQuizViewModel = viewModel()
    dateSelectionQuizViewModel.loadQuiz(sampleDateSelectionQuiz)

    DateSelectionQuizViewer(
        quiz = sampleDateSelectionQuiz,
        quizTheme = QuizTheme(),
        onUserInput = { },
    )
}
