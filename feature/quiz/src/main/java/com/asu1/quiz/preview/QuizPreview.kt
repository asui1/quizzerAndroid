package com.asu1.quiz.preview

import androidx.compose.runtime.Composable
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz

@Composable
fun QuizPreview(
    quiz: Quiz,
) {
    when(quiz){
        is MultipleChoiceQuiz -> {
            MultipleChoiceQuizPreview(quiz)
        }
        is DateSelectionQuiz -> {
            DateSelectionQuizPreview(quiz)
        }
        is ReorderQuiz -> {
            ReorderQuizPreview(quiz)
        }
        is ConnectItemsQuiz -> {
            ConnectItemsQuizPreview(quiz)
        }
        is ShortAnswerQuiz -> {
            TODO()
        }
        is FillInBlankQuiz -> {
            TODO()
        }
    }
}