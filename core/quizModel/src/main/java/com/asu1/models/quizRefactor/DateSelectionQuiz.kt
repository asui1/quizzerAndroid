package com.asu1.models.quizRefactor

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate
import java.time.YearMonth
import java.util.UUID

@Serializable
@SerialName("1")
data class DateSelectionQuiz(
    override var question: String = "",
    val answers: List<String> = emptyList(),       // ← same name as old JSON
    val ans: List<Boolean> = emptyList(),          // ← same name as old JSON
    val maxAnswerSelection: Int = 5,                // ← same name as old JSON
    val centerDate: List<Int> = listOf(            // ← same name as old JSON
        YearMonth.now().year,
        YearMonth.now().monthValue,
        1
    ),
    val yearRange: Int = 20,                        // ← same name as old JSON
    val answerDate: List<List<Int>> = emptyList(),  // ← same name as old JSON
    override var bodyType: BodyType = BodyType.NONE,
    override val uuid: String = UUID.randomUUID().toString(),
    override val layoutType: QuizType = QuizType.QUIZ2
) : Quiz() {
    /** Transient view‐state, not serialized **/
    @Transient
    val correctDates: Set<LocalDate> =
        answerDate.map { LocalDate.of(it[0], it[1], it[2]) }.toSet()

    @Transient
    var userDates: MutableSet<LocalDate> = mutableSetOf()

    @Transient
    val centerYearMonth: YearMonth =
        YearMonth.of(centerDate[0], centerDate[1])

    override fun initViewState() {
        userDates.clear()
    }

    override fun validateQuiz(): QuizError {
        if (question.isBlank())          return QuizError.EMPTY_QUESTION
        if (correctDates.isEmpty())      return QuizError.EMPTY_ANSWER
        return QuizError.NO_ERROR
    }

    override fun gradeQuiz(): Boolean {
        return correctDates.all { it in userDates }
    }

    override fun cloneQuiz(
        question: String,
        bodyType: BodyType,
        uuid: String
    ): Quiz {
        return copy(
            question           = question,
            answers            = answers,
            ans                = ans,
            maxAnswerSelection = maxAnswerSelection,
            centerDate         = centerDate,
            yearRange          = yearRange,
            answerDate         = answerDate,
            bodyType           = bodyType,
            uuid               = uuid
        ).also { it.initViewState() }
    }
}