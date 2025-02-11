package com.asu1.models.quiz

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizJson
import com.asu1.models.serializers.QuizType

abstract class Quiz(
    open var answers: List<String> = listOf(),
    open var question: String = "",
    open var point: Int = 5,
    open var bodyType: BodyType = BodyType.NONE,
    open val layoutType: QuizType = QuizType.QUIZ1,
    val uuid: String = java.util.UUID.randomUUID().toString()
){
    fun validateBody(){
        bodyType = bodyType.validate()
    }

    abstract fun initViewState()
    abstract fun validateQuiz() : QuizError
    abstract fun changeToJson() : QuizJson
    abstract fun load(data: String)
    abstract fun gradeQuiz(): Boolean
}