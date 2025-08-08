package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.sampleConnectItemsQuiz
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.quiz.content.QuizMode
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun ConnectItemsQuizViewer(
    quiz: ConnectItemsQuiz,
) {
    val (leftDots, rightDots) = rememberDotOffsets(quiz)
    val dragState = rememberDragState()

    QuizViewerBase(
        quiz = quiz,
        mode = QuizMode.Viewer
    ) {
        ConnectItemsBody(
            quiz            = quiz,
            dragState       = dragState,
            leftDotOffsets  = leftDots,
            rightDotOffsets = rightDots
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConnectItemsQuizViewer() {
    QuizzerAndroidTheme {
        ConnectItemsQuizViewer(
            quiz = sampleConnectItemsQuiz,
        )
    }
}
