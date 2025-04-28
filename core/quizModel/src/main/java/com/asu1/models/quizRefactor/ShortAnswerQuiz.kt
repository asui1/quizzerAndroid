package com.asu1.models.quizRefactor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.BodyTypeSerializer
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
    val answer: String = "",
    @Serializable(with = BodyTypeSerializer::class)
    override var bodyValue: BodyType = BodyType.NONE,
) : Quiz() {
    /** Runtime‐only user input, not serialized */
    var userAnswer by mutableStateOf("")
    @Transient
    override val uuid: String = UUID.randomUUID().toString()
    @Transient
    override val quizType: QuizType = QuizType.QUIZ5

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
        bodyValue  = bodyType,
    ).also { it.initViewState() }
}