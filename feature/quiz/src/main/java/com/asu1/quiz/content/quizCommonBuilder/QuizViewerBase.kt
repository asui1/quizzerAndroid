package com.asu1.quiz.content.quizCommonBuilder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.Quiz
import com.asu1.quiz.content.QuizMode
import com.asu1.quiz.content.BOX_PADDING
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.content.quizBodyBuilder.QuizBodyViewer

@Composable
fun QuizViewerBase(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    mode: QuizMode,
    quizBody: @Composable () -> Unit = {},
){
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(BOX_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (mode == QuizMode.Checker) {
            // gradeQuiz() is on the base Quiz class
            AnswerShower(
                isCorrect = quiz.gradeQuiz(),
                contentAlignment = Alignment.Center
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
        QuizBodyViewer(quizBody = quiz.bodyValue)
        quizBody()
    }
}