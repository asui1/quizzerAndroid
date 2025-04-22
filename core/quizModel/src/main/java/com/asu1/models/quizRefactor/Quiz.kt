package com.asu1.models.quizRefactor

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizSerializer
import com.asu1.models.serializers.QuizType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonClassDiscriminator
import java.util.UUID

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = QuizSerializer::class)
@JsonClassDiscriminator("layoutType")
sealed class Quiz {
    abstract val question: String
    abstract val bodyValue: BodyType
    @Transient
    open val uuid: String = UUID.randomUUID().toString()

    @Transient
    open val quizType: QuizType = QuizType.QUIZ1

    abstract fun initViewState()
    abstract fun validateQuiz(): QuizError
    abstract fun gradeQuiz(): Boolean

    abstract fun cloneQuiz(
        question: String = this.question,
        bodyType: BodyType = this.bodyValue,
        uuid: String = this.uuid
    ): Quiz
}

