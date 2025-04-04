package com.asu1.quiz.viewer

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.Quiz3
import com.asu1.models.quiz.Quiz4
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.sampleQuiz1
import com.asu1.models.sampleQuiz2
import com.asu1.models.sampleQuiz3
import com.asu1.quiz.viewmodel.quiz.Quiz4ViewModel
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates

@Composable
fun QuizViewer(
    quiz: Quiz<*>,
    quizTheme: QuizTheme = QuizTheme(),
    updateQuiz: (QuizUserUpdates) -> Unit = {},
    isPreview: Boolean = false,
) {
    when(quiz){
        is Quiz1 -> {
            Quiz1Viewer(
                quiz = quiz,
                toggleUserAnswer = {
                    updateQuiz(QuizUserUpdates.Quiz1Update(it))
                },
                isPreview = isPreview,
            )
        }
        is Quiz2 -> {
            Quiz2Viewer(
                quiz = quiz,
                quizTheme = quizTheme,
                onUserInput = {
                    updateQuiz(QuizUserUpdates.Quiz2Update(it))
                },
                isPreview = isPreview,
            )
        }
        is Quiz3 -> {
            Quiz3Viewer(
                quiz = quiz,
                onUserInput = {first, second ->
                    updateQuiz(QuizUserUpdates.Quiz3Update(first, second))
                },
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
                isPreview = isPreview,
                onUpdate = {items ->
                    updateQuiz(QuizUserUpdates.Quiz4Update(items))
                }
                )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz1() {
    QuizViewer(
        quiz = sampleQuiz1,
    )
}

@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz2() {
    QuizViewer(
        quiz = sampleQuiz2,
    )
}

@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz3(){
    QuizViewer(
        quiz = sampleQuiz3,
    )
}