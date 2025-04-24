package com.asu1.quiz.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.Quiz
import com.asu1.quiz.checker.AnswerShower
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody

@Composable
fun QuizBase(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    mode: QuizMode,
    quizBody: @Composable () -> Unit = {},
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (mode == QuizMode.Checker) {
            // gradeQuiz() is on the base Quiz class
            AnswerShower(
                isCorrect = quiz.gradeQuiz(),
                contentAlignment = Alignment.CenterStart
            ) {
                QuestionTextStyle.GetTextComposable(
                    quiz.question,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            QuestionTextStyle.GetTextComposable(
                quiz.question,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(16.dp))
        BuildBody(quizBody = quiz.bodyValue)
        quizBody()
    }
}