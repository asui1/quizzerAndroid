package com.asu1.quizzer.datacreation

import com.asu1.quizzer.TestQuiz1
import com.asu1.quizzer.TestQuiz2
import com.asu1.quizzer.TestQuiz3
import com.asu1.quizzer.TestQuiz4
import com.asu1.quizzer.allInOneForTest
import com.asu1.quizzer.model.BodyType
import java.time.LocalDate
import java.time.YearMonth

val iuquizzes3 = listOf(
    TestQuiz1(
        answers = mutableListOf("에잇", "밤편지", "분홍신", "금요일에 만나요", "관객이 될게"),
        ans = mutableListOf(true, true, false, false, true),
        question = "아이유 공식 유튜브에 콘서트 라이브가 있는 노래들은?"
    ),
    TestQuiz2(
        point = 4,
        centerDate = YearMonth.of(2023, 9),
        answerDate = mutableSetOf(
            LocalDate.of(2023, 9, 23),
            LocalDate.of(2023, 9, 24)
        ),
        answers = mutableListOf(),
        question = "2023년에 있었던 I+UN1VER5E의 날짜는?"
    ),
    TestQuiz1(
        point = 4,
        answers = mutableListOf("74627", "81467", "86325", "91511", "97284"),
        ans = mutableListOf(false, false, true, false, false),
        question = "아이유 유애나 7기 숫자는?"
    ),
    TestQuiz1(
        point = 6,
        answers = mutableListOf(
            "안녕 라일락 아이유만 사랑해",
            "안녕 라일락 우리의 라일락",
            "안녕 아이유 우리가 사랑해",
            "아이유 아이유 아이유 아이유",
            "안녕 라일락 지은이 아이유"
        ),
        ans = mutableListOf(false, true, false, false, false),
        bodyType = BodyType.YOUTUBE("", 0),
        bodyText = "나리는 꽃가루에 눈이 따끔해 (아야)눈물이 고여도 꾹 참을래 (아.이.유)",
        question = "아이유 라일락 노래 응원법 시작하는 법은?"
    ),
    TestQuiz4(
        connectionAnswers = mutableListOf("9월 18일", "3월 25일", "5월 16일", "10월 10일"),
        connectionAnswerIndex = mutableListOf(2, 0, 3, 1),
        answers = mutableListOf("꽃갈피", "가을 아침", "삐삐", "LILAC"),
        question = "앨범과 관련된 날짜를 연결하세요."
    ),
    TestQuiz3(
        point = 4,
        answers = mutableListOf(
            "Real Fantasy",
            "딱 한발짝...그 만큼만 더",
            "스물네 걸음 : 하나 둘 셋 넷",
            "이 지금 dlwlrma",
            "I+UN1VER5E",
            "HEREH"
        ),
        question = "아이유 콘서트들을 순서대로 나열하세요."
    ),
    TestQuiz1(
        point = 6,
        answers = mutableListOf(
            "사랑해! 아이유!",
            "최고야! 아이유!",
            "사랑해! 유애나!",
            "영원히! 아이유!",
            "영원히! 유애나!"
        ),
        ans = mutableListOf(false, false, false, false, true),
        bodyType = BodyType.TEXT("I'm stanning, just stanning you\n(_________________)\n\n오늘도 스치듯 그 말이"),
        question = "다음 두 가사 사이에 들어갈 응원법은?"
    ),
    TestQuiz1(
        answers = mutableListOf(
            "후면 카메라로 찍는다",
            "필터없이 각도 조절로만 찍는다",
            "항상 손으로 V를 하며 찍는다",
            "셀카봉을 꼭 사용한다",
            "핸드폰을 가로로 눞혀서 찍는다"
        ),
        ans = mutableListOf(true, false, false, false, false),
        question = "아이유 셀카의 특이한 점은?"
    ),
    TestQuiz3(
        point = 6,
        answers = mutableListOf(
            "드림하이",
            "최고다 이순신",
            "프로듀사",
            "달의 연인 - 보보경심 려",
            "나의 아저씨",
            "호텔 델루나"
        ),
        question = "아이유가 출연한 드라마를 순서대로 나열하세요."
    ),
    TestQuiz1(
        answers = mutableListOf("좋은날", "Blueming", "분홍신", "팔레트", "스물셋"),
        ans = mutableListOf(true, false, false, true, true),
        question = "아이유 노래들 중 빌보르 2010년대 K-POP Top 100에 선정된 노래들은?"
    ),
)

val iutestdata3 = allInOneForTest(
    title = "아이유 퀴즈 심화편",
    description = "아이유의 모든 활동에 대한 문제입니다. 정말 얼마나 알고 있나요?",
    tags = setOf("아이유", "유애나", "가수", "앨범", "팬"),
    titleImage = com.asu1.quizzer.test.R.drawable.ai_iu3,
    colorInt = 3,
    quizzes = iuquizzes3,
    bodyImages = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    bodyYoutubeLinks = listOf("", "", "", "https://youtu.be/v7bnOxV4jAc?si=GeUOZSCNCPmcVpkd", "", "", "", "", "", ""),
    primaryColor = "ff8d4a5b",
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle = listOf(3, 0, 0),
    answerTextStyle = listOf(1, 6, 0),
    backgroundColorFilter = "ffffffff",
    backgroundImageIndex = 5,
    effectIndex = 3,
    effectColor = "80FA1A8E",
    textColor = "fff88cae",
    overlayImage = com.asu1.quizzer.test.R.drawable.iu_guiter,
)