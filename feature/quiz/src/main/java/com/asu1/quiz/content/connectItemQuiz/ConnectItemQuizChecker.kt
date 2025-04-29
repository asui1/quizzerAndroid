package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.runtime.Composable
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.quiz.content.QuizMode

@Composable
fun ConnectItemsQuizChecker(
    quiz: ConnectItemsQuiz,
) {
    val (leftDots, rightDots) = rememberDotOffsets(quiz)

    QuizViewerBase(
        quiz = quiz,
        mode = QuizMode.Checker
    ) {
        ConnectItemsBody(
            quiz            = quiz,
            dragState       = null,
            leftDotOffsets  = leftDots,
            rightDotOffsets = rightDots
        )
    }
}