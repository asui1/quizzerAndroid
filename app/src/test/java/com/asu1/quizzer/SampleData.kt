package com.asu1.quizzer

import androidx.compose.ui.graphics.Color
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.ui.theme.LightColorScheme
import java.time.LocalDate
import java.time.YearMonth

val scoreCard = ScoreCard(
    title = "페이커 퀴즈",
    solver = "Guest",
    score = 100f,
    background = ImageColor(
        imageData = byteArrayOf(),
        color = Color.Red,
        colorGradient = Color.Blue,
        state = ImageColorState.GRADIENT
    ),
    imageStateval = 0,
    colorScheme = LightColorScheme,
)
val quiz1 = Quiz1(
    bodyType = BodyType.NONE,
    shuffleAnswers = false,
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
    question = "페이커가 데뷔전 첫 킬을 딴 챔피언은?",
)
val quiz2 = Quiz2(
    centerDate = YearMonth.of(1996, 5),
    yearRange= 20,
    answerDate = mutableSetOf(
        LocalDate.of(1996, 5, 7),
    ),
    maxAnswerSelection = 1,
    answers = mutableListOf(),
    question = "페이커의 생일은?"

)
val quiz3 = Quiz3(
    answers= mutableListOf(
        "Team Liquid",
        "Gen. G",
        "C9",
        "BLG",
        "LNG",
        "JDG",
        "Weibo"
    ),
    question = "2023년 월즈에서 T1이 상대한 팀 순서는?"
)
val quiz4 = Quiz4(
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
    question =  "페이커가 플레이한 챔피언과 연관이 있는 선수를 연결하세요."
)
