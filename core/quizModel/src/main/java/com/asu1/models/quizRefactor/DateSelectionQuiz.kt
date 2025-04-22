package com.asu1.models.quizRefactor

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.BodyTypeSerializer
import com.asu1.models.serializers.LocalDateSerializer
import com.asu1.models.serializers.LocalDateSetSerializer
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import com.asu1.utils.toggle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate
import java.util.UUID

@Serializable
@SerialName("1")
data class DateSelectionQuiz(
    override var question: String = "",
    // store as LocalDate, but serialize as [year,month,day]
    @Serializable(with = LocalDateSerializer::class)
    val centerDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    @Serializable(with = LocalDateSetSerializer::class)
    val answerDate: Set<LocalDate> = emptySet(),
    @Serializable(with = BodyTypeSerializer::class)
    override var bodyValue: BodyType = BodyType.NONE,
) : Quiz() {
    /** Transient view‐state, not serialized **/
    @Transient
    var userDates: MutableSet<LocalDate> = mutableSetOf()
    @Transient
    override val uuid: String = UUID.randomUUID().toString()
    @Transient
    override val quizType: QuizType = QuizType.QUIZ2
    @Transient
    val yearRange: Int = 20

    override fun initViewState() {
        userDates.clear()
    }

    override fun validateQuiz(): QuizError {
        if (question.isBlank())          return QuizError.EMPTY_QUESTION
        if (answerDate.isEmpty())      return QuizError.EMPTY_ANSWER
        return QuizError.NO_ERROR
    }

    override fun gradeQuiz(): Boolean {
        return userDates.size == answerDate.size && userDates.all { it in answerDate}
    }

    override fun cloneQuiz(
        question: String,
        bodyType: BodyType,
        uuid: String
    ): Quiz {
        return copy(
            question           = question,
            centerDate         = centerDate,
            answerDate         = answerDate,
            bodyValue           = bodyType,
        ).also { it.initViewState() }
    }

    fun toggleUserAnswer(date: LocalDate) = userDates.toggle(date)
}