package com.asu1.models.quiz

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizJson
import com.asu1.models.serializers.QuizType
import com.asu1.models.serializers.json
import com.asu1.utils.normalizeMixedInputPrecisely
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class Quiz5(
    var answer: String = "",
    var userAnswer: String = "",
    override val layoutType: QuizType = QuizType.QUIZ5,
    override var answers: List<String> = listOf("", "", "", "", ""),
    override var question: String = "",
    override var bodyType: BodyType = BodyType.NONE
): Quiz<Quiz5>(answers, question) {
    override fun cloneQuiz(
        answers: List<String>,
        question: String,
        bodyType: BodyType,
        layoutType: QuizType,
        uuid: String
    ): Quiz5 {
        return this.copy(
            answers = answers,
            question = question,
            bodyType = bodyType,
            layoutType = layoutType,
        )
    }

    fun cloneQuiz(
        answer: String,
        userAnswer: String,
        answers: List<String>,
        question: String,
        bodyType: BodyType,
        layoutType: QuizType
    ): Quiz5{
        return this.copy(
            answer = answer,
            userAnswer = userAnswer,
            answers = answers,
            question = question,
            bodyType = bodyType,
            layoutType = layoutType,
        )
    }

    override fun initViewState() {
    }

    override fun validateQuiz(): QuizError {
        if(question.isEmpty()){
            return QuizError.EMPTY_QUESTION
        }
        if(answer.isEmpty()){
            return QuizError.EMPTY_ANSWER
        }
        return QuizError.NO_ERROR
    }

    override fun changeToJson(): QuizJson {
        return QuizJson.Quiz5Json(
            body = QuizJson.Quiz5Body(
                bodyValue = Json.encodeToString(bodyType),
                question = question,
                answer = answer,
            )
        )
    }

    override fun load(data: String) {
        val quiz5Json = json.decodeFromString<QuizJson.Quiz5Json>(data)
        val body = quiz5Json.body

        bodyType = Json.decodeFromString(body.bodyValue)
        question = body.question
        answer = body.answer
    }


    override fun gradeQuiz(): Boolean {
        return normalizeMixedInputPrecisely(answer) == normalizeMixedInputPrecisely(userAnswer)
    }
}