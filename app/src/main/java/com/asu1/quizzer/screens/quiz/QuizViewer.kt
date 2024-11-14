package com.asu1.quizzer.screens.quiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import com.asu1.quizzer.model.sampleQuiz1
import com.asu1.quizzer.model.sampleQuiz2
import com.asu1.quizzer.model.sampleQuiz3
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
) {
    when(quiz){
        is Quiz1 -> {
            Quiz1Viewer(
                quiz = quiz,
                quizTheme = quizTheme,
                toggleUserAnswer = {
                    updateQuiz1(it)
                }
            )
        }
        is Quiz2 -> {
            Quiz2Viewer(
                quiz = quiz,
                quizTheme = quizTheme,
                onUserInput = {
                    updateQuiz2(it)
                }
            )
        }
        is Quiz3 -> {
            Quiz3Viewer(
                quiz = quiz,
                quizTheme = quizTheme,
                onUserInput = {first, second ->
                    updateQuiz3(first, second)
                }
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
                }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz1() {
    QuizViewer(
        quiz = sampleQuiz1
    )
}

@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz2() {
    QuizViewer(
        quiz = sampleQuiz2
    )
}

@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz3(){
    QuizViewer(
        quiz = sampleQuiz3
    )
}