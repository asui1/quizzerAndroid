package com.asu1.models

import com.asu1.models.quiz.Quiz1
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.Quiz3
import com.asu1.models.quiz.Quiz4
import com.asu1.models.serializers.BodyType
import java.time.LocalDate
import java.time.YearMonth

val sampleQuiz1 = Quiz1(
    question = "What is the capital of India?",
    answers = mutableListOf("Delhi", "Mumbai", "Kolkata", "Chennai"),
    shuffledAnswers = mutableListOf("Delhi", "Mumbai", "Kolkata", "Chennai"),
    userAns = mutableListOf(false, false, false, false),
    ans = mutableListOf(true, false, false, false),
    bodyType = BodyType.TEXT("This is a sample body text"),
)

val sampleQuiz2 = Quiz2(
    question = "Select your birthdate",
    answers = mutableListOf(""),
    answerDate = mutableSetOf(
        LocalDate.of(2000, 1, 1)),
    maxAnswerSelection = 5,
    centerDate = YearMonth.of(2000, 1),
    yearRange = 20,
    userAnswerDate = mutableSetOf(
        LocalDate.of(2000, 1, 3)
    ),
)

val sampleQuiz3 = Quiz3(
    question = "Arrange the following in ascending order",
    answers = mutableListOf("1", "2", "3", "4", "5"),
    shuffledAnswers = mutableListOf("1", "4", "2", "5", "3"),
)

val sampleQuiz4 = Quiz4(
    question = "Connect the following",
    answers = mutableListOf("A", "B", "C", "D"),
    connectionAnswers = mutableListOf("1", "2", "3", "4"),
    userConnectionIndex = mutableListOf(null, null, null, null),
    connectionAnswerIndex = mutableListOf(null, null, null, null),
    dotPairOffsets = mutableListOf(Pair(null, null), Pair(null, null), Pair(null, null), Pair(null, null)),
)

