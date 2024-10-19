package com.asu1.quizzer.model

import androidx.compose.ui.geometry.Offset
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
    abstract fun changeToJson() : Json
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

    override fun changeToJson(): Json {
        TODO("Not yet implemented")
    }

    override fun load(data: String) {
        TODO("Not yet implemented")
    }

    fun validateBody() {
        if (bodyType == BodyType.YOUTUBE && youtubeId.isEmpty()) {
            bodyType = BodyType.NONE
        }
        if(bodyType == BodyType.IMAGE && image == null){
            bodyType = BodyType.NONE
        }
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

    override fun changeToJson(): Json {
        TODO("Not yet implemented")
    }

    override fun load(data: String) {
        TODO("Not yet implemented")
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

    override fun changeToJson(): Json {
        TODO("Not yet implemented")
    }

    override fun load(data: String) {
        TODO("Not yet implemented")
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

    override fun changeToJson(): Json {
        TODO("Not yet implemented")
    }

    override fun load(data: String) {
        TODO("Not yet implemented")
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