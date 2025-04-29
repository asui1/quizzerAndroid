package com.asu1.quiz.content.multipleChoiceQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.content.quizCommonBuilder.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun MultipleChoiceQuizPreview(quiz: MultipleChoiceQuiz) {
    QuizBase(quiz = quiz, mode = QuizMode.Preview) {
        MultipleChoiceQuizBody(
            displayedOptions = quiz.displayedOptions,
            selections = quiz.userSelections,
            enabled = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMultipleChoiceQuizPreview(){
    QuizzerAndroidTheme {
        MultipleChoiceQuizPreview(
            quiz = sampleMultipleChoiceQuiz,
        )
    }
}