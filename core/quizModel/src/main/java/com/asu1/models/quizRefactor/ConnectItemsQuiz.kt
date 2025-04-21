package com.asu1.models.quizRefactor

import androidx.compose.ui.geometry.Offset
import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
@SerialName("3")
data class ConnectItemsQuiz(
    override var question: String = "",
    val answers: List<String> = emptyList(),             // same JSON key
    val ans: List<Boolean> = emptyList(),                // same JSON key (unused)
    val connectionAnswers: List<String> = emptyList(),   // same JSON key
    val connectionAnswerIndex: List<Int?> = emptyList(), // same JSON key
    val maxAnswerSelection: Int = 1,                      // same JSON key
    override var bodyType: BodyType = BodyType.NONE,
    override val uuid: String = UUID.randomUUID().toString(),
    override val layoutType: QuizType = QuizType.QUIZ4
) : Quiz(
) {
    /** Transient view‑state (not serialized) **/
    @Transient
    var leftDots: MutableList<Offset?> = MutableList(answers.size) { null }
    @Transient
    var rightDots: MutableList<Offset?> = MutableList(connectionAnswers.size) { null }
    @Transient
    var userConnectionIndex: MutableList<Int?> = MutableList(answers.size) { null }

    override fun initViewState() {
        userConnectionIndex = MutableList(answers.size) { null }
        leftDots            = MutableList(answers.size) { null }
        rightDots           = MutableList(connectionAnswers.size) { null }
    }

    override fun validateQuiz(): QuizError {
        if (question.isBlank()) return QuizError.EMPTY_QUESTION
        if (answers.any(String::isBlank) || connectionAnswers.any(String::isBlank))
            return QuizError.EMPTY_ANSWER
        if (connectionAnswerIndex.all { it == null })
            return QuizError.EMPTY_OPTION
        return QuizError.NO_ERROR
    }

    override fun gradeQuiz(): Boolean =
        connectionAnswerIndex.indices.all { idx ->
            connectionAnswerIndex[idx] == userConnectionIndex[idx]
        }

    override fun cloneQuiz(
        question: String,
        bodyType: BodyType,
        uuid: String
    ): Quiz = copy(
        question              = question,
        answers               = answers,
        ans                   = ans,
        connectionAnswers     = connectionAnswers,
        connectionAnswerIndex = connectionAnswerIndex,
        maxAnswerSelection    = maxAnswerSelection,
        bodyType              = bodyType,
        uuid                  = uuid
    ).also { it.initViewState() }
}