package com.asu1.quiz.content.multipleChoiceQuiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.content.QuizViewer
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun MultipleChoiceQuizViewer(
    quiz: MultipleChoiceQuiz,
    updateQuiz: (QuizUserUpdates) -> Unit
) = QuizViewer(
    quiz             = quiz,
    updateQuiz       = updateQuiz,
    initialSelections = { quiz.userSelections.toMutableStateList() }
) { selections ->
    MultipleChoiceQuizBody(
        displayedOptions = displayedOptions,
        selections       = selections,
        enabled          = true,
        onChecked        = { idx ->
            selections[idx] = !selections[idx]
            updateQuiz(QuizUserUpdates.MultipleChoiceQuizUpdate(idx))
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewMultipleChoiceQuiz(){
    QuizzerAndroidTheme {
        MultipleChoiceQuizViewer(
            quiz = sampleMultipleChoiceQuiz,
            updateQuiz = {},
        )
    }
}