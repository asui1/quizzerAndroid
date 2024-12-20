package com.asu1.quizzer.datacreationEN

import com.asu1.quizzer.TestQuiz1
import com.asu1.quizzer.TestQuiz2
import com.asu1.quizzer.TestQuiz3
import com.asu1.quizzer.TestQuiz4
import com.asu1.quizzer.allInOneForTest
import java.time.LocalDate
import java.time.YearMonth

val lolworldsEn24 = listOf(
    TestQuiz3(
        point = 5,
        answers = mutableListOf("Aurora", "Yone", "Skarner", "Jax", "Ashe"),
        question = "Arrange the champions by ban/pick rate at the 2024 Worlds."
    ),
    TestQuiz1(
        point = 5,
        answers = mutableListOf("Aurora", "Jax", "Kalista", "Rell", "Skarner"),
        ans = mutableListOf(false, true, false, true, false),
        question = "Which of the following champions are NOT in the top 5 ban rate?"
    ),
    TestQuiz3(
        point = 5,
        answers = mutableListOf("Rell", "Jax", "Skarner", "Gnar", "Kai'Sa"),
        question = "Arrange the champions by pick rate at the 2024 Worlds."
    ),
    TestQuiz1(
        point = 6,
        answers = mutableListOf("Gnar vs Rumble", "Ornn vs Rumble", "Jax vs Rumble", "Jax vs Gnar", "Gragas vs Jax"),
        ans = mutableListOf(false, false, false, true, false),
        question = "The finals featured a hyped Zeus vs. Bin matchup. Which of the following matchups did NOT occur?"
    ),
    TestQuiz4(
        point = 4,
        answers = mutableListOf("Faker", "Gumayusi", "Canyon", "Keria"),
        connectionAnswerIndex = mutableListOf(2, 0, 1, 3),
        connectionAnswers = mutableListOf("Set 1", "Set 2", "Set 3", "Set 4"),
        question = "Match the POG winners with their sets from the Gen.G vs. T1 semifinals."
    ),
    TestQuiz1(
        point = 4,
        answers = mutableListOf("Jax", "Skarner", "Yone", "Kai'Sa", "Rell"),
        ans = mutableListOf(true, true, false, false, false),
        question = "Which champions did BLG pick across all three games against WBG in the semifinals?"
    ),
    TestQuiz4(
        point = 6,
        answers = mutableListOf("LNG", "HLE", "TES", "FLY"),
        connectionAnswerIndex = mutableListOf(3, 1, 0, 2),
        connectionAnswers = mutableListOf("4W 4L", "6W 6L", "7W 7L", "5W 3L"),
        question = "Match the eliminated quarterfinal teams with their series records."
    ),
    TestQuiz4(
        point = 6,
        answers = mutableListOf("T1", "TL", "Weibo", "Gen.G"),
        connectionAnswerIndex = mutableListOf(1, 3, 2, 0),
        connectionAnswers = mutableListOf("3W 0L", "3W 1L", "3W, 2L", "2W 3L"),
        question = "Match the teams with their records in the Swiss stage."
    ),
    TestQuiz1(
        point = 5,
        answers = mutableListOf("paiN Gaming", "Movistar R7", "PSG Talon", "100 Thieves", "MAD Lions KOI", "Vikings Esports"),
        ans = mutableListOf(true, false, true, false, false, false),
        question = "Which of the following teams advanced from the Play-In stage?"
    ),
    TestQuiz2(
        point = 4,
        centerDate = YearMonth.of(2024, 9),
        answerDate = mutableSetOf(LocalDate.of(2024, 9, 25)),
        answers = mutableListOf(),
        question = "What was the date of the first match of Worlds 2024?"
    ),
)

val lolWorldsEN24 = allInOneForTest(
    title = "2024 League of Legends Worlds Quiz",
    description = "Test your knowledge of the 2024 World Championship records. How well do you remember?",
    tags = setOf("LoL", "2024 Worlds", "League of Legends", "World Championship"),
    titleImage = com.asu1.quizzer.test.R.drawable.make_them_believe,
    colorInt = 3,
    quizzes = lolworldsEn24,
    bodyImages = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    bodyYoutubeLinks = listOf("", "", "", "", "", "", "", "", "", ""),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle = listOf(3, 0, 0),
    answerTextStyle = listOf(1, 6, 0),
    primaryColor = "FF2A9DF0",
    backgroundImageIndex = 0,
    effectIndex = 6,
    effectColor = "ffcfe5ff",
    textColor = "ffffffff",
    backgroundColorFilter = "FF2A9DF0",
)
