package com.asu1.quiz.content

import androidx.compose.runtime.Composable
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.quiz.content.connectItemQuiz.ConnectItemQuizPreview
import com.asu1.quiz.content.dateSelectionQuiz.DateSelectionQuizPreview
import com.asu1.quiz.content.multipleChoiceQuiz.MultipleChoiceQuizPreview
import com.asu1.quiz.content.reorderQuiz.ReorderQuizPreview
import com.asu1.quiz.content.shortAnswerQuiz.ShortAnswerQuizPreview

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
            ConnectItemQuizPreview(quiz)
        }
        is ShortAnswerQuiz -> {
            ShortAnswerQuizPreview(quiz)
        }
        is FillInBlankQuiz -> {
            TODO()
        }
    }
}