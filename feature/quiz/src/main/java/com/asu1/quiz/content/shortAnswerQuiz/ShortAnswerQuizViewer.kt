package com.asu1.quiz.content.shortAnswerQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.sampleShortAnswerQuiz
import com.asu1.quiz.content.QuizMode
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun ShortAnswerQuizViewer(
    quiz: ShortAnswerQuiz
) {
    val focusManager = LocalFocusManager.current
    QuizViewerBase(
        quiz = quiz,
        mode = QuizMode.Viewer,
    ) {
        ShortAnswerBody(
            userAnswer = quiz.userAnswer,
            onValueChange = { quiz.userAnswer = it },
            onDone = { focusManager.clearFocus() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShortAnswerQuiz(){
    QuizzerAndroidTheme {
        ShortAnswerQuizViewer(
            quiz = sampleShortAnswerQuiz,
        )
    }
}