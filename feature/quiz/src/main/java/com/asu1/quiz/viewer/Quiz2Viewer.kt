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
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.sampleQuiz2
import com.asu1.quiz.ui.CalendarWithFocusDates
import com.asu1.quiz.ui.Quiz2SelectionViewer
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewmodel.quiz.Quiz2ViewModel
import java.time.LocalDate

@Composable
fun Quiz2Viewer(
    quiz: Quiz2,
    quizTheme: QuizTheme = QuizTheme(),
    onUserInput: (LocalDate) -> Unit = {},
)
{
    val userAnswers = remember { mutableStateListOf(*quiz.userAnswerDate.toTypedArray()) }
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
                    currentMonth = quiz.centerDate,
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
fun Quiz2ViewerPreview(){
    val quiz2ViewModel: Quiz2ViewModel = viewModel()
    quiz2ViewModel.loadQuiz(sampleQuiz2)

    Quiz2Viewer(
        quiz = sampleQuiz2,
        quizTheme = QuizTheme(),
        onUserInput = { },
    )
}
