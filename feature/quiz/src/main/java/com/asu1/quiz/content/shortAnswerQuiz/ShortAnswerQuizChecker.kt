package com.asu1.quiz.content.shortAnswerQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.sampleShortAnswerQuiz
import com.asu1.quiz.content.quizCommonBuilder.AnswerShower
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.quiz.content.QuizMode
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun ShortAnswerQuizChecker(
    quiz: ShortAnswerQuiz
) {
    QuizViewerBase(
        quiz = quiz,
        mode = QuizMode.Viewer,
    ) {
        AnswerShower(
            isCorrect = quiz.gradeQuiz()
        ) {
            ShortAnswerBody(
                userAnswer = quiz.userAnswer,
                onValueChange = {  },
                onDone = {  },
                enabled = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShortAnswerQuizChecker(){
    QuizzerAndroidTheme {
        ShortAnswerQuizChecker(
            quiz = sampleShortAnswerQuiz,
        )
    }
}
