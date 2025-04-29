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

@Serializable
@SerialName("3")
data class ConnectItemsQuiz(
    override var question: String = "",
    var answers: List<String> = listOf("", "", ""),
    var connectionAnswers: List<String> = listOf("", "", ""),
    var connectionAnswerIndex: List<Int?> = listOf(null, null, null),
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
        connectionAnswers = connectionAnswers.toMutableList().apply {
            removeAt(index)
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                for(i in indices){
                    this[i]?.let{
                        if (it == index) {
                            this[i] = null
                        } else if (it > index) {
                            this[i] = it - 1
                        }
                    }
                }
            }
        }
    }
}