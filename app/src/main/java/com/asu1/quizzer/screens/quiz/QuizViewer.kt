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
import com.asu1.quizzer.viewModels.quizModels.Quiz1ViewModel
import com.asu1.quizzer.viewModels.quizModels.Quiz2ViewModel
import com.asu1.quizzer.viewModels.quizModels.Quiz3ViewModel
import com.asu1.quizzer.viewModels.quizModels.Quiz4ViewModel

@Composable
fun QuizViewer(
    quizTheme: QuizTheme = QuizTheme(),
    quiz: Quiz,
    updateUserInput: (Quiz) -> Unit = {},
    isPreview: Boolean = false,
) {
    fun updateQuiz(quiz: Quiz){
        if(!isPreview){
            updateUserInput(quiz)
        }
    }
    when(quiz){
        is Quiz1 -> {
            val quiz1ViewModel: Quiz1ViewModel = viewModel(
                key = quiz.uuid
            )
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
            val quiz2ViewModel: Quiz2ViewModel = viewModel()
            quiz2ViewModel.loadQuiz(quiz)
            Quiz2Viewer(
                quiz = quiz2ViewModel,
                quizTheme = quizTheme,
                onExit = {
                    updateQuiz(it)
                }
            )
        }
        is Quiz3 -> {
            val quiz3ViewModel: Quiz3ViewModel = viewModel()
            quiz3ViewModel.loadQuiz(quiz)
            Quiz3Viewer(
                quiz = quiz3ViewModel,
                quizTheme = quizTheme,
                onExit = {
                    updateQuiz(it)
                }
            )
        }
        is Quiz4 -> {
            val quiz4ViewModel: Quiz4ViewModel = viewModel()
            quiz4ViewModel.loadQuiz(quiz)
            Quiz4Viewer(
                quiz = quiz4ViewModel,
                quizTheme = quizTheme,
                onExit = {
                    updateQuiz(it)
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