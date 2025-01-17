package com.asu1.quizzer.quizCreateUtils

import com.asu1.models.serializers.BodyType
import java.time.LocalDate
import java.time.YearMonth

data class allInOneForTest(
    val title: String,
    val description: String,
    val tags: Set<String>,
    val titleImage: Int,
    val colorInt : Int,
    val quizzes: List<TestQuiz>,
    val bodyImages: List<Int>,
    val bodyYoutubeLinks: List<String> = listOf(),
    val questionTextStyle : List<Int> = listOf(0, 0, 0),
    val bodyTextStyle : List<Int> = listOf(0, 0, 0),
    val answerTextStyle : List<Int> = listOf(0, 0, 0),
    val primaryColor: String = "",
    val textColor: String = "",
    val backgroundImageIndex: Int = 0,
    val backgroundColorFilter: String = "",
    val effectIndex: Int = 1,
    val effectColor: String = "",
    val overlayImage: Int = 0,
    )

abstract class TestQuiz(
    open val point: Int = 5,
    open val bodyType: BodyType = BodyType.NONE,
    open val bodyText: String = "",
    open val question: String,
)

data class TestQuiz1(
    val answers: MutableList<String>,
    val ans: MutableList<Boolean>,
    override val point: Int = 5,
    override val bodyType: BodyType = BodyType.NONE,
    override val bodyText: String = "",
    override val question: String,
) : TestQuiz(point, bodyType, bodyText, question)

data class TestQuiz2(
    val centerDate: YearMonth,
    val answerDate: MutableSet<LocalDate>,
    val answers: MutableList<LocalDate>,
    override val point: Int = 5,
    override val bodyType: BodyType = BodyType.NONE,
    override val bodyText: String = "",
    override val question: String,
) : TestQuiz(point, bodyType, bodyText, question)

data class TestQuiz3(
    val answers: MutableList<String>,
    override val point: Int = 5,
    override val bodyType: BodyType = BodyType.NONE,
    override val bodyText: String = "",
    override val question: String,
) : TestQuiz(point, bodyType, bodyText, question)

data class TestQuiz4(
    val connectionAnswers: MutableList<String>,
    val connectionAnswerIndex: MutableList<Int>,
    val answers: MutableList<String>,
    override val point: Int = 5,
    override val bodyType: BodyType = BodyType.NONE,
    override val bodyText: String = "",
    override val question: String,
) : TestQuiz(point, bodyType, bodyText, question)
