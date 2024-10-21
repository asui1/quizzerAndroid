package com.asu1.quizzer.model

import androidx.compose.ui.geometry.Offset
import com.asu1.quizzer.data.Quiz1Body
import com.asu1.quizzer.data.Quiz1Json
import com.asu1.quizzer.data.Quiz2Body
import com.asu1.quizzer.data.Quiz2Json
import com.asu1.quizzer.data.Quiz3Body
import com.asu1.quizzer.data.Quiz3Json
import com.asu1.quizzer.data.Quiz4Body
import com.asu1.quizzer.data.Quiz4Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.YearMonth
import java.util.Base64

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
    open val layoutType: QuizType = QuizType.QUIZ1,
) {
    operator fun get(index: Int): String {
        return answers[index]
    }

    operator fun set(index: Int, value: String) {
        answers[index] = value
    }

    abstract fun initViewState()
    abstract fun validateQuiz() : Pair<String, Boolean>
    abstract fun changeToJson() : String
    abstract fun load(data: String)
}

enum class BodyType(val value: Int) {
    NONE(0),
    TEXT(1),
    IMAGE(2),
    YOUTUBE(3),
}

//BASIC MULTIPLE CHOICE QUIZ
data class Quiz1(
    var bodyType: BodyType = BodyType.NONE,
    var image: ByteArray? = null,
    var ans: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
    var shuffleAnswers: Boolean = false,
    var maxAnswerSelection: Int = 1,
    var bodyText: String = "",
    var youtubeId: String = "",
    var youtubeStartTime: Int = 0,
    var userAns: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
    var shuffledAnswers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ1,
) : Quiz(answers, question){
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

    override fun validateQuiz(): Pair<String, Boolean> {
        if(question == ""){
            return Pair("Question cannot be empty", false)
        }
        if(answers.contains("")){
            return Pair("Answers cannot be empty", false)
        }
        if(ans.contains(true).not()){
            return Pair("Correct answer not selected", false)
        }
        if(ans.count { it } > maxAnswerSelection){
            return Pair("More than $maxAnswerSelection correct answers selected", false)
        }
        return Pair("", true)
    }

    override fun changeToJson(): String {
        val quiz1Json = Quiz1Json(
            layoutType = layoutType.value,
            body = Quiz1Body(
                bodyType = bodyType.value,
                image = image?.let { Base64.getEncoder().encodeToString(it) },
                bodyText = bodyText,
                shuffleAnswers = shuffleAnswers,
                maxAnswerSelection = maxAnswerSelection,
                answers = answers,
                ans = ans,
                question = question,
                youtubeId = youtubeId.takeIf { it.isNotEmpty() },
                youtubeStartTime = youtubeStartTime.takeIf { it != 0 }
            )
        )
        return Json.encodeToString(quiz1Json)
    }

    override fun load(data: String) {
        val quiz1Json = Json.decodeFromString<Quiz1Json>(data)
        val body = quiz1Json.body

        bodyType = BodyType.values().first { it.value == body.bodyType }
        image = body.image?.let { Base64.getDecoder().decode(it) }
        bodyText = body.bodyText
        shuffleAnswers = body.shuffleAnswers
        maxAnswerSelection = body.maxAnswerSelection
        answers = body.answers.toMutableList()
        ans = body.ans.toMutableList()
        question = body.question
        youtubeId = body.youtubeId ?: ""
        youtubeStartTime = body.youtubeStartTime ?: 0
        initViewState()
    }
    fun validateBody() {
        if (bodyType == BodyType.YOUTUBE && youtubeId.isEmpty()) {
            bodyType = BodyType.NONE
        }
        if(bodyType == BodyType.IMAGE && image == null){
            bodyType = BodyType.NONE
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Quiz1) return false

        if (bodyType != other.bodyType) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (ans != other.ans) return false
        if (shuffleAnswers != other.shuffleAnswers) return false
        if (maxAnswerSelection != other.maxAnswerSelection) return false
        if (bodyText != other.bodyText) return false
        if (youtubeId != other.youtubeId) return false
        if (youtubeStartTime != other.youtubeStartTime) return false
        if (shuffledAnswers != other.shuffledAnswers) return false
        if (answers != other.answers) return false
        if (question != other.question) return false
        if (layoutType != other.layoutType) return false

        return true
    }
}

val sampleQuiz1 = Quiz1(
    question = "What is the capital of India?",
    answers = mutableListOf("Delhi", "Mumbai", "Kolkata", "Chennai"),
    shuffledAnswers = mutableListOf("Delhi", "Mumbai", "Kolkata", "Chennai"),
    userAns = mutableListOf(false, false, false, false),
    ans = mutableListOf(true, false, false, false),
    bodyText = "This is a sample question",
    bodyType = BodyType.TEXT
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
): Quiz(answers, question){
    override fun initViewState() {
        userAnswerDate = mutableSetOf()
    }

    override fun validateQuiz(): Pair<String, Boolean> {
        if(question == ""){
            return Pair("Question cannot be empty", false)
        }
        if(answerDate.isEmpty()){
            return Pair("Answer date cannot be empty", false)
        }
        return Pair("", true)
    }

    override fun changeToJson(): String {
        val quiz2Json = Quiz2Json(
            layoutType = layoutType.value,
            body = Quiz2Body(
                centerDate = listOf(centerDate.year, centerDate.monthValue, 1),
                yearRange = yearRange,
                answerDate = answerDate.map { listOf(it.year, it.monthValue, it.dayOfMonth) },
                maxAnswerSelection = maxAnswerSelection,
                answers = answers,
                ans = listOf(),
                question = question
            )
        )
        return Json.encodeToString(quiz2Json)
    }

    override fun load(data: String) {
        val quiz2Json = Json.decodeFromString<Quiz2Json>(data)
        val body = quiz2Json.body

        centerDate = YearMonth.of(body.centerDate[0], body.centerDate[1])
        yearRange = body.yearRange
        answerDate = body.answerDate.map { LocalDate.of(it[0], it[1], it[2]) }.toMutableSet()
        maxAnswerSelection = body.maxAnswerSelection
        answers = body.answers.toMutableList()
        question = body.question
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
    var shuffledAnswers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var answers: MutableList<String> = mutableListOf("", "", "", "", ""),
    override var question: String = "",
    override var layoutType: QuizType = QuizType.QUIZ3,
): Quiz(answers, question){
    override fun initViewState() {
        if (answers.isNotEmpty()) {
            shuffledAnswers = (mutableListOf(answers.first()) + answers.drop(1).mapIndexed { index, answer ->
                answer + "Q!Z2${index + 1}"
            }.shuffled()).toMutableList()
        }
    }

    override fun validateQuiz(): Pair<String, Boolean> {
        if(question == ""){
            return Pair("Question cannot be empty", false)
        }
        if(answers.contains("")){
            return Pair("Answer cannot be empty", false)
        }
        return Pair("", true)
    }

    override fun changeToJson(): String {
        val quiz3Json = Quiz3Json(
            layoutType = layoutType.value,
            body = Quiz3Body(
                layoutType = layoutType.value,
                maxAnswerSelection = 1,
                answers = answers,
                ans = listOf(),
                question = question
            )
        )
        return Json.encodeToString(quiz3Json)    }

    override fun load(data: String) {
        val quiz3Json = Json.decodeFromString<Quiz3Json>(data)
        val body = quiz3Json.body

        answers = body.answers.toMutableList()
        question = body.question
        initViewState()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Quiz3) return false

        if (answers != other.answers) return false
        if (question != other.question) return false
        if (layoutType != other.layoutType) return false

        return true
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
): Quiz(answers, question){
    init {
        // Additional initialization logic if needed
    }
    override fun initViewState() {
        userConnectionIndex = MutableList(answers.size) { null }
    }

    override fun validateQuiz(): Pair<String, Boolean> {
        if(question == ""){
            return Pair("Question cannot be empty", false)
        }
        if(answers.contains("") || connectionAnswers.contains("")){
            return Pair("Answer cannot be empty", false)
        }
        if(connectionAnswerIndex.none { it != null }){
            return Pair("At least one connection must be made", false)
        }
        return Pair("", true)
    }

    override fun changeToJson(): String {
        val quiz4Json = Quiz4Json(
            layoutType = layoutType.value,
            body = Quiz4Body(
                connectionAnswers = connectionAnswers,
                connectionAnswerIndex = connectionAnswerIndex,
                layoutType = layoutType.value,
                maxAnswerSelection = 1,
                answers = answers,
                ans = listOf(),
                question = question
            )
        )
        return Json.encodeToString(quiz4Json)
    }

    override fun load(data: String) {
        val quiz4Json = Json.decodeFromString<Quiz4Json>(data)
        val body = quiz4Json.body

        connectionAnswers = body.connectionAnswers.toMutableList()
        connectionAnswerIndex = body.connectionAnswerIndex.toMutableList()
        answers = body.answers.toMutableList()
        question = body.question

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

        return true
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