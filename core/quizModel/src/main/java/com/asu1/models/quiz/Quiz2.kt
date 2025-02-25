package com.asu1.models.quiz

import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizJson
import com.asu1.models.serializers.QuizType
import com.asu1.models.serializers.json
import java.time.LocalDate
import java.time.YearMonth

data class Quiz2(
    var maxAnswerSelection: Int = 5,
    var centerDate: YearMonth = YearMonth.now(),
    var yearRange: Int = 20,
    var answerDate: MutableSet<LocalDate> = mutableSetOf(),
    var userAnswerDate: MutableSet<LocalDate> = mutableSetOf(),
    override val layoutType: QuizType = QuizType.QUIZ2,
    override var answers: List<String> = listOf(),
    override var question: String = "",
    override var point: Int = 5,
): Quiz<Quiz2>(answers, question, point) {
    fun toggleUserAnswer(date: LocalDate){
        userAnswerDate = userAnswerDate.toMutableSet().apply{
            if(contains(date)){
                remove(date)
            }else{
                add(date)
            }
        }
    }

    override fun cloneQuiz(
        answers: List<String>,
        question: String,
        point: Int,
        bodyType: BodyType,
        layoutType: QuizType,
        uuid: String
    ): Quiz2 {
        return this.copy(
            answers = answers,
            question = question,
            point = point,
            layoutType = layoutType,
        )
    }

    fun cloneQuiz(
        maxAnswerSelection: Int,
        centerDate: YearMonth,
        yearRange: Int,
        answerDate: MutableSet<LocalDate>,
        userAnswerDate: MutableSet<LocalDate>,
        answers: List<String>,
        question: String,
        point: Int,
    ): Quiz2 {
        return this.copy(
            maxAnswerSelection = maxAnswerSelection,
            centerDate = centerDate,
            yearRange = yearRange,
            answerDate = answerDate.toMutableSet(),
            userAnswerDate = userAnswerDate.toMutableSet(),
            answers = answers,
            question = question,
            point = point,
        )
    }

    override fun initViewState() {
        userAnswerDate = mutableSetOf()
    }
    override fun validateQuiz(): QuizError {
        if(question == ""){
            return QuizError.EMPTY_QUESTION
        }
        if(answerDate.isEmpty()){
            return QuizError.EMPTY_ANSWER
        }
        return QuizError.NO_ERROR
    }

    override fun changeToJson(): QuizJson {
        val quiz2Json = QuizJson.Quiz2Json(
            body = QuizJson.Quiz2Body(
                centerDate = listOf(centerDate.year, centerDate.monthValue, 1),
                yearRange = yearRange,
                answerDate = answerDate.map { listOf(it.year, it.monthValue, it.dayOfMonth) },
                maxAnswerSelection = maxAnswerSelection,
                answers = answers,
                ans = listOf(),
                points = point,
                question = question
            )
        )
        return quiz2Json
    }

    override fun load(data: String) {
        val quiz2Json = json.decodeFromString<QuizJson.Quiz2Json>(data)
        val body = quiz2Json.body

        centerDate = YearMonth.of(body.centerDate[0], body.centerDate[1])
        yearRange = body.yearRange
        answerDate = body.answerDate.map { LocalDate.of(it[0], it[1], it[2]) }.toMutableSet()
        maxAnswerSelection = body.maxAnswerSelection
        answers = body.answers.toMutableList()
        question = body.question
        point = body.points
        initViewState()
    }
    override fun gradeQuiz(): Boolean {
        for( i in answerDate){
            if(userAnswerDate.contains(i).not()){
                return false
            }
        }
        return true
    }
}