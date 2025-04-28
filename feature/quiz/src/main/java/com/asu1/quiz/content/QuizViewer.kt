package com.asu1.quiz.content

import androidx.compose.runtime.Composable
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.quiz.content.connectItemQuiz.ConnectItemsQuizViewer
import com.asu1.quiz.content.dateSelectionQuiz.DateSelectionQuizViewer
import com.asu1.quiz.content.fillInBlanklQuiz.FillInBlankQuizViewer
import com.asu1.quiz.content.multipleChoiceQuiz.MultipleChoiceQuizViewer
import com.asu1.quiz.content.reorderQuiz.ReorderQuizViewer
import com.asu1.quiz.content.shortAnswerQuiz.ShortAnswerQuizViewer

@Composable
fun QuizViewer(
    quiz: Quiz,
) {
    when(quiz){
        is MultipleChoiceQuiz -> {
            MultipleChoiceQuizViewer(
                quiz = quiz
            )
        }
        is DateSelectionQuiz -> {
            DateSelectionQuizViewer(
                quiz = quiz,
            )
        }
        is ReorderQuiz -> {
            ReorderQuizViewer(
                quiz = quiz
            )
        }
        is ConnectItemsQuiz -> {
            ConnectItemsQuizViewer(
                quiz
            )
        }
        is ShortAnswerQuiz -> {
            ShortAnswerQuizViewer(quiz)
        }
        is FillInBlankQuiz -> {
            FillInBlankQuizViewer(
                quiz = quiz,
            )
        }
    }
}
