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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.sampleQuiz2
import com.asu1.quiz.creator.CalendarWithFocusDates
import com.asu1.quiz.ui.TextStyleManager
import com.asu1.quiz.viewmodel.quiz.Quiz2ViewModel

import com.asu1.resources.R
import com.asu1.resources.TextStyles
import java.time.LocalDate

@Composable
fun Quiz2Viewer(
    quiz: Quiz2,
    quizTheme: QuizTheme = QuizTheme(),
    onUserInput: (LocalDate) -> Unit = {},
    isPreview: Boolean = false,
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
        userScrollEnabled = !isPreview,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            TextStyleManager.GetTextComposable(TextStyles.QUESTION, quiz.question, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
                CalendarWithFocusDates(
                    focusDates = userAnswers.toSet(),
                    onDateClick = { date ->
                        if(isPreview) return@CalendarWithFocusDates
                        onUserInput(date)
                        updateLocalUserAnswer(date)
                    },
                    currentMonth = quiz.centerDate,
                    colorScheme = quizTheme.colorScheme,
                    bodyTextStyle = quizTheme.bodyTextStyle,
                    isPreview = isPreview,
                )
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
            TextStyleManager.GetTextComposable(TextStyles.ANSWER,
                stringResource(R.string.selected_answers), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(userAnswers.size){
            TextStyleManager.GetTextComposable(TextStyles.ANSWER, buildString {
                append("${it + 1}. ")
                append(userAnswers[it])
            }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
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
