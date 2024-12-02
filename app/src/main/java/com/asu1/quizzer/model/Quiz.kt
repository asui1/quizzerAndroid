package com.asu1.quizzer.model

import androidx.compose.ui.geometry.Offset
import com.asu1.quizzer.R
import com.asu1.quizzer.data.QuizJson
import com.asu1.quizzer.data.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.YearMonth

enum class QuizType(val value: Int) {
    QUIZ1(0),
    QUIZ2(1),
    QUIZ3(2),
    QUIZ4(3),
}


abstract class Quiz(
    open var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    open var question: String = "",
    val uuid: String = java.util.UUID.randomUUID().toString(),
    open var point: Int = 5,
    open val layoutType: QuizType = QuizType.QUIZ1,
    open var bodyType: BodyType = BodyType.NONE,
) {
    operator fun get(index: Int): String {
        return answers[index]
    }

    operator fun set(index: Int, value: String) {
        answers[index] = value
    }
    fun validateBody() {
        when(bodyType){
            is BodyType.TEXT -> {
                if((bodyType as BodyType.TEXT).bodyText.isEmpty()){
                    bodyType = BodyType.NONE
                }
            }
            is BodyType.IMAGE -> {
                if((bodyType as BodyType.IMAGE).bodyImage.isEmpty()){
                    bodyType = BodyType.NONE
                }
            }
            is BodyType.YOUTUBE -> {
                if((bodyType as BodyType.YOUTUBE).youtubeId.isEmpty()){
                    bodyType = BodyType.NONE
                }
            }
            is BodyType.NONE -> bodyType = BodyType.NONE
            else -> bodyType = BodyType.NONE
        }
    }
    abstract fun initViewState()
    abstract fun validateQuiz() : Pair<Int, Boolean>
    abstract fun changeToJson() : QuizJson
    abstract fun load(data: String)
    abstract fun gradeQuiz(): Boolean
}

//BASIC MULTIPLE CHOICE QUIZ
data class Quiz1(
    var ans: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
    var shuffleAnswers: Boolean = false,
    var userAns: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
    var shuffledAnswers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ1,
    override var bodyType: BodyType = BodyType.NONE,
    override var point: Int = 5,
) : Quiz(answers, question){

    fun toggleUserAnswer(index: Int){
        userAns = userAns.toMutableList().apply{
            set(index, get(index).not())
        }
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
        validateBody()
    }

    override fun validateQuiz(): Pair<Int, Boolean> {
        if(question == ""){
            return Pair(R.string.question_cannot_be_empty, false)
        }
        if(answers.contains("")){
            return Pair(R.string.answers_cannot_be_empty, false)
        }
        if(ans.contains(true).not()){
            return Pair(R.string.correct_answer_not_selected, false)
        }
        return Pair(0, true)
    }

    override fun changeToJson(): QuizJson {
        val quiz1Json = QuizJson.Quiz1Json(
            body = QuizJson.Quiz1Body(
                bodyValue = Json.encodeToString(bodyType),
                shuffleAnswers = shuffleAnswers,
                answers = answers,
                ans = ans,
                question = question,
                points = point,
            )
        )
        return quiz1Json
    }

    override fun load(data: String) {
        val quiz1Json = json.decodeFromString<QuizJson.Quiz1Json>(data)
        val body = quiz1Json.body

        bodyType = Json.decodeFromString(body.bodyValue)
        shuffleAnswers = body.shuffleAnswers
        answers = body.answers.toMutableList()
        ans = body.ans.toMutableList()
        question = body.question
        initViewState()
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Quiz1) return false

        if (!bodyType.equals(other.bodyType)) return false
        if (ans != other.ans) return false
        if (shuffleAnswers != other.shuffleAnswers) return false
        if (answers != other.answers) return false
        if (question != other.question) return false
        if (layoutType != other.layoutType) return false
        if (userAns != other.userAns) return false
        if (uuid != other.uuid) return false
        return true
    }

    override fun hashCode(): Int {
        var result = ans.hashCode()
        result = 31 * result + shuffleAnswers.hashCode()
        result = 31 * result + userAns.hashCode()
        result = 31 * result + shuffledAnswers.hashCode()
        result = 31 * result + answers.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + layoutType.hashCode()
        result = 31 * result + bodyType.hashCode()
        result = 31 * result + point
        return result
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

val sampleQuiz1 = Quiz1(
    question = "What is the capital of India?",
    answers = mutableListOf("Delhi", "Mumbai", "Kolkata", "Chennai"),
    shuffledAnswers = mutableListOf("Delhi", "Mumbai", "Kolkata", "Chennai"),
    userAns = mutableListOf(false, false, false, false),
    ans = mutableListOf(true, false, false, false),
    bodyType = BodyType.TEXT("This is a sample body text"),
)

//SELECTING DATE FROM CALENDAR
data class Quiz2(
    var maxAnswerSelection: Int = 5,
    var centerDate: YearMonth = YearMonth.now(),
    var yearRange: Int = 20,
    var answerDate: MutableSet<LocalDate> = mutableSetOf(),
    var userAnswerDate: MutableSet<LocalDate> = mutableSetOf(),
    override var answers: MutableList<String> = mutableListOf(),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ2,
    override var point: Int = 5,
): Quiz(answers, question){
    fun toggleUserAnswer(date: LocalDate){
        userAnswerDate = userAnswerDate.toMutableSet().apply{
            if(contains(date)){
                remove(date)
            }else{
                add(date)
            }
        }
    }

    override fun initViewState() {
        userAnswerDate = mutableSetOf()
        validateBody()
    }

    override fun validateQuiz(): Pair<Int, Boolean> {
        if(question == ""){
            return Pair(R.string.question_cannot_be_empty, false)
        }
        if(answerDate.isEmpty()){
            return Pair(R.string.answer_date_cannot_be_empty, false)
        }
        return Pair(0, true)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Quiz2) return false

        if (maxAnswerSelection != other.maxAnswerSelection) return false
        if (centerDate != other.centerDate) return false
        if (yearRange != other.yearRange) return false
        if (answerDate != other.answerDate) return false
        if (answers != other.answers) return false
        if (question != other.question) return false
        if (layoutType != other.layoutType) return false
        if (userAnswerDate != other.userAnswerDate) return false
        if (point != other.point) return false
        if (uuid != other.uuid) return false

        return true
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

val sampleQuiz2 = Quiz2(
    question = "Select your birthdate",
    answers = mutableListOf(""),
    answerDate = mutableSetOf(
        LocalDate.of(2000, 1, 1)),
    maxAnswerSelection = 5,
    centerDate = YearMonth.of(2000, 1),
    yearRange = 20,
    userAnswerDate = mutableSetOf(
        LocalDate.of(2000, 1, 3)
    ),
)

//ORDERING QUESTIONS
data class Quiz3(
    var shuffledAnswers: MutableList<String> = mutableListOf("1", "2", "3", "4", "5"),
    override var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ3,
    override var bodyType: BodyType = BodyType.NONE,
    override var point: Int = 5,
): Quiz(answers, question){
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

    override fun validateQuiz(): Pair<Int, Boolean> {
        if(question == ""){
            return Pair(R.string.question_cannot_be_empty, false)
        }
        if(answers.contains("")){
            return Pair(R.string.answers_cannot_be_empty, false)
        }
        return Pair(0, true)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Quiz3) return false

        if (answers != other.answers) return false
        if (question != other.question) return false
        if (layoutType != other.layoutType) return false
        if (shuffledAnswers != other.shuffledAnswers) return false
        if (!bodyType.equals(other.bodyType)) return false
        if (point != other.point) return false
        if (uuid != other.uuid) return false

        return true
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

    override fun hashCode(): Int {
        var result = shuffledAnswers.hashCode()
        result = 31 * result + answers.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + layoutType.hashCode()
        result = 31 * result + bodyType.hashCode()
        result = 31 * result + point
        return result
    }
}

val sampleQuiz3 = Quiz3(
    question = "Arrange the following in ascending order",
    answers = mutableListOf("1", "2", "3", "4", "5"),
    shuffledAnswers = mutableListOf("1", "4", "2", "5", "3"),
)

//CONNECTING QUESTIONS
data class Quiz4(
    var connectionAnswers: List<String> = mutableListOf("", "", "", ""),
    var connectionAnswerIndex: List<Int?> = mutableListOf(null, null, null, null),
    var dotPairOffsets: List<Pair<Offset?, Offset?>> = mutableListOf(Pair(null, null), Pair(null, null), Pair(null, null), Pair(null, null)),
    var userConnectionIndex: List<Int?> = mutableListOf(null, null, null, null),
    override var answers: MutableList<String> = mutableListOf("", "", "", ""),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ4,
    override var bodyType: BodyType = BodyType.NONE,
    override var point: Int = 5,
): Quiz(answers, question){
    fun updateUserConnection(from: Int, to: Int?){
        userConnectionIndex = userConnectionIndex.toMutableList().apply {
            set(from, to)
        }
    }

    override fun initViewState() {
        userConnectionIndex = MutableList(answers.size) { null }
        validateBody()
    }

    override fun validateQuiz(): Pair<Int, Boolean> {
        if(question == ""){
            return Pair(R.string.question_cannot_be_empty, false)
        }
        if(answers.contains("") || connectionAnswers.contains("")){
            return Pair(R.string.answers_cannot_be_empty, false)
        }
        if(connectionAnswerIndex.none { it != null }){
            return Pair(R.string.at_least_one_connection_must_be_made, false)
        }
        return Pair(0, true)
    }

    override fun changeToJson(): QuizJson {
        val quiz4Json = QuizJson.Quiz4Json(
            body = QuizJson.Quiz4Body(
                connectionAnswers = connectionAnswers,
                connectionAnswerIndex = connectionAnswerIndex,
                layoutType = layoutType.value,
                maxAnswerSelection = 1,
                answers = answers,
                ans = listOf(),
                points = point,
                question = question,
                bodyValue = Json.encodeToString(bodyType),
            )
        )
        return quiz4Json
    }

    override fun load(data: String) {
        val quiz4Json = json.decodeFromString<QuizJson.Quiz4Json>(data)
        val body = quiz4Json.body

        connectionAnswers = body.connectionAnswers.toMutableList()
        connectionAnswerIndex = body.connectionAnswerIndex.toMutableList()
        answers = body.answers.toMutableList()
        question = body.question
        bodyType = Json.decodeFromString(body.bodyValue)
        point = body.points

        initViewState()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Quiz4) return false

        if (connectionAnswers != other.connectionAnswers) return false
        if (connectionAnswerIndex != other.connectionAnswerIndex) return false
        if (answers != other.answers) return false
        if (question != other.question) return false
        if (layoutType != other.layoutType) return false
        if (!bodyType.equals(other.bodyType)) return false
        if (point != other.point) return false
        if (userConnectionIndex != other.userConnectionIndex) return false
        if (dotPairOffsets != other.dotPairOffsets) return false
        if (uuid != other.uuid) return false

        return true
    }
    fun updateOffset(index: Int, offset: Offset, isStart: Boolean){
        dotPairOffsets = dotPairOffsets.toMutableList().apply {
            set(index, if(isStart){
                Pair(offset, get(index).second)
            }else{
                Pair(get(index).first, offset)
            })
        }
    }

    override fun gradeQuiz(): Boolean {
        for(i in connectionAnswerIndex.indices){
            if(connectionAnswerIndex[i] != userConnectionIndex[i]){
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = connectionAnswers.hashCode()
        result = 31 * result + connectionAnswerIndex.hashCode()
        result = 31 * result + dotPairOffsets.hashCode()
        result = 31 * result + userConnectionIndex.hashCode()
        result = 31 * result + answers.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + layoutType.hashCode()
        result = 31 * result + bodyType.hashCode()
        result = 31 * result + point
        return result
    }
}

val sampleQuiz4 = Quiz4(
    question = "Connect the following",
    answers = mutableListOf("A", "B", "C", "D"),
    connectionAnswers = mutableListOf("1", "2", "3", "4"),
    userConnectionIndex = mutableListOf(null, null, null, null),
    connectionAnswerIndex = mutableListOf(null, null, null, null),
    dotPairOffsets = mutableListOf(Pair(null, null), Pair(null, null), Pair(null, null), Pair(null, null)),
)