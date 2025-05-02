package com.asu1.quizzer.quizCreateUtils.datacreation

import com.asu1.imagecolor.BackgroundBase
import com.asu1.imagecolor.Effect
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizData
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.scorecard.ScoreCard
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.quizTheme.hexColor
import com.asu1.quizzer.test.R
import java.time.LocalDate

val iuquizzes1 = listOf(
    MultipleChoiceQuiz(
        options = mutableListOf("빨강", "분홍", "보라", "연두", "연노랑"),
        correctFlags = mutableListOf(false, false, true, false, false),
        question = "아이유가 가장 좋아하는 색은?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("밤편지", "너의 의미", "가을 아침", "Rain Drop", "좋은 날"),
        correctFlags = mutableListOf(true, false, false, true, false),
        question = "다음 중 아이유가 작사한 노래들은?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("미아", "좋은 날", "분홍신", "Boo", "잔소리"),
        correctFlags = mutableListOf(false, false, false, false, true),
        question = "아이유의 첫 1위 곡은?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("유은지", "이지금", "이지은", "유지연", "이지연"),
        correctFlags = mutableListOf(false, false, true, false, false),
        question = "아이유의 본명은 무엇인가요?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("호텔 델루나", "드림 하이", "달의 연인", "사랑의 불시착", "미스터 션샤인"),
        correctFlags = mutableListOf(true, true, true, false, false),
        question = "아이유가 출연한 드라마를 모두 고르세요."
    ),
    DateSelectionQuiz(
        centerDate = LocalDate.of(2008, 9, 1),
        answerDate = mutableSetOf(LocalDate.of(2008, 9, 18)),
        question = "아이유의 데뷔 날은?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("Modern Times", "LILAC", "Palette", "Love Poem", "Real"),
        correctFlags = mutableListOf(false, true, false, false, false),
        question = "아이유가 2021년에 발표한 앨범 이름은?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf(
            "빅히트 엔터테인먼트",
            "미스틱 스토리",
            "WM 엔터테인먼트",
            "EDAM 엔터테인먼트",
            "브레이브 엔터테인먼트"
        ),
        correctFlags = mutableListOf(false, false, false, true, false),
        question = "아이유의 소속사는?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("유애나", "아리", "나리", "유리", "유즈"),
        correctFlags = mutableListOf(true, false, false, false, false),
        question = "아이유의 팬덤 이름은?"
    ),
    DateSelectionQuiz(
        centerDate = LocalDate.of(1993, 5, 1),
        answerDate = mutableSetOf(LocalDate.of(1993, 5, 16)),
        question = "아이유의 생일은?"
    ),
)

val iuQuizData1 = QuizData(
    title       = "아이유 팬이라면 간단한 퀴즈",
    description = "아이유에 대한 간단한 퀴즈입니다. 아이유 팬이라면 이 정도는 간단히?",
    tags        = setOf("아이유", "유애나", "가수", "자작곡", "연예인")
    // image stays createEmptyBitmap(); inject via test builder if needed
)

// 3) Build your QuizTheme using the hex values and styles from the old DTO
val iuQuizTheme1 = QuizTheme(
    backgroundImage = ImageColor(
        color         = hexColor("FF874b6c"), // primaryColor
        color2        = hexColor("FFffffff"), // backgroundColorFilter
        colorGradient = hexColor("ffffffff"), // effectColor
        state         = ImageColorState.COLOR
    ),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle     = listOf(3, 0, 0),
    answerTextStyle   = listOf(1, 6, 0),
    colorScheme = com.asu1.resources.LightColorScheme.copy(
        primary            = hexColor("FF874b6c"),
        onPrimary          = hexColor("ff6c3454"),
        background         = hexColor("FFffffff"),
        secondaryContainer = hexColor("ffffffff")
    )
)

val iuScoreCard = ScoreCard(
    textColor = hexColor("6c3454"),
    background = ImageColor(
        backgroundBase = BackgroundBase.GLOWING_TROPHY,
        color = hexColor("ffffff"),
        color2 = hexColor("ffffff"),
        effect = Effect.FIREWORKS,
    ),
)

@Suppress("unused")
val iuQuizBundle1 = QuizBundle(
    data         = iuQuizData1,
    theme        = iuQuizTheme1,
    quizzes      = iuquizzes1,
    titleImage   = R.drawable.iu_ai1,
    overlayImage = R.drawable.iu_portrait_bg2,
    scoreCard = iuScoreCard,
)