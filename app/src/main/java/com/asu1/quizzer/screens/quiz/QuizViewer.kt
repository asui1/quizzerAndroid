package com.asu1.quizzer.screens.quiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import com.asu1.quizzer.model.QuizType
import com.asu1.quizzer.model.sampleQuiz1
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.QuizTheme
import com.asu1.quizzer.viewModels.quizModels.Quiz1ViewModel

@Composable
fun QuizViewer(
    quizTheme: QuizTheme = QuizTheme(),
    quiz: Quiz,
    updateQuiz: (Quiz) -> Unit = {},
) {
    when(quiz){
        is Quiz1 -> {
            val quiz1ViewModel: Quiz1ViewModel = viewModel()
            quiz1ViewModel.loadQuiz(quiz)
            Quiz1Viewer(
                quiz = quiz1ViewModel,
                quizTheme = quizTheme,
                onExit = {
                    updateQuiz(it)
                }
            )
        }
        is Quiz2 -> {
//            Quiz2Viewer(
//                quiz = viewModel(),
//                quizTheme = quizTheme
//            )
        }
        is Quiz3 -> {
//            Quiz3Viewer(
//                quiz = viewModel(),
//                quizTheme = quizTheme
//            )
        }
        is Quiz4 -> {
//            Quiz4Viewer(
//                quiz = viewModel(),
//                quizTheme = quizTheme
//            )
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