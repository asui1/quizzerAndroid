package com.asu1.quiz.content.multipleChoiceQuiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.content.AnswerShower
import com.asu1.quiz.content.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun MultipleChoiceQuizChecker(quiz: MultipleChoiceQuiz) {
    val selections = remember {quiz.userSelections.toMutableStateList() }
    QuizBase(quiz = quiz, mode = QuizMode.Checker) {
        MultipleChoiceQuizBody(
            displayedOptions = quiz.displayedOptions,
            selections = selections,
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