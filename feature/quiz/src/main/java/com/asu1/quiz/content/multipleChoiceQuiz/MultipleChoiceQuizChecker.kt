package com.asu1.quiz.content.multipleChoiceQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.checker.AnswerShower
import com.asu1.quiz.content.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun MultipleChoiceQuizChecker(quiz: MultipleChoiceQuiz) {
    QuizBase(
        quiz = quiz,
        mode = QuizMode.Checker,
    ) {
        quiz.displayedOptions.forEachIndexed { idx, option ->
            AnswerShower(
                isCorrect = quiz.correctFlags[idx] == quiz.userSelections[idx],
                showChecker = quiz.correctFlags[idx] || quiz.userSelections[idx],
                contentAlignment = Alignment.CenterStart
            ) {
                MultipleChoiceOptionRow(
                    text = option,
                    checked = quiz.userSelections[idx],
                    enabled = false,
                    onChecked = {}
                )
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