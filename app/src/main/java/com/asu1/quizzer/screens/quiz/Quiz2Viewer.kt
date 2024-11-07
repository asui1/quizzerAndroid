package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.GetTextStyle
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.sampleQuiz2
import com.asu1.quizzer.viewModels.QuizTheme
import com.asu1.quizzer.viewModels.quizModels.Quiz2ViewModel

@Composable
fun Quiz2Viewer(
    quiz: Quiz2ViewModel = viewModel(),
    quizTheme: QuizTheme = QuizTheme(),
    onExit: (Quiz2) -> Unit = {},
)
{
    val quiz2State by quiz.quiz2State.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            onExit(quiz2State)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            GetTextStyle(quiz2State.question, quizTheme.questionTextStyle, quizTheme.colorScheme, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            key(quiz2State.userAnswerDate, quiz2State.uuid, "viewer"){
                CalendarWithFocusDates(
                    focusDates = quiz2State.userAnswerDate,
                    onDateClick = { date ->
                        quiz.updateUserAnswerDate(date)
                    },
                    currentMonth = quiz2State.centerDate,
                    colorScheme = quizTheme.colorScheme,
                    bodyTextStyle = quizTheme.bodyTextStyle,
                )
            }
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
            GetTextStyle("Selected Answers", listOf(quizTheme.answerTextStyle[0], quizTheme.answerTextStyle[1], quizTheme.answerTextStyle[2], quizTheme.bodyTextStyle[3]), quizTheme.colorScheme, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(quiz2State.userAnswerDate.size){
            GetTextStyle("${it+1}. " + quiz2State.userAnswerDate[it], quizTheme.answerTextStyle, quizTheme.colorScheme, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

}

private operator fun <E> MutableSet<E>.get(it: Int): String {
    return this.elementAt(it).toString()
}

@Preview(showBackground = true)
@Composable
fun Quiz2ViewerPreview(){
    val quiz2ViewModel: Quiz2ViewModel = viewModel()
    quiz2ViewModel.loadQuiz(sampleQuiz2)

    Quiz2Viewer(
        quiz = quiz2ViewModel,
        quizTheme = QuizTheme()
    )
}
