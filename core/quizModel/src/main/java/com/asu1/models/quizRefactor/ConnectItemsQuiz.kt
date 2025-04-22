package com.asu1.models.quizRefactor

import androidx.compose.ui.geometry.Offset
import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.BodyTypeSerializer
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import com.asu1.utils.getDragIndex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
@SerialName("3")
data class ConnectItemsQuiz(
    override var question: String = "",
    var answers: List<String> = emptyList(),
    var connectionAnswers: List<String> = emptyList(),
    var connectionAnswerIndex: List<Int?> = emptyList(),
    @Serializable(with = BodyTypeSerializer::class)
    override var bodyValue: BodyType = BodyType.NONE,
) : Quiz(
) {
    /** Transient view‑state (not serialized) **/
    @Transient
    var leftDots: MutableList<Offset> = MutableList(answers.size) { Offset.Zero }
    @Transient
    var rightDots: MutableList<Offset> = MutableList(connectionAnswers.size) { Offset.Zero }
    @Transient
    var userConnectionIndex: MutableList<Int?> = MutableList(answers.size) { null }
    @Transient
    override val quizType: QuizType = QuizType.QUIZ4
    @Transient
    override val uuid: String = UUID.randomUUID().toString()

    override fun initViewState() {
        userConnectionIndex = MutableList(answers.size) { null }
        leftDots            = MutableList(answers.size) { Offset.Zero }
        rightDots           = MutableList(connectionAnswers.size) { Offset.Zero }
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
            leftDots = leftDots.toMutableList().apply {
                add(Offset.Zero)
            }
        }
    }

    fun addConnectionAnswer(){
        connectionAnswers = connectionAnswers.toMutableList().apply {
            add("")
            rightDots = rightDots.toMutableList().apply {
                add(Offset.Zero)
            }
        }
    }

    fun deleteAnswerAt(index: Int){
        answers = answers.toMutableList().apply {
            removeAt(index)
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                removeAt(index)
            }
            leftDots = leftDots.toMutableList().apply {
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
            rightDots = rightDots.toMutableList().apply {
                removeAt(index)
            }
        }
    }

    fun onDragEnd(from: Int, offset: Offset, isCreator: Boolean){
        val connectionIndex = getDragIndex(offset, rightDots)
        if(isCreator) {
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                this[from] = connectionIndex
            }
        }else{
            userConnectionIndex = userConnectionIndex.toMutableList().apply {
                this[from] = connectionIndex
            }
        }
    }
}