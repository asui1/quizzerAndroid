package com.asu1.models.quizRefactor

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
@SerialName("2")
data class ReorderQuiz(
    override var question: String = "",
    val answers: List<String> = emptyList(),       // same JSON key
    override var bodyType: BodyType = BodyType.NONE,
    override val uuid: String = UUID.randomUUID().toString(),
    override val layoutType: QuizType = QuizType.QUIZ3
) : Quiz() {
    /** Transient state, not serialized **/
    @Transient
    var shuffledAnswers: MutableList<String> = answers.toMutableList()

    override fun initViewState() {
        if (answers.isNotEmpty()) {
            // first item stays, the rest get a “Q!Z2<n>” tag and shuffle
            val head = answers.first()
            val tagged = answers
                .drop(1)
                .mapIndexed { idx, ans -> "$ans Q!Z2${idx+1}" }
                .shuffled()
            shuffledAnswers = (listOf(head) + tagged).toMutableList()
        }
    }

    override fun validateQuiz(): QuizError {
        if (question.isBlank())            return QuizError.EMPTY_QUESTION
        if (answers.any(String::isBlank))  return QuizError.EMPTY_ANSWER
        return QuizError.NO_ERROR
    }

    override fun gradeQuiz(): Boolean {
        return answers.indices.all { i ->
            val candidate = shuffledAnswers.getOrNull(i)
                ?.replace(Regex("Q!Z2\\d+$"), "")
                ?: return false
            answers[i] == candidate
        }
    }

    override fun cloneQuiz(
        question: String,
        bodyType: BodyType,
        uuid: String
    ): Quiz {
        return copy(
            question           = question,
            answers            = answers,
            bodyType           = bodyType,
            uuid               = uuid
        ).also { it.initViewState() }
    }
}