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

val lolworldsEn24 = listOf(
    ReorderQuiz(
        answers = mutableListOf("Aurora", "Yone", "Skarner", "Jax", "Ashe"),
        question = "Arrange the champions by ban/pick rate at the 2024 Worlds."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("Aurora", "Jax", "Kalista", "Rell", "Skarner"),
        correctFlags = mutableListOf(false, true, false, true, false),
        question = "Which of the following champions are NOT in the top 5 ban rate?"
    ),
    ReorderQuiz(
        answers = mutableListOf("Rell", "Jax", "Skarner", "Gnar", "Kai'Sa"),
        question = "Arrange the champions by pick rate at the 2024 Worlds."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("Gnar vs Rumble", "Ornn vs Rumble", "Jax vs Rumble", "Jax vs Gnar", "Gragas vs Jax"),
        correctFlags = mutableListOf(false, false, false, true, false),
        question = "The finals featured a hyped Zeus vs. Bin matchup. Which of the following matchups did NOT occur?"
    ),
    ConnectItemsQuiz(
        answers = mutableListOf("Faker", "Gumayusi", "Canyon", "Keria"),
        connectionAnswerIndex = mutableListOf(2, 0, 1, 3),
        connectionAnswers = mutableListOf("Set 1", "Set 2", "Set 3", "Set 4"),
        question = "Match the POG winners with their sets from the Gen.G vs. T1 semifinals."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("Jax", "Skarner", "Yone", "Kai'Sa", "Rell"),
        correctFlags = mutableListOf(true, true, false, false, false),
        question = "Which champions did BLG pick across all three games against WBG in the semifinals?"
    ),
    ConnectItemsQuiz(
        answers = mutableListOf("LNG", "HLE", "TES", "FLY"),
        connectionAnswerIndex = mutableListOf(3, 1, 0, 2),
        connectionAnswers = mutableListOf("4W 4L", "6W 6L", "7W 7L", "5W 3L"),
        question = "Match the eliminated quarterfinal teams with their series records."
    ),
    ConnectItemsQuiz(
        answers = mutableListOf("T1", "TL", "Weibo", "Gen.G"),
        connectionAnswerIndex = mutableListOf(1, 3, 2, 0),
        connectionAnswers = mutableListOf("3W 0L", "3W 1L", "3W, 2L", "2W 3L"),
        question = "Match the teams with their records in the Swiss stage."
    ),
    MultipleChoiceQuiz(
        options = mutableListOf("paiN Gaming", "Movistar R7", "PSG Talon", "100 Thieves", "MAD Lions KOI", "Vikings Esports"),
        correctFlags = mutableListOf(true, false, true, false, false, false),
        question = "Which of the following teams advanced from the Play-In stage?"
    ),
    DateSelectionQuiz(
        centerDate = LocalDate.of(2024, 9, 1),
        answerDate = mutableSetOf(LocalDate.of(2024, 9, 25)),
        question = "What was the date of the first match of Worlds 2024?"
    ),
)

val lolWorldsEN24Data = QuizData(
    title       = "2024 League of Legends Worlds Quiz",
    description = "Test your knowledge of the 2024 World Championship records. How well do you remember?",
    tags        = setOf("LoL", "2024 Worlds", "League of Legends", "World Championship")
)

val lolWorldsEN24Theme = QuizTheme(
    backgroundImage = ImageColor(
        color         = hexColor("FF2A9DF0"), // primaryColor
        color2        = hexColor("FF2A9DF0"), // backgroundColorFilter
        colorGradient = hexColor("ffcfe5ff"), // effectColor
        state         = ImageColorState.COLOR
    ),
    questionTextStyle = listOf(0, 0, 0),
    bodyTextStyle     = listOf(3, 0, 0),
    answerTextStyle   = listOf(1, 6, 0),
    colorScheme = com.asu1.resources.LightColorScheme.copy(
        primary            = hexColor("FF2A9DF0"),
        onPrimary          = hexColor("ffffffff"),
        background         = hexColor("FF2A9DF0"),
        secondaryContainer = hexColor("ffcfe5ff")
    )
)

@Suppress("unused")
val lolWorldsEN24Bundle = QuizBundle(
    data       = lolWorldsEN24Data,
    theme      = lolWorldsEN24Theme,
    quizzes    = lolworldsEn24,
    titleImage = R.drawable.make_them_believe
)