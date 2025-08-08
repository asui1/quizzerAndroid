package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.runtime.Composable
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.quiz.content.QuizMode

@Composable
fun ConnectItemQuizPreview(
    quiz: ConnectItemsQuiz,
) {
    QuizViewerBase(
        quiz = quiz,
        mode = QuizMode.Preview
    ) {
        ConnectItemsBody(
            quiz            = quiz,
            dragState       = null,
            leftDotOffsets  = null,
            rightDotOffsets = null
        )
    }
}
