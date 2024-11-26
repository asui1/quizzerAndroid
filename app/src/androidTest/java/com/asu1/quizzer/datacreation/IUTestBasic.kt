package com.asu1.quizzer.datacreation

import com.asu1.quizzer.TestQuiz1
import com.asu1.quizzer.TestQuiz2
import com.asu1.quizzer.allInOneForTest
import java.time.LocalDate
import java.time.YearMonth

val iuquizzes1 = listOf(
    TestQuiz1(
        answers = mutableListOf("빨강", "분홍", "보라", "연두", "연노랑"),
        ans = mutableListOf(false, false, true, false, false),
        question = "아이유가 가장 좋아하는 색은?"
    ),
    TestQuiz1(
        answers = mutableListOf("밤편지", "너의 의미", "가을 아침", "Rain Drop", "좋은 날"),
        ans = mutableListOf(true, false, false, true, false),
        question = "다음 중 아이유가 작사한 노래들은?"
    ),
    TestQuiz1(
        answers = mutableListOf("미아", "좋은 날", "분홍신", "Boo", "잔소리"),
        ans = mutableListOf(false, false, false, false, true),
        question = "아이유의 첫 1위 곡은?"
    ),
    TestQuiz1(
        answers = mutableListOf("유은지", "이지금", "이지은", "유지연", "이지연"),
        ans = mutableListOf(false, false, true, false, false),
        question = "아이유의 본명은 무엇인가요?"
    ),
    TestQuiz1(
        answers = mutableListOf("호텔 델루나", "드림 하이", "달의 연인", "사랑의 불시착", "미스터 션샤인"),
        ans = mutableListOf(true, true, true, false, false),
        question = "아이유가 출연한 드라마를 모두 고르세요."
    ),
    TestQuiz2(
        centerDate = YearMonth.of(2008, 9),
        answerDate = mutableSetOf(LocalDate.of(2008, 9, 18)),
        answers = mutableListOf(),
        question = "아이유의 데뷔 날은?"
    ),
    TestQuiz1(
        answers = mutableListOf("Modern Times", "LILAC", "Palette", "Love Poem", "Real"),
        ans = mutableListOf(false, true, false, false, false),
        question = "아이유가 2021년에 발표한 앨범 이름은?"
    ),
    TestQuiz1(
        answers = mutableListOf(
            "빅히트 엔터테인먼트",
            "미스틱 스토리",
            "WM 엔터테인먼트",
            "EDAM 엔터테인먼트",
            "브레이브 엔터테인먼트"
        ),
        ans = mutableListOf(false, false, false, true, false),
        question = "아이유의 소속사는?"
    ),
    TestQuiz1(
        answers = mutableListOf("유애나", "아리", "나리", "유리", "유즈"),
        ans = mutableListOf(true, false, false, false, false),
        question = "아이유의 팬덤 이름은?"
    ),
    TestQuiz2(
        centerDate = YearMonth.of(1993, 5),
        answerDate = mutableSetOf(LocalDate.of(1993, 5, 16)),
        answers = mutableListOf(),
        question = "아이유의 생일은?"
    ),
)

val iutestdata1 = allInOneForTest(
    title = "아이유 팬이라면 간단한 퀴즈",
    description = "아이유에 대한 간단한 퀴즈입니다. 아이유 팬이라면 이 정도는 간단히?",
    tags = setOf("아이유", "유애나", "가수", "자작곡", "연예인"),
    titleImage = com.asu1.quizzer.test.R.drawable.iu_ai1,
    colorInt = 3,
    quizzes = iuquizzes1,
    bodyImages = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    bodyYoutubeLinks = listOf("", "", "", "", "", "", "", "", "", ""),
    primaryColor = "FF874b6c",
    gradientColor1 = "FFffd8e9",
    gradientColor2 = "ffe5d6db",
    textColor = "ff380727",
)
