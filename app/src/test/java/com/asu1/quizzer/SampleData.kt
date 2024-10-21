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
import com.asu1.quizzer.model.ShaderType
import com.asu1.quizzer.ui.theme.LightColorScheme
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import java.time.LocalDate
import java.time.YearMonth

val scoreCard = ScoreCard(
    title = "페이커 퀴즈",
    creator = "Guest",
    score = 100,
    background = ImageColor(Color.Red, byteArrayOf(), Color.Blue, ImageColorState.COLOR),
    size = 0.3f,
    xRatio = 0.5f,
    yRatio = 0.5f,
    imageStateval = 0,
    colorScheme = LightColorScheme,
    shaderType = ShaderType.Brush1,
)
val quiz1 = Quiz1(
    bodyType = BodyType.NONE,
    image = byteArrayOf(),
    bodyText = "",
    shuffleAnswers = false,
    maxAnswerSelection = 1,
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
    youtubeId = null.toString(),
    youtubeStartTime = 0,
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

fun getQuizLayoutViewModelSample(): QuizLayoutViewModel{
    val quizLayoutViewModel = QuizLayoutViewModel()
    quizLayoutViewModel.setBackgroundImage(ImageColor(
        color = Color(0xFF000000),
        imageData = byteArrayOf(),
        color2 = Color(0xFFFFFFFF),
        state = ImageColorState.COLOR
    ))
    quizLayoutViewModel.updateShuffleQuestions(false)
    quizLayoutViewModel.quizData.value.title = "페이커 퀴즈"
    quizLayoutViewModel.quizTheme.value.colorScheme = LightColorScheme
    quizLayoutViewModel.quizData.value.creator = "아슈"
    quizLayoutViewModel.quizData.value.image = byteArrayOf()
    quizLayoutViewModel.quizData.value.uuid = "4f06ce3e-86c0-549b-be5e-94e58d75ed14"
    quizLayoutViewModel.quizTheme.value.questionTextStyle = listOf(0, 0, 1, 0)
    quizLayoutViewModel.quizTheme.value.bodyTextStyle = listOf(0, 0, 2, 1)
    quizLayoutViewModel.quizTheme.value.answerTextStyle = listOf(0, 0, 0, 2)
    quizLayoutViewModel.quizData.value.tags = mutableSetOf("페이커", "퀴즈", "테스트", "아슈")
    quizLayoutViewModel.addQuiz(quiz1)
    quizLayoutViewModel.addQuiz(quiz2)
    quizLayoutViewModel.addQuiz(quiz3)
    quizLayoutViewModel.addQuiz(quiz4)
    return quizLayoutViewModel
}
