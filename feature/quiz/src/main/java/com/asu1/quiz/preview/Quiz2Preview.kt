package com.asu1.quiz.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.QuizTheme
import com.asu1.quiz.creator.CalendarWithFocusDates
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.resources.R

@Composable
fun Quiz2Preview(
    quiz: Quiz2,
    quizTheme: QuizTheme = QuizTheme(),
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        CalendarWithFocusDates(
            focusDates = quiz.userAnswerDate.toSet(),
            onDateClick = { date ->
            },
            currentMonth = quiz.centerDate,
            colorScheme = quizTheme.colorScheme,
            bodyTextStyle = quizTheme.bodyTextStyle,
            isPreview = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnswerTextStyle.GetTextComposable(
            stringResource(R.string.selected_answers), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        quiz.userAnswerDate.forEachIndexed { index, date ->
            AnswerTextStyle.GetTextComposable(
                buildString {
                    append("${index + 1}. ")
                    append(date)
                }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
