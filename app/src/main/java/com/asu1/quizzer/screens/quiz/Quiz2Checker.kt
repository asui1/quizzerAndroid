package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.sampleQuiz2
import com.asu1.quizzer.composables.AnswerShower
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.resources.R
import com.asu1.resources.TextStyles

@Composable
fun Quiz2Checker(
    quiz: Quiz2,
    quizTheme: QuizTheme = QuizTheme(),
    quizStyleManager: TextStyleManager,
)
{
    val result = remember{quiz.gradeQuiz()}
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            AnswerShower(
                isCorrect = result,
                contentAlignment = Alignment.CenterStart
            ){
                quizStyleManager.GetTextComposable(TextStyles.QUESTION, quiz.question, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            CalendarWithFocusDates(
                focusDates = quiz.userAnswerDate,
                onDateClick = {
                },
                currentMonth = quiz.centerDate,
                colorScheme = quizTheme.colorScheme,
                bodyTextStyle = quizTheme.bodyTextStyle,
                isPreview = true,
            )
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
            quizStyleManager.GetTextComposable(
                TextStyles.ANSWER,
                stringResource(R.string.selected_answers), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            quiz.userAnswerDate.forEachIndexed { index, localDate ->
                AnswerShower(
                    isCorrect = quiz.answerDate.contains(localDate),
                    contentAlignment = Alignment.CenterStart,
                ){
                    quizStyleManager.GetTextComposable(TextStyles.ANSWER, buildString {
                        append("${index + 1}. ")
                        append(localDate)
                    }, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        item{
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            quiz.answerDate.forEach{localDate ->
                if(!quiz.userAnswerDate.contains(localDate)){
                    AnswerShower(
                        isCorrect = false,
                        contentAlignment = Alignment.CenterStart,
                    ){
                        quizStyleManager.GetTextComposable(TextStyles.ANSWER, localDate.toString(), modifier = Modifier.fillMaxWidth())
                    }
                    Spacer(modifier = Modifier.height(4.dp))                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz2CheckerPreview(){
    Quiz2Checker(
        quiz = sampleQuiz2,
        quizStyleManager = TextStyleManager()
    )
}