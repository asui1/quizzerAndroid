package com.asu1.models.quizRefactor

import androidx.compose.runtime.mutableStateListOf
import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.BodyTypeSerializer
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

const val REORDER_QUIZ_DEFAULT_SIZE = 5

@Serializable
@SerialName("2")
data class ReorderQuiz(
    override var question: String = "",
    val answers: List<String> = List(REORDER_QUIZ_DEFAULT_SIZE){""},
    @Serializable(with = BodyTypeSerializer::class)
    override var bodyValue: BodyType = BodyType.NONE,
) : Quiz() {
    /** Transient state, not serialized **/
    @Transient
    var shuffledAnswers = mutableStateListOf<String>()

    @Transient
    override val uuid: String = UUID.randomUUID().toString()
    @Transient
    override val quizType: QuizType = QuizType.QUIZ3

    override fun initViewState() {
        if (answers.isNotEmpty()) {
            val tagged = answers
                .mapIndexed { idx, ans -> "${ans}Q!Z2${idx+1}" }
                .shuffled()
            shuffledAnswers.clear()
            shuffledAnswers.addAll(tagged)
        }
    }

    override fun validateQuiz(): QuizError = when {
        question.isBlank() -> QuizError.EMPTY_QUESTION
        answers.any(String::isBlank) -> QuizError.EMPTY_ANSWER
        else -> QuizError.NO_ERROR
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
            bodyValue           = bodyType,
        ).also { it.initViewState() }
    }

    fun swap(from: Int, to: Int) {
        if (from !in shuffledAnswers.indices || to !in shuffledAnswers.indices) return

        val tmp = shuffledAnswers[from]
        shuffledAnswers[from] = shuffledAnswers[to]
        shuffledAnswers[to] = tmp
    }
}
