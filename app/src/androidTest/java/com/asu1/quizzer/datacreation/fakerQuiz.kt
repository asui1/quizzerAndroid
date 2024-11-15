package com.asu1.quizzer.datacreation

import com.asu1.quizzer.TestQuiz1
import com.asu1.quizzer.TestQuiz2
import com.asu1.quizzer.TestQuiz3
import com.asu1.quizzer.TestQuiz4
import com.asu1.quizzer.allInOneForTest
import java.time.LocalDate
import java.time.YearMonth

val fakerquizzes = listOf(
    TestQuiz4(
        point = 5,
        connectionAnswers = mutableListOf("캡틴잭", "우지", "류", "나그네", "룰러"),
        connectionAnswerIndex = mutableListOf(2, 0, 3, 4, 1),
        answers = mutableListOf("제드", "알리스타", "리븐", "아지르", "갈리오"),
        question = "페이커가 플레이한 챔피언과 연관이 있는 선수를 연결하세요."
    ),
    TestQuiz1(
        point = 5,
        answers = mutableListOf(
            "PRISM GLARE XP RGB",
            "AURORA FLASE XR RGB",
            "NEON STRIKE Z RGB",
            "KLEVV CRAS XR RGB",
            "ZION VORTEX XT RGB"
        ),
        ans = mutableListOf(false, false, false, true, false),
        question = "'불 좀 꺼줄래? 내 램 좀 보게'라는 멘트로 화제였던 광고의 제품은 무엇일까요?"
    ),
    TestQuiz3(
        point = 6,
        answers = mutableListOf("아지르", "오리아나", "라이즈", "코르키", "아리", "르블랑"),
        question = "2024년 7월 기준 페이커가 LCK에서 플레이한 챔피언들을 순서대로 나열하세요."
    ),
    TestQuiz1(
        point = 6,
        answers = mutableListOf(
            "그런 쓰레기같은 말 좀 안했으면 좋겠어.",
            "이제 알겠어, 왜 사람들이 아재 개그를 싫어하는지.",
            "난 상혁이 개그 재밌어.",
            "넌 정말 재능을 롤에 다 썼구나.",
            "너무 웃겨서 눈물이 나올 정도야.",
        ),
        ans = mutableListOf(true, false, false, false, false),
        question = "페이커의 유머에 대해 울프가 했던 말은?"
    ),
    TestQuiz3(
        point = 6,
        answers = mutableListOf("Team Liquid", "Gen. G", "C9", "BLG", "LNG", "JDG", "Weibo"),
        question = "2023년 월즈에서 T1이 상대한 팀 순서는?"
    ),
    TestQuiz4(
        point = 4,
        connectionAnswers = mutableListOf("오리아나", "라이즈", "제드", "신드라"),
        connectionAnswerIndex = mutableListOf(2, 1, 3, 0),
        answers = mutableListOf("2013", "2015", "2016", "2023"),
        question = "페이커의 롤 스킨을 연도와 연결하세요"
    ),
    TestQuiz1(
        point = 4,
        answers = mutableListOf("르블랑", "리산드라", "코르키", "갈리오", "라이즈"),
        ans = mutableListOf(false, false, false, true, false),
        question = "페이커가 2017년 월즈 4강 RNG 상대로 5번 연달아 꺼내며 극찬을 받았던 챔피언은?"
    ),
    TestQuiz2(
        point = 5,
        centerDate = YearMonth.of(1996, 5),
        answerDate = mutableSetOf(LocalDate.of(1996, 5, 7)),
        answers = mutableListOf(),
        question = "페이커의 생일은?"
    ),
    TestQuiz1(
        point = 5,
        answers = mutableListOf("마스터이", "리븐", "누누", "우디르", "그라가스", "문도 박사"),
        ans = mutableListOf(true, true, false, false, true, false),
        question = "페이커가 LCK 중 미드에서 꺼낸 적 있는 챔피언들은?"
    ),
    TestQuiz1(
        point = 4,
        answers = mutableListOf("오리아나", "니달리", "르블랑", "카직스", "애니"),
        ans = mutableListOf(false, true, false, false, false),
        question = "페이커가 데뷔전 첫 킬을 딴 챔피언은?"
    ),
)

val fakertestData = allInOneForTest(
    title = "페이커 데뷔 이후의 이것 저것",
    description = "살아있는 롤의 신, 페이커에 대한 퀴즈입니다. 페이커의 데뷔 이후 있었던 일들을 얼마나 잘 기억하고 있나요?",
    tags = setOf("페이커", "롤", "LOL", "League of Legends", "T1"),
    titleImage = com.asu1.quizzer.test.R.drawable.faker,
    colorInt = 3,
    quizzes = fakerquizzes,
    bodyImages = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    bodyYoutubeLinks = listOf("", "", "", "", "", "", "", "", "", "")
)
