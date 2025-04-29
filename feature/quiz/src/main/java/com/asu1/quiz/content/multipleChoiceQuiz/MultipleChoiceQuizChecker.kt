package com.asu1.quiz.content.multipleChoiceQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.content.quizCommonBuilder.AnswerShower
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.quiz.content.QuizMode
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun MultipleChoiceQuizChecker(quiz: MultipleChoiceQuiz) {
    QuizViewerBase(quiz = quiz, mode = QuizMode.Checker) {
        MultipleChoiceQuizBody(
            displayedOptions = quiz.displayedOptions,
            selections = quiz.userSelections,
            enabled = false
        ) { idx, content ->
            AnswerShower(
                isCorrect         = quiz.correctFlags[idx] == quiz.userSelections[idx],
                showChecker       = quiz.correctFlags[idx] || quiz.userSelections[idx],
                contentAlignment  = Alignment.CenterStart
            ) {
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMultipleChoiceQuizChecker(){
    QuizzerAndroidTheme {
        MultipleChoiceQuizChecker(
            quiz = sampleMultipleChoiceQuiz,
        )
    }
}