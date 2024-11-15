package com.asu1.quizzer

import com.asu1.quizzer.model.BodyType
import java.time.LocalDate
import java.time.YearMonth
import kotlin.random.Random

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
    val gradientColor1: String = "",
    val gradientColor2: String = "",
    val textColor: String = "",
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

val quiz1 = TestQuiz1(
    point = 5,
    answers = mutableListOf(
        "오리아나",
        "니달리",
        "르블랑",
        "카직스",
        "애니"
    ),
    ans = mutableListOf(
        false,
        true,
        false,
        false,
        false
    ),
    bodyType = BodyType.TEXT,
    bodyText = "FAKER IS GOD.",
    question = "페이커가 데뷔전 첫 킬을 딴 챔피언은?",
)


val quiz2 = TestQuiz2(
    centerDate = YearMonth.of(1996, 5),
    answerDate = mutableSetOf(
        LocalDate.of(1996, 5, 7),
    ),
    answers = mutableListOf(),
    question = "페이커의 생일은?"
)
val quiz3 = TestQuiz3(
    answers= mutableListOf(
        "Team Liquid",
        "Gen. G",
        "C9",
        "BLG",
        "LNG",
        "JDG",
        "Weibo"
    ),
    question = "2023년 월즈에서 T1이 상대한 팀 순서는?",
    bodyType = BodyType.IMAGE,
    bodyText = "",
)
val quiz4 = TestQuiz4(
    connectionAnswers = mutableListOf(
        "캡틴잭",
        "우지",
        "류",
        "나그네",
        "룰러"
    ),
    connectionAnswerIndex = mutableListOf(
        2,
        0,
        3,
        4,
        1
    ),
    answers = mutableListOf(
        "제드",
        "알리스타",
        "리븐",
        "아지르",
        "갈리오"
    ),
    question =  "페이커가 플레이한 챔피언과 연관이 있는 선수를 연결하세요.",
    bodyType = BodyType.YOUTUBE,

)

val testQuizData = allInOneForTest(
    title = "페이커 퀴즈",
    description = "페이커에 대한 퀴즈입니다.",
    tags = setOf("페이커", "퀴즈", "아슈"),
    titleImage = R.drawable.question1,
    colorInt = Random.nextInt(primaryColors.size),
    quizzes = listOf(
        quiz1,
        quiz2,
        quiz3,
        quiz4
    ),
    bodyImages = listOf(
        R.drawable.question1,
        R.drawable.question1,
        R.drawable.question1,
        R.drawable.question1,
    ),
    bodyYoutubeLinks = listOf(
        "https://youtu.be/eyY_NWThxnY?si=rqj0tVixOzgROpsc",
        "https://youtu.be/eyY_NWThxnY?si=rqj0tVixOzgROpsc",
        "https://youtu.be/eyY_NWThxnY?si=rqj0tVixOzgROpsc",
        "https://youtu.be/eyY_NWThxnY?si=rqj0tVixOzgROpsc",
    ),
)