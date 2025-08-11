package com.asu1.quiz.content.fillInBlankQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.sampleFillInBlankQuiz
import com.asu1.quiz.content.QuizMode
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun FillInBlankQuizViewer(
    quiz: FillInBlankQuiz,
){
    val focusManager = LocalFocusManager.current

    QuizViewerBase(
        quiz = quiz,
        mode = QuizMode.Viewer
    ){
        FillInBlankQuizBody(
            quiz = quiz,
            focusManager = focusManager,
            enabled = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFillInBlankQuizViewer(){
    QuizzerAndroidTheme {
        FillInBlankQuizViewer(
            sampleFillInBlankQuiz
        )
    }
}
