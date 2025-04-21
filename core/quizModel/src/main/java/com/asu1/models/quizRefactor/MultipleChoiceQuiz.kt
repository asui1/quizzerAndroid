package com.asu1.models.quizRefactor

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

// === Only MultipleChoiceQuiz declares an 'options' list ===
@Serializable
@SerialName("0")
data class MultipleChoiceQuiz(
    override var question: String = "",
    @SerialName("answers")
    val options: List<String> = List(5){""},
    @SerialName("ans")
    val correctFlags: List<Boolean> = List(5){false},
    val shuffleAnswers: Boolean = false,
    override var bodyType: BodyType = BodyType.NONE,
    override val uuid: String = UUID.randomUUID().toString(),
    override val layoutType: QuizType = QuizType.QUIZ1
) : Quiz() {
    @Transient
    var displayedOptions: MutableList<String> = mutableListOf()

    @Transient
    var userSelections: MutableList<Boolean> = mutableListOf()

    override fun initViewState() {
        displayedOptions = if (shuffleAnswers) options.shuffled().toMutableList()
        else options.toMutableList()
        userSelections = MutableList(options.size) { false }
    }

    override fun validateQuiz(): QuizError {
        if (question.isBlank())                  return QuizError.EMPTY_QUESTION
        if (options.any { it.isBlank() })       return QuizError.EMPTY_ANSWER
        if (correctFlags.none { it })           return QuizError.EMPTY_OPTION
        return QuizError.NO_ERROR
    }

    override fun gradeQuiz(): Boolean {
        return correctFlags.indices.all { correctFlags[it] == userSelections[it] }
    }

    override fun cloneQuiz(
        question: String,
        bodyType: BodyType,
        uuid: String
    ): Quiz {
        return copy(
            question = question,
            options = options,
            correctFlags = correctFlags,
            shuffleAnswers = shuffleAnswers,
            bodyType = bodyType,
            uuid = uuid
        ).also { it.initViewState() }
    }
}