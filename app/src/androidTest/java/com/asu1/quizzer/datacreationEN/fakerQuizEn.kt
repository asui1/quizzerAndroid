package com.asu1.quizzer.datacreationEN

import com.asu1.quizzer.TestQuiz1
import com.asu1.quizzer.TestQuiz2
import com.asu1.quizzer.TestQuiz3
import com.asu1.quizzer.TestQuiz4
import com.asu1.quizzer.allInOneForTest
import java.time.LocalDate
import java.time.YearMonth

val fakerquizzesEn = listOf(
    TestQuiz4(
        point = 6,
        connectionAnswers = mutableListOf("Azir", "Galio", "Orianna", "Ryze", "LeBlanc"),
        connectionAnswerIndex = mutableListOf(3, 4, 0, 1, 2),
        answers = mutableListOf("69.4%", "63.2%", "81.3%", "68.3%", "63.6%"),
        question = "Match Faker's champions with their win rates as of 2024."
    ),
    TestQuiz3(
        point = 6,
        answers = mutableListOf("Azir", "Orianna", "Corki", "Ryze", "Ahri", "LeBlanc"),
        question = "Arrange the champions Faker has played in the LCK by frequency as of 2024."
    ),
    TestQuiz1(
        point = 6,
        answers = mutableListOf(
            "Stop saying such garbage.",
            "Now I understand why people hate dad jokes.",
            "I find Sang-hyeok's jokes funny.",
            "You really put all your talent into League.",
            "It’s so funny that it brings tears to my eyes."
        ),
        ans = mutableListOf(true, false, false, false, false),
        question = "What did Wolf say about Faker's humor?"
    ),
    TestQuiz4(
        point = 5,
        connectionAnswers = mutableListOf("Captain Jack", "Uzi", "Ryu", "Nagane", "Ruler"),
        connectionAnswerIndex = mutableListOf(2, 0, 3, 4, 1),
        answers = mutableListOf("Zed", "Alistar", "Riven", "Azir", "Galio"),
        question = "Match Faker's champions with players associated with them."
    ),
    TestQuiz1(
        point = 5,
        answers = mutableListOf(
            "PRISM GLARE XP RGB",
            "AURORA FLARE XR RGB",
            "NEON STRIKE Z RGB",
            "KLEVV CRAS XR RGB",
            "ZION VORTEX XT RGB"
        ),
        ans = mutableListOf(false, false, false, true, false),
        question = "Which product was featured in the ad with the tagline 'Can you turn off the lights? I want to see my RAM'?"
    ),
    TestQuiz2(
        point = 5,
        centerDate = YearMonth.of(1996, 5),
        answerDate = mutableSetOf(LocalDate.of(1996, 5, 7)),
        answers = mutableListOf(),
        question = "What is Faker's birthday?"
    ),
    TestQuiz1(
        point = 5,
        answers = mutableListOf("Master Yi", "Riven", "Nunu", "Udyr", "Gragas", "Dr. Mundo"),
        ans = mutableListOf(true, true, false, false, true, false),
        question = "Which champions has Faker played mid lane in the LCK?"
    ),
    TestQuiz4(
        point = 4,
        connectionAnswers = mutableListOf("Orianna", "Ryze", "Zed", "Syndra"),
        connectionAnswerIndex = mutableListOf(2, 1, 3, 0),
        answers = mutableListOf("2013", "2015", "2016", "2023"),
        question = "Match Faker's League skins with the year they were released."
    ),
    TestQuiz1(
        point = 4,
        answers = mutableListOf("LeBlanc", "Lissandra", "Corki", "Galio", "Ryze"),
        ans = mutableListOf(false, false, false, true, false),
        question = "Which champion did Faker pick 5 times in a row against RNG in the 2017 Worlds semifinals, earning high praise?"
    ),
    TestQuiz1(
        point = 4,
        answers = mutableListOf("Orianna", "Nidalee", "LeBlanc", "Kha'Zix", "Annie"),
        ans = mutableListOf(false, true, false, false, false),
        question = "Which champion did Faker secure his first kill on in his debut match?"
    ),
)

val fakertestDataEn = allInOneForTest(
    title = "The God of LoL: Faker",
    description = "This quiz is about Faker, the living god of League of Legends. How well do you remember the milestones in his career?",
    tags = setOf("Faker", "LoL", "LOL", "League of Legends", "T1"),
    titleImage = com.asu1.quizzer.test.R.drawable.faker,
    colorInt = 3,
    quizzes = fakerquizzesEn,
    bodyImages = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    bodyYoutubeLinks = listOf("", "", "", "", "", "", "", "", "", ""),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle = listOf(3, 0, 0),
    answerTextStyle = listOf(1, 6, 0),
    primaryColor = "FFD3B27A",
    backgroundImageIndex = 8,
    effectIndex = 1,
    effectColor = "ffe4002b",
    textColor = "ffffdea8",
    backgroundColorFilter = "ffffffff",
)
