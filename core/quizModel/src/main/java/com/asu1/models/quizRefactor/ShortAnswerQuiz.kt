package com.asu1.models.quizRefactor

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import com.asu1.utils.normalizeMixedInputPrecisely
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
@SerialName("4")
data class ShortAnswerQuiz(
    override var question: String = "",
    /** must match old JSON key `answer` */
    val answer: String = "",
    override var bodyType: BodyType = BodyType.NONE,
    override val uuid: String = UUID.randomUUID().toString(),
    override val layoutType: QuizType = QuizType.QUIZ5
) : Quiz() {
    /** Runtime‐only user input, not serialized */
    @Transient
    var userAnswer: String = ""

    override fun initViewState() {
        userAnswer = ""
    }

    override fun validateQuiz(): QuizError {
        if (question.isBlank())    return QuizError.EMPTY_QUESTION
        if (answer.isBlank())      return QuizError.EMPTY_ANSWER
        return QuizError.NO_ERROR
    }

    override fun gradeQuiz(): Boolean {
        return normalizeMixedInputPrecisely(answer) ==
                normalizeMixedInputPrecisely(userAnswer)
    }

    override fun cloneQuiz(
        question: String,
        bodyType: BodyType,
        uuid: String
    ): Quiz = copy(
        question  = question,
        answer    = answer,
        bodyType  = bodyType,
        uuid      = uuid
    ).also { it.initViewState() }
}