package com.asu1.quizzer.quizCreateUtils.datacreationEN

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

val fakerquizzesEn = listOf(
    ConnectItemsQuiz(
        connectionAnswers = mutableListOf("Azir", "Galio", "Orianna", "Ryze", "LeBlanc"),
        connectionAnswerIndex = mutableListOf(3, 4, 0, 1, 2),
        answers = mutableListOf("69.4%", "63.2%", "81.3%", "68.3%", "63.6%"),
        question = "Match Faker's champions with their win rates as of 2024."
    ),
    ReorderQuiz(
        answers = mutableListOf("Azir", "Orianna", "Corki", "Ryze", "Ahri", "LeBlanc"),
        question = "Arrange the champions Faker has played in the LCK by frequency as of 2024."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf(
            "Stop saying such garbage.",
            "Now I understand why people hate dad jokes.",
            "I find Sang-hyeok's jokes funny.",
            "You really put all your talent into League.",
            "It’s so funny that it brings tears to my eyes."
        ),
        correctFlags = mutableListOf(true, false, false, false, false),
        question = "What did Wolf say about Faker's humor?"
    ),
    ConnectItemsQuiz(
        connectionAnswers = mutableListOf("Captain Jack", "Uzi", "Ryu", "Nagane", "Ruler"),
        connectionAnswerIndex = mutableListOf(2, 0, 3, 4, 1),
        answers = mutableListOf("Zed", "Alistar", "Riven", "Azir", "Galio"),
        question = "Match Faker's champions with players associated with them."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf(
            "PRISM GLARE XP RGB",
            "AURORA FLARE XR RGB",
            "NEON STRIKE Z RGB",
            "KLEVV CRAS XR RGB",
            "ZION VORTEX XT RGB"
        ),
        correctFlags = mutableListOf(false, false, false, true, false),
        question = "Which product was featured in the ad with the tagline 'Can you turn off the lights? I want to see my RAM'?"
    ),
    DateSelectionQuiz(
        centerDate = LocalDate.of(1996, 5, 1),
        answerDate = mutableSetOf(LocalDate.of(1996, 5, 7)),
        question = "What is Faker's birthday?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("Master Yi", "Riven", "Nunu", "Udyr", "Gragas", "Dr. Mundo"),
        correctFlags = mutableListOf(true, true, false, false, true, false),
        question = "Which champions has Faker played mid lane in the LCK?"
    ),
    ConnectItemsQuiz(
        connectionAnswers = mutableListOf("Orianna", "Ryze", "Zed", "Syndra"),
        connectionAnswerIndex = mutableListOf(2, 1, 3, 0),
        answers = mutableListOf("2013", "2015", "2016", "2023"),
        question = "Match Faker's League skins with the year they were released."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("LeBlanc", "Lissandra", "Corki", "Galio", "Ryze"),
        correctFlags = mutableListOf(false, false, false, true, false),
        question = "Which champion did Faker pick 5 times in a row against RNG in the 2017 Worlds semifinals, earning high praise?"
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("Orianna", "Nidalee", "LeBlanc", "Kha'Zix", "Annie"),
        correctFlags = mutableListOf(false, true, false, false, false),
        question = "Which champion did Faker secure his first kill on in his debut match?"
    ),
)

val fakerQuizDataEn = QuizData(
    title       = "The God of LoL: Faker",
    description = "This quiz is about Faker, the living god of League of Legends. How well do you remember the milestones in his career?",
    tags        = setOf("Faker", "LoL", "LOL", "League of Legends", "T1")
    // image stays createEmptyBitmap(); inject via test builder if needed
)

// 3) Build your QuizTheme using the hex values from the old DTO
val fakerQuizThemeEn = QuizTheme(
    backgroundImage = ImageColor(
        color         = hexColor("FFD3B27A"),  // primaryColor
        color2        = hexColor("ffffffff"),  // backgroundColorFilter
        colorGradient = hexColor("ffe4002b"),  // effectColor
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

// 4) Wrap it all up in a QuizBundle
@Suppress("unused")
val fakerQuizBundleEn = QuizBundle(
    data       = fakerQuizDataEn,
    theme      = fakerQuizThemeEn,
    quizzes    = fakerquizzesEn,
    titleImage = R.drawable.faker
)