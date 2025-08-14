package com.asu1.quizzer.quizCreateUtils.datacreation

import com.asu1.imagecolor.BackgroundBase
import com.asu1.imagecolor.Effect
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizData
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.scorecard.ScoreCard
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.quizTheme.hexColor
import com.asu1.quizzer.test.R
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

val fakerquizzes = persistentListOf(
    ConnectItemsQuiz(
        connectionAnswers = mutableListOf("아지르", "갈리오", "오리아나", "라이즈", "르블랑"),
        connectionAnswerIndex = mutableListOf(3, 4, 0, 1, 2),
        answers = mutableListOf("69.4%", "63.2%", "81.3%", "68.3%", "63.6%"),
        question = "2024년 기준 페이커가 플레이한 챔피언과 승률을 연결하세요."
    ),
    ReorderQuiz(
        answers = mutableListOf("아지르", "오리아나", "코르키", "라이즈", "아리", "르블랑"),
        question = "2024년 기준 페이커가 LCK에서 플레이한 챔피언들을 많은 순서대로 나열하세요."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf(
            "그런 쓰레기같은 말 좀 안했으면 좋겠어.",
            "이제 알겠어, 왜 사람들이 아재 개그를 싫어하는지.",
            "난 상혁이 개그 재밌어.",
            "넌 정말 재능을 롤에 다 썼구나.",
            "너무 웃겨서 눈물이 나올 정도야.",
        ),
        correctFlags = mutableListOf(true, false, false, false, false),
        question = "페이커의 유머에 대해 울프가 했던 말은?"
    ),
    ConnectItemsQuiz(
        connectionAnswers = mutableListOf("캡틴잭", "우지", "류", "나그네", "룰러"),
        connectionAnswerIndex = mutableListOf(2, 0, 3, 4, 1),
        answers = mutableListOf("제드", "알리스타", "리븐", "아지르", "갈리오"),
        question = "페이커가 플레이한 챔피언과 연관이 있는 선수를 연결하세요."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf(
            "PRISM GLARE XP RGB",
            "AURORA FLASE XR RGB",
            "NEON STRIKE Z RGB",
            "KLEVV CRAS XR RGB",
            "ZION VORTEX XT RGB"
        ),
        correctFlags = mutableListOf(false, false, false, true, false),
        question = "'불 좀 꺼줄래? 내 램 좀 보게'라는 멘트로 화제였던 광고의 제품은 무엇일까요?"
    ),
    DateSelectionQuiz(
        centerDate = LocalDate.of(1996, 5, 1),
        answerDate = mutableSetOf(LocalDate.of(1996, 5, 7)),
        question = "페이커의 생일은?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("마스터이", "리븐", "누누", "우디르", "그라가스", "문도 박사"),
        correctFlags = mutableListOf(true, true, false, false, true, false),
        question = "페이커가 LCK 중 미드에서 꺼낸 적 있는 챔피언들은?"
    ),
    ConnectItemsQuiz(
        connectionAnswers = mutableListOf("오리아나", "라이즈", "제드", "신드라"),
        connectionAnswerIndex = mutableListOf(2, 1, 3, 0),
        answers = mutableListOf("2013", "2015", "2016", "2023"),
        question = "페이커의 롤 스킨을 연도와 연결하세요"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("르블랑", "리산드라", "코르키", "갈리오", "라이즈"),
        correctFlags = mutableListOf(false, false, false, true, false),
        question = "페이커가 2017년 월즈 4강 RNG 상대로 5번 연달아 꺼내며 극찬을 받았던 챔피언은?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("오리아나", "니달리", "르블랑", "카직스", "애니"),
        correctFlags = mutableListOf(false, true, false, false, false),
        question = "페이커가 데뷔전 첫 킬을 딴 챔피언은?"
    ),
)

val fakerQuizData = QuizData(
    title       = "롤의 신 페이커에 대해",
    description = "살아있는 롤의 신, 페이커에 대한 퀴즈입니다. 페이커의 데뷔 이후 있었던 일들을 얼마나 잘 기억하고 있나요?",
    tags        = setOf("페이커", "롤", "LOL", "League of Legends", "T1")
)

val fakerQuizTheme = QuizTheme(
    backgroundImage = ImageColor(
        color         = hexColor("FFD3B27A"),   // primaryColor
        color2        = hexColor("ffffffff"),   // backgroundColorFilter
        colorGradient = hexColor("ffe4002b"),   // effectColor
        state         = ImageColorState.COLOR
    ),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle     = listOf(3, 0, 0),
    answerTextStyle   = listOf(1, 6, 0),
    colorScheme = com.asu1.resources.LightColorScheme.copy(
        primary            = hexColor("FFD3B27A"),
        onPrimary          = hexColor("ffffdea8"),
        background         = hexColor("ffffffff"),
        secondaryContainer = hexColor("ffe4002b")
    )
)

val fakerScoreCard = ScoreCard(
    textColor = hexColor("ffdea8"),
    background = ImageColor(
        backgroundBase = BackgroundBase.GLOWING_TROPHY,
        color = hexColor("ffffff"),
        color2 = hexColor("e4002b"),
        effect = Effect.FIREWORKS,
    ),
)
@Suppress("unused")
val fakerQuizBundle = QuizBundle(
    data       = fakerQuizData,
    theme      = fakerQuizTheme,
    quizzes    = fakerquizzes,
    titleImage = R.drawable.faker,
    scoreCard = fakerScoreCard,
)

