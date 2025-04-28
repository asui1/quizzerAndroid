package com.asu1.quiz.content.multipleChoiceQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.content.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun MultipleChoiceQuizViewer(
    quiz: MultipleChoiceQuiz
) {
    QuizBase(
        quiz = quiz,
        mode = QuizMode.Viewer,
    ) {
        quiz.displayedOptions.forEachIndexed { idx, option ->
            MultipleChoiceOptionRow(
                text = option,
                checked = quiz.userSelections[idx],
                enabled = true,
                onChecked = {
                    quiz.userSelections[idx] = !quiz.userSelections[idx]
//                    onUserInput(QuizUserUpdates.MultipleChoiceQuizUpdate(idx))
                }
            )
        }
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