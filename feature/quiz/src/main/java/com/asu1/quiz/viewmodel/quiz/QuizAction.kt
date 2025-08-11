package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.serializers.BodyType

sealed class QuizAction {
    data class UpdateQuestion(val value: String): QuizAction()
    data class UpdateBody(val body: BodyType): QuizAction()
    data class Load(val quiz: Quiz): QuizAction()
    data object Reset: QuizAction()
    data object ViewerInit: QuizAction()
}
