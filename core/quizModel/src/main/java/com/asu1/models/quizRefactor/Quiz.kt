package com.asu1.models.quizRefactor

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("layoutType")
sealed class Quiz {
    abstract val question: String
    abstract val bodyType: BodyType
    abstract val uuid: String
    abstract val layoutType: QuizType

    abstract fun initViewState()
    abstract fun validateQuiz(): QuizError
    abstract fun gradeQuiz(): Boolean

    abstract fun cloneQuiz(
        question: String = this.question,
        bodyType: BodyType = this.bodyType,
        uuid: String = this.uuid
    ): Quiz
}
