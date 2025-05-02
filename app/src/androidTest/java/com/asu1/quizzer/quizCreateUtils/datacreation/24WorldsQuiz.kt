package com.asu1.quizzer.quizCreateUtils.datacreation

import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizData
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.quizTheme.hexColor
import com.asu1.quizzer.test.R
import java.time.LocalDate

val lolworldsKr24 = listOf(
    ReorderQuiz(
        answers = mutableListOf("오로라", "요네", "스카너", "잭스", "애쉬"),
        question = "2024 월즈에서 밴픽률 순서대로 나열하세요."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("오로라", "잭스", "칼리스타", "렐", "스카너"),
        correctFlags = mutableListOf(false, true, false, true, false,),
        question = "다음 중 밴률 탑5에 없는 챔피언들은?"
    ),
    ReorderQuiz(
        answers = mutableListOf("렐", "잭스", "스카너", "나르", "카이사"),
        question = "2024 월즈에서 픽률 순서대로 나열하세요."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("나르 vs 럼블", "오른 vs 럼블", "잭스 vs 럼블", "잭스 vs 나르", "그라가스 vs 잭스"),
        correctFlags = mutableListOf(false, false, false, true, false),
        question = "결승은 Zeus와 Bin의 대결로 이목을 끌었는데 다음 중 없었던 매치업은?"
    ),
    ConnectItemsQuiz(
        answers = mutableListOf("Faker", "Gumayusi", "Canyon", "Keria"),
        connectionAnswerIndex = mutableListOf(2, 0, 1, 3),
        connectionAnswers = mutableListOf("1세트", "2세트", "3세트", "4세트"),
        question = "4강 Gen.G vs T1에서 POG를 받은 선수와 그 세트를 연결하세요."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("잭스", "스카너", "요네", "카이사", "렐"),
        correctFlags = mutableListOf(true, true, false, false, false),
        question = "4강 WBG vs BLG에서 BLG가 3세트 동안 계속 픽한 챔피언들은?"
    ),
    ConnectItemsQuiz(
        answers = mutableListOf("LNG", "HLE", "TES", "FLY"),
        connectionAnswerIndex = mutableListOf(3, 1, 0, 2),
        connectionAnswers = mutableListOf("4승 4패", "6승 6패", "7승 7패", "5승 3패"),
        question = "8강에서 탈락한 팀들의 세트 득실과 연결하세요."
    ),
    ConnectItemsQuiz(
        answers = mutableListOf("T1", "TL", "Weibo", "Gen. G"),
        connectionAnswerIndex = mutableListOf(1, 3, 2, 0),
        connectionAnswers = mutableListOf("3승 0패", "3승 1패", "3승 2패", "탈락"),
        question = "스위스 스테이지에서 팀들을 승/패와 연결하세요"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("paiN Gaming", "Movistar R7", "PSG Talon", "100 Thives", "MAD Lions KOI", "Vikings Esports"),
        correctFlags = mutableListOf(true, false, true, false, false, false),
        question = "다음 팀 중 플레이-인에서 올라온 팀들은?"
    ),
    DateSelectionQuiz(
        centerDate = LocalDate.of(2024, 9, 1),
        answerDate = mutableSetOf(LocalDate.of(2024, 9, 25)),
        question = "월즈 첫 경기 날 은?"
    ),
)

val androidAacDataKo = QuizData(
    title = "2024 LOL 월챔에 대한 퀴즈",
    description = "2024년 월드 챔피언쉽의 기록을 확인해보는 퀴즈입니다. 당신은 얼마나 잘 기억하고 있나요?",
    tags = setOf("롤", "2024 월즈", "리그 오브 레전드", "월드 챔피언쉽"),
)
val lolWorldsKR24Theme = QuizTheme(
    backgroundImage = ImageColor(
        color         = hexColor("FF2A9DF0"),   // primaryColor
        color2        = hexColor("FF2A9DF0"),   // backgroundColorFilter
        colorGradient = hexColor("ffcfe5ff"),   // effectColor
        state         = ImageColorState.COLOR
    ),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle     = listOf(3, 0, 0),
    answerTextStyle   = listOf(1, 6, 0),
    colorScheme = com.asu1.resources.LightColorScheme.copy(
        primary            = hexColor("FF2A9DF0"),
        onPrimary          = hexColor("ffffffff"),  // textColor
        background         = hexColor("FF2A9DF0"),
        secondaryContainer = hexColor("ffcfe5ff")
    )
)
@Suppress("unused")
val androidAacQuizBundleKo = QuizBundle(
    data = androidAacDataKo,
    theme = lolWorldsKR24Theme,
    quizzes = lolworldsKr24,
    titleImage = R.drawable.make_them_believe,
)
