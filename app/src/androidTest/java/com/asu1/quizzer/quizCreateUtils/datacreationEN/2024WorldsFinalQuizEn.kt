package com.asu1.quizzer.quizCreateUtils.datacreationEN

import com.asu1.models.quiz.QuizData
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.datacreation.lolworlds24ScoreCard
import com.asu1.quizzer.quizCreateUtils.datacreation.t1WorldsQuizTheme
import com.asu1.quizzer.test.R
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

val worlds24T1En = persistentListOf(
    MultipleChoiceQuiz(
        options = mutableListOf(
            "15 wins, 3 losses",
            "14 wins, 4 losses",
            "13 wins, 5 losses",
            "13 wins, 4 losses",
            "12 wins, 5 losses",
        ),
        correctFlags = mutableListOf(false, false, false, true, false),
        question = "What was T1's series record at the 2024 World Championship?"
    ),

    ReorderQuiz(
        answers = mutableListOf(
            "The Final Battle",
            "Faker's Decisive Move",
            "Godlike Initiation",
            "Faker's Sylas That Refuses to Die",
            "Elk's Map-Cleaving Ashe Arrow"
        ),
        question = "List the Top 5 iconic moments of the 2024 Worlds in order."
    ),

    MultipleChoiceQuiz(
        options = mutableListOf(
            "Used Galio's ultimate to save Zeus in a 4v1 situation",
            "Oner interrupted Ahri's teleport with Poppy's ultimate",
            "Faker secured the kill on Elk",
            "Zeus made an incredible play with a flash ping-pong maneuver",
            "Faker endured till the end and secured a triple kill"
        ),
        correctFlags = mutableListOf(true, false, true, false, false),
        question = "Which of these correctly describes the final moments of Game 5?"
    ),

    ReorderQuiz(
        answers = mutableListOf(
            "Gumayusi and Keria were killed",
            "Bin used teleport to cut off the retreat",
            "Faker taunted Knight with a flash engage",
            "Zeus used a flash knockup to finish Knight",
            "Oner eliminated Xun",
            "Faker killed Elk with a headbutt"
        ),
        question = "After Gumayusi and Keria were killed in Game 5, T1 made a miraculous 3v4 comeback. Arrange the events in order."
    ),

    ReorderQuiz(
        answers = mutableListOf(
            "BLG destroyed the mid turret and retreated",
            "Faker broke through Rakan's line",
            "Faker dodged Sejuani's ultimate",
            "Keria used a flash ultimate to initiate on the enemy",
            "Oner followed up with a flash ultimate",
            "Elk was killed",
            "Knight was killed"
        ),
        question = "Arrange the events when Faker initiated mid in Game 4 in order."
    ),

    ConnectItemsQuiz(
        answers = mutableListOf(
            "Solo-killed Elk",
            "Broke through the enemy to take down their ADC and mid",
            "Chased down fleeing enemies to snowball further",
            "Solo-killed Bin"
        ),
        connectionAnswerIndex = mutableListOf(1, 3, 0, 2),
        connectionAnswers = mutableListOf("Sejuani's ultimate", "Ziggs' ultimate", "Gnar's ultimate", "Rakan's ultimate"),
        question = "Match Faker's superplays in Game 4 with the ultimates used during those moments."
    ),

    MultipleChoiceQuiz(
        options = mutableListOf(
            "Zeus airborne Xun twice in a row to secure a kill.",
            "Oner dove in and killed Elk.",
            "Faker stole Rumble's ultimate and split the enemy formation.",
            "Gumayusi absorbed Rumble and Galio ultimates to set up the fight.",
            "Keria hit a 3-man ultimate to secure the teamfight."
        ),
        correctFlags = mutableListOf(true, true, false, true, false),
        question = "Which of these correctly describes the final teamfight in Game 2?"
    ),

    ReorderQuiz(
        answers = mutableListOf("Stunned Elk and Knight", "Airborne Elk and Knight", "Interrupted Rakan's W", "Stopped Galio's ultimate", "Killed Galio"),
        question = "In Game 2, an artistic top dive took place at the 8-minute mark. Arrange the plays in order."
    ),

    MultipleChoiceQuiz(
        options = mutableListOf(
            "Zeus stopped Elk from recalling.",
            "Oner landed a clean Sejuani ultimate.",
            "Zeus stunned two enemies with Gnar's flash ultimate.",
            "Faker teleported in to secure a kill.",
            "Gumayusi finished off a fleeing enemy with Caitlyn's ultimate."
        ),
        correctFlags = mutableListOf(false, true, false, false, true),
        question = "In Game 1, Zeus almost turned the tide with a superplay around 12 minutes. Which of these are incorrect descriptions of that moment?"
    ),

    DateSelectionQuiz(
        centerDate = LocalDate.of(2024, 11, 1),
        answerDate = mutableSetOf(LocalDate.of(2024, 11, 2)),
        question = "When was the date of the finals?"
    ),
)

val t1WorldsEnQuizData = QuizData(
    title       = "2024 League of Legends Worlds Finals Quiz",
    description = "A quiz about T1's historic 5th championship win at the 2024 World Championship Finals.",
    tags        = setOf("LoL", "2024 Worlds", "T1", "World Championship", "League of Legends")
)

@Suppress("unused")
val T1WORLDSQUIZEN = QuizBundle(
    data       = t1WorldsEnQuizData,
    theme      = t1WorldsQuizTheme,
    quizzes    = worlds24T1En,
    titleImage = R.drawable.t1_final,
    scoreCard = lolworlds24ScoreCard
)