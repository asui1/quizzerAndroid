package com.asu1.models.quiz

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizJson
import com.asu1.models.serializers.QuizType
import com.asu1.models.serializers.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class Quiz1(
    var ans: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
    var userAns: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
    var shuffleAnswers: Boolean = false,
    var shuffledAnswers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override val layoutType: QuizType = QuizType.QUIZ1,
    override var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var question: String = "",
    override var point: Int = 5,
    override var bodyType: BodyType = BodyType.NONE
): Quiz(answers, question, point, bodyType) {
    fun toggleUserAnswerAt(index: Int){
        userAns[index] = !userAns[index]
    }

    override fun initViewState() {
        shuffledAnswers = if(shuffleAnswers){
            answers.toMutableList().also {
                it.shuffle()
            }
        }else{
            answers.toMutableList()
        }
        userAns = MutableList(answers.size) { false }
    }

    override fun validateQuiz(): QuizError {
        if(question == ""){
            return QuizError.EMPTY_QUESTION
        }
        if(answers.contains("")){
            return QuizError.EMPTY_ANSWER
        }
        if(ans.contains(true).not()){
            return QuizError.EMPTY_OPTION
        }
        return QuizError.NO_ERROR
    }

    override fun changeToJson(): QuizJson {
        return QuizJson.Quiz1Json(
            body = QuizJson.Quiz1Body(
                bodyValue = Json.encodeToString(bodyType),
                shuffleAnswers = shuffleAnswers,
                answers = answers,
                ans = ans,
                question = question,
                points = point,
            )
        )
    }

    override fun load(data: String) {
        val quiz1Json = json.decodeFromString<QuizJson.Quiz1Json>(data)
        val body = quiz1Json.body

        bodyType = Json.decodeFromString(body.bodyValue)
        shuffleAnswers = body.shuffleAnswers
        answers = body.answers.toMutableList()
        ans = body.ans.toMutableList()
        question = body.question
    }

    override fun gradeQuiz(): Boolean {
        for(i in ans.indices){
            if(ans[i] != userAns[i]){
                return false
            }
        }
        return true
    }
}