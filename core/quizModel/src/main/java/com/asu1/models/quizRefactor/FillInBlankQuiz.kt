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
@SerialName("5")
data class FillInBlankQuiz(
    override var question: String = "",
    val answers: List<String> = listOf(""),
    val fillInAnswers: List<String> = emptyList(),
    @SerialName("bodyValue")
    override var bodyType: BodyType = BodyType.NONE,
    override val uuid: String = UUID.randomUUID().toString(),
    override val layoutType: QuizType = QuizType.QUIZ6
) : Quiz(
) {
    /** Transient UI state: not serialized **/
    @Transient
    var userAnswers: MutableList<String> = mutableListOf()

    override fun initViewState() {
        userAnswers = MutableList(fillInAnswers.size) { "" }
    }

    override fun validateQuiz(): QuizError {
        if (question.isBlank())                          return QuizError.EMPTY_QUESTION
        if (answers.isEmpty() || answers.any(String::isBlank))
            return QuizError.EMPTY_ANSWER
        if (fillInAnswers.isEmpty() || fillInAnswers.any(String::isBlank))
            return QuizError.EMPTY_ANSWER
        return QuizError.NO_ERROR
    }

    override fun gradeQuiz(): Boolean =
        fillInAnswers.indices.all { idx ->
            normalizeMixedInputPrecisely(userAnswers[idx]) ==
                    normalizeMixedInputPrecisely(fillInAnswers[idx])
        }

    override fun cloneQuiz(
        question: String,
        bodyType: BodyType,
        uuid: String
    ): Quiz = copy(
        question       = question,
        answers        = answers,
        fillInAnswers  = fillInAnswers,
        bodyType       = bodyType,
        uuid           = uuid
    ).also { it.initViewState() }
}