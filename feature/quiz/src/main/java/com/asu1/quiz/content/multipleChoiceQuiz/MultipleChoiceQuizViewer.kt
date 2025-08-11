package com.asu1.quiz.content.multipleChoiceQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.quiz.content.QuizMode
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun MultipleChoiceQuizViewer(
    quiz: MultipleChoiceQuiz
){
    QuizViewerBase(quiz = quiz, mode = QuizMode.Preview) {
        MultipleChoiceQuizBody(
            displayedOptions = quiz.displayedOptions,
            selections = quiz.userSelections,
            enabled = true,
            onChecked = { index ->
                quiz.userSelections[index] = !quiz.userSelections[index]
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMultipleChoiceQuiz(){
    QuizzerAndroidTheme {
        MultipleChoiceQuizViewer(
            quiz = sampleMultipleChoiceQuiz,
        )
    }
}
