package com.asu1.quiz.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.asu1.models.quizRefactor.Quiz
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates

@Composable
inline fun <Q : Quiz, S> QuizViewer(
    quiz: Q,
    @Suppress("unused") updateQuiz: (QuizUserUpdates) -> Unit,
    noinline initialSelections: () -> S,
    crossinline content: @Composable Q.(S) -> Unit
) {
    val selections = remember(quiz) { initialSelections() }
    QuizBase(quiz = quiz, mode = QuizMode.Viewer) {
        // inside here we run your per-quiz body
        quiz.content(selections)
    }
}
