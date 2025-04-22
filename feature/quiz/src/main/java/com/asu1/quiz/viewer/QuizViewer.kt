package com.asu1.quiz.viewer

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.models.sampleDateSelectionQuiz
import com.asu1.models.sampleReorderQuiz
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates

@Composable
fun QuizViewer(
    quiz: Quiz,
    quizTheme: QuizTheme = QuizTheme(),
    updateQuiz: (QuizUserUpdates) -> Unit = {},
) {
    when(quiz){
        is MultipleChoiceQuiz -> {
            MultipleChoiceQuizViewer(
                quiz = quiz,
                toggleUserAnswer = {
                    updateQuiz(QuizUserUpdates.MultipleChoiceQuizUpdate(it))
                },
            )
        }
        is DateSelectionQuiz -> {
            DateSelectionQuizViewer(
                quiz = quiz,
                quizTheme = quizTheme,
                onUserInput = {
                    updateQuiz(QuizUserUpdates.DateSelectionQuizUpdate(it))
                },
            )
        }
        is ReorderQuiz -> {
            ReorderQuizViewer(
                quiz = quiz,
                onUserInput = {first, second ->
                    updateQuiz(QuizUserUpdates.ReorderQuizUpdate(first, second))
                },
            )
        }
        is ConnectItemsQuiz -> {
            ConnectItemsQuizViewer(
                quiz = quiz,
                quizTheme = quizTheme,
                onUpdate = {items ->
                    updateQuiz(QuizUserUpdates.ConnectItemQuizUpdate(items))
                }
                )
        }
        is ShortAnswerQuiz -> {
            TODO()
        }
        is FillInBlankQuiz -> {
            TODO()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz1() {
    QuizViewer(
        quiz = sampleMultipleChoiceQuiz,
    )
}

@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz2() {
    QuizViewer(
        quiz = sampleDateSelectionQuiz,
    )
}

@Preview(showBackground = true)
@Composable
fun ViewerPreviewQuiz3(){
    QuizViewer(
        quiz = sampleReorderQuiz,
    )
}