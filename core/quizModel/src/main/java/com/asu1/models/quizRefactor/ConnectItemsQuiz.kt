package com.asu1.models.quizRefactor

import androidx.compose.runtime.toMutableStateList
import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.BodyTypeSerializer
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

const val CONNECT_ITEMS_QUIZ_DEFAULT_SIZE = 3

@Serializable
@SerialName("3")
data class ConnectItemsQuiz(
    override var question: String = "",
    var answers: List<String> = List(CONNECT_ITEMS_QUIZ_DEFAULT_SIZE){""},
    var connectionAnswers: List<String> = List(CONNECT_ITEMS_QUIZ_DEFAULT_SIZE){""},
    var connectionAnswerIndex: List<Int?> = List(CONNECT_ITEMS_QUIZ_DEFAULT_SIZE){null},
    @Serializable(with = BodyTypeSerializer::class)
    override var bodyValue: BodyType = BodyType.NONE,
) : Quiz(
) {
    @Transient
    var userConnectionIndex = List(answers.size) { null as Int? }
        .toMutableStateList()

    @Transient
    override val quizType: QuizType = QuizType.QUIZ4
    @Transient
    override val uuid: String = UUID.randomUUID().toString()

    override fun initViewState() {
        userConnectionIndex = List(answers.size) { null as Int? }
            .toMutableStateList()
    }

    override fun validateQuiz(): QuizError = when {
        question.isBlank() -> QuizError.EMPTY_QUESTION
        answers.any(String::isBlank) || connectionAnswers.any(String::isBlank) -> QuizError.EMPTY_ANSWER
        connectionAnswerIndex.all { it == null } -> QuizError.EMPTY_OPTION
        else -> QuizError.NO_ERROR
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
        connectionAnswers     = connectionAnswers,
        connectionAnswerIndex = connectionAnswerIndex,
        bodyValue              = bodyType,
    ).also { it.initViewState() }

    fun addAnswer(){
        answers = answers.toMutableList().apply {
            add("")
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                add(null)
            }
        }
    }

    fun addConnectionAnswer(){
        connectionAnswers = connectionAnswers.toMutableList().apply {
            add("")
        }
    }

    fun deleteAnswerAt(index: Int){
        answers = answers.toMutableList().apply {
            removeAt(index)
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                removeAt(index)
            }
        }
    }

    fun deleteConnectionAnswerAt(index: Int){
        if (index !in connectionAnswers.indices) return
        // Remove the answer
        connectionAnswers = connectionAnswers.take(index) + connectionAnswers.drop(index + 1)

        // Adjust the indexes: equal → null, greater → minus one, else unchanged
        connectionAnswerIndex = connectionAnswerIndex.map { value ->
            when {
                value == index -> null
                value != null && value > index -> value - 1
                else -> value
            }
        }
    }
}
