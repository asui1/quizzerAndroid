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

@Serializable
@SerialName("0")
data class MultipleChoiceQuiz(
    override var question: String = "",
    @SerialName("answers")
    val options: List<String> = List(5){""},
    @SerialName("ans")
    val correctFlags: List<Boolean> = List(5){false},
    val shuffleAnswers: Boolean = false,
    @Serializable(with = BodyTypeSerializer::class)
    override var bodyValue: BodyType = BodyType.NONE,
) : Quiz() {
    @Transient
    var displayedOptions: MutableList<String> = options.toMutableList()

    @Transient
    var userSelections = mutableStateListOf<Boolean>()

    @Transient
    override val quizType: QuizType = QuizType.QUIZ1
    @Transient
    override val uuid: String = UUID.randomUUID().toString()

    override fun initViewState() {
        displayedOptions = if (shuffleAnswers) options.shuffled().toMutableList()
        else options.toMutableList()
        userSelections.clear()
        repeat(options.size) { userSelections.add(false) }
    }

    override fun validateQuiz(): QuizError {
        if (question.isBlank())                  return QuizError.EMPTY_QUESTION
        if (options.any { it.isBlank() })       return QuizError.EMPTY_ANSWER
        if (correctFlags.none { it })           return QuizError.EMPTY_OPTION
        return QuizError.NO_ERROR
    }

    override fun gradeQuiz(): Boolean {
        // Build a list of “correct” flags in the same order as displayedOptions
        val effectiveCorrectFlags = displayedOptions.map { option ->
            // Find this option’s original index, then grab its correctFlags entry
            options.indexOf(option).takeIf { it >= 0 }?.let { correctFlags[it] } == true
        }

        // Compare each flag to the user’s selection
        return effectiveCorrectFlags.zip(userSelections).all { (correct, selected) ->
            correct == selected
        }
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
            bodyValue = bodyType,
        ).also { it.initViewState() }
    }

    fun toggleUserSelectionAt(answerIndex: Int){
        userSelections[answerIndex] = !userSelections[answerIndex]
    }
}