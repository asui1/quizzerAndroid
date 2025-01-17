package com.asu1.quizzer.screens.quiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.Quiz3
import com.asu1.models.quiz.Quiz4
import com.asu1.models.sampleQuiz1
import com.asu1.models.sampleQuiz2
import com.asu1.models.sampleQuiz3
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.quizzer.viewModels.QuizTheme
import com.asu1.quizzer.viewModels.quizModels.Quiz4ViewModel
import java.time.LocalDate

@Composable
fun QuizViewer(
    quiz: Quiz,
    quizTheme: QuizTheme = QuizTheme(),
    updateQuiz1: (Int) -> Unit = {},
    updateQuiz2: (LocalDate) -> Unit = {},
    updateQuiz3: (Int, Int) -> Unit = {_, _ ->},
    updateQuiz4: (Int, Int?) -> Unit = {_, _ ->},
    quizStyleManager: TextStyleManager,
    isPreview: Boolean = false,
) {
    when(quiz){
        is Quiz1 -> {
            Quiz1Viewer(
                quiz = quiz,
                toggleUserAnswer = {
                    updateQuiz1(it)
                },
                quizStyleManager = quizStyleManager,
                isPreview = isPreview,
            )
        }
        is Quiz2 -> {
            Quiz2Viewer(
                quiz = quiz,
                quizTheme = quizTheme,
                onUserInput = {
                    updateQuiz2(it)
                },
                quizStyleManager = quizStyleManager,
                isPreview = isPreview,
            )
        }
        is Quiz3 -> {
            Quiz3Viewer(
                quiz = quiz,
                onUserInput = {first, second ->
                    updateQuiz3(first, second)
                },
                quizStyleManager = quizStyleManager,
                isPreview = isPreview,
            )
        }
        is Quiz4 -> {
            val quiz4ViewModel: Quiz4ViewModel = viewModel(
                key = quiz.uuid
            )
            quiz4ViewModel.loadQuiz(quiz)
            Quiz4Viewer(
                quiz = quiz4ViewModel,
                quizTheme = quizTheme,
                onUserInput = {first, second ->
                    updateQuiz4(first, second)
                },
                quizStyleManager = quizStyleManager,
                isPreview = isPreview,
                )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz1() {
    QuizViewer(
        quiz = sampleQuiz1,
        quizStyleManager = TextStyleManager()
    )
}

@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz2() {
    QuizViewer(
        quiz = sampleQuiz2,
        quizStyleManager = TextStyleManager()
    )
}

@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz3(){
    QuizViewer(
        quiz = sampleQuiz3,
        quizStyleManager = TextStyleManager()
    )
}