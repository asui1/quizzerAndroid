package com.asu1.models.quiz

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizJson
import com.asu1.models.serializers.QuizType
import com.asu1.models.serializers.json
import com.asu1.utils.normalizeMixedInputPrecisely
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class Quiz6(
    var fillInAnswers: List<String> = listOf(),
    var userAnswers: List<String> = listOf(),
    override val layoutType: QuizType = QuizType.QUIZ6,
    override var answers: List<String> = listOf(""),
    override var question: String = "",
    override var bodyType: BodyType = BodyType.NONE
): Quiz<Quiz6>(answers, question) {
    override fun cloneQuiz(
        answers: List<String>,
        question: String,
        bodyType: BodyType,
        layoutType: QuizType,
        uuid: String
    ): Quiz6 {
        return this.copy(
            answers = answers,
            question = question,
            bodyType = bodyType,
            layoutType = layoutType,
        )
    }

    fun cloneQuiz(
        fillInAnswers: List<String>,
        userAnswers: List<String>,
        answers: List<String>,
        question: String,
        bodyType: BodyType,
        layoutType: QuizType
    ): Quiz6{
        return this.copy(
            fillInAnswers = fillInAnswers,
            userAnswers = userAnswers,
            answers = answers,
            question = question,
            bodyType = bodyType,
            layoutType = layoutType,
        )
    }

    override fun initViewState() {
        userAnswers = List(fillInAnswers.size) { "" }
    }

    override fun validateQuiz(): QuizError {
        if(question.isEmpty()){
            return QuizError.EMPTY_QUESTION
        }
        if(answers.isEmpty() || answers.contains("")){
            return QuizError.EMPTY_ANSWER
        }
        if(fillInAnswers.isEmpty() || fillInAnswers.contains("")){
            return QuizError.EMPTY_ANSWER
        }
        return QuizError.NO_ERROR
    }

    override fun changeToJson(): QuizJson {
        return QuizJson.Quiz6Json(
            body = QuizJson.Quiz6Body(
                bodyValue = Json.encodeToString(bodyType),
                question = question,
                answers = answers,
                fillInAnswers = fillInAnswers,
            )
        )
    }

    override fun load(data: String) {
        val quiz5Json = json.decodeFromString<QuizJson.Quiz5Json>(data)
        val body = quiz5Json.body

        bodyType = Json.decodeFromString(body.bodyValue)
        question = body.question
    }


    override fun gradeQuiz(): Boolean {
        userAnswers.mapIndexed{ index, item ->
            if(normalizeMixedInputPrecisely(item) != normalizeMixedInputPrecisely(fillInAnswers[index]))
                return false
        }
        return true
    }
}