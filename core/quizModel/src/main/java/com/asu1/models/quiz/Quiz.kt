package com.asu1.models.quiz

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizJson
import com.asu1.models.serializers.QuizType

abstract class Quiz<T : Quiz<T>>(
    open var answers: List<String> = listOf(),
    open var question: String = "",
    open var bodyType: BodyType = BodyType.NONE,
    open val layoutType: QuizType = QuizType.QUIZ1,
    val uuid: String = java.util.UUID.randomUUID().toString()
){
    fun validateBody(){
        bodyType = bodyType.validate()
    }

    abstract fun cloneQuiz(
        answers: List<String> = this.answers,
        question: String = this.question,
        bodyType: BodyType = this.bodyType,
        layoutType: QuizType = this.layoutType,
        uuid: String = this.uuid
    ): T

    abstract fun initViewState()
    abstract fun validateQuiz() : QuizError
    abstract fun changeToJson() : QuizJson
    abstract fun load(data: String)
    abstract fun gradeQuiz(): Boolean
}