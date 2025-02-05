package com.asu1.models.quiz

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizJson
import com.asu1.models.serializers.QuizType
import com.asu1.models.serializers.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class Quiz3(
    var shuffledAnswers: MutableList<String> = mutableListOf("1", "2", "3", "4", "5"),
    override val layoutType: QuizType = QuizType.QUIZ3,
    override var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var question: String = "",
    override var point: Int = 5,
    override var bodyType: BodyType = BodyType.NONE
): Quiz(answers, question, point, bodyType) {
    fun swap(index1: Int, index2: Int){
        shuffledAnswers = shuffledAnswers.toMutableList().apply {
            val temp = get(index1)
            set(index1, get(index2))
            set(index2, temp)
        }
    }

    override fun initViewState() {
        if (answers.isNotEmpty()) {
            shuffledAnswers = (mutableListOf(answers.first()) + answers.drop(1).mapIndexed { index, answer ->
                buildString {
                    append(answer)
                    append("Q!Z2${index + 1}")
                }
            }.shuffled()).toMutableList()
        }
        validateBody()
    }

    override fun validateQuiz(): QuizError {
        if(question == ""){
            return QuizError.EMPTY_QUESTION
        }
        if(answers.contains("")){
            return QuizError.EMPTY_ANSWER
        }
        return QuizError.NO_ERROR
    }

    override fun changeToJson(): QuizJson {
        val quiz3Json = QuizJson.Quiz3Json(
            body = QuizJson.Quiz3Body(
                layoutType = layoutType.value,
                maxAnswerSelection = 1,
                answers = answers,
                ans = listOf(),
                points = point,
                question = question,
                bodyValue = Json.encodeToString(bodyType),
            )
        )
        return quiz3Json
    }


    override fun load(data: String) {
        val quiz3Json = json.decodeFromString<QuizJson.Quiz3Json>(data)
        val body = quiz3Json.body

        answers = body.answers.toMutableList()
        question = body.question
        bodyType = Json.decodeFromString(body.bodyValue)
        point = body.points

        initViewState()
    }
    override fun gradeQuiz(): Boolean {
        for(i in answers.indices){
            val curAnswer = if(i != 0){
                shuffledAnswers[i].replace(Regex("Q!Z2\\d+$"), "")
            } else{
                shuffledAnswers[i]
            }
            if(answers[i] != curAnswer){
                return false
            }
        }
        return true
    }

}