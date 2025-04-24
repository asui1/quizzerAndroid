package com.asu1.quiz.viewer

import androidx.compose.runtime.Composable
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.quiz.content.dateSelectionQuiz.DateSelectionQuizViewer
import com.asu1.quiz.content.multipleChoiceQuiz.MultipleChoiceQuizViewer
import com.asu1.quiz.content.reorderQuiz.ReorderQuizViewer
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
                onUserInput = { userUpdate ->
                    updateQuiz(userUpdate)
                }
            )
        }
        is DateSelectionQuiz -> {
            DateSelectionQuizViewer(
                quiz = quiz,
                onUserInput = { userUpdate ->
                    updateQuiz(userUpdate)
                }
            )
        }
        is ReorderQuiz -> {
            ReorderQuizViewer(
                quiz = quiz,
                onUserInput = { userUpdate ->
                    updateQuiz(userUpdate)
                }
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
            ShortAnswerQuizViewer(
                quiz = quiz,
                updateUserAnswer = { answer ->
                    updateQuiz(QuizUserUpdates.ShortAnswerQuizUpdate(answer))
                }
            )
        }
        is FillInBlankQuiz -> {
            FillInBlankQuizViewer(
                quiz = quiz,
                updateUserAnswer = { answer ->

                },
            )
        }
    }
}
