package com.asu1.quizzer.util.constants

import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.viewModels.QuizCardMainViewModel.QuizCardsWithTag
import com.asu1.quizzer.viewModels.UserViewModel

val userDataTest = UserViewModel.UserDatas("whwkd122@gmail.com", "whwkd122", null, setOf("tag1", "tag2"))
val emptyUserDataTest = UserViewModel.UserDatas(null, null, null, setOf())

val sampleQuizCard = QuizCard(
    id = "1",
    title = "11Quiz 1. Sample of Quiz Cards. Can you solve this? This will go over 2 lines.",
    tags = listOf("tag1", "tag2"),
    creator = "Creator",
    image = byteArrayOf(),
    count = 3,
    description = "This is a sample quiz card. Please check how this is shown on screen."
)
val sampleQuizCardList = listOf(
    sampleQuizCard,
    sampleQuizCard.copy(id = "2"),
    sampleQuizCard.copy(id = "3"),
    sampleQuizCard.copy(id = "4"),
    sampleQuizCard.copy(id = "5"),
    sampleQuizCard.copy(id = "6"),
)
val sampleQuizCardsWithTagList = listOf(
    QuizCardsWithTag("tag1", sampleQuizCardList),
    QuizCardsWithTag("tag2", sampleQuizCardList),
    QuizCardsWithTag("tag3", sampleQuizCardList),
    QuizCardsWithTag("tag4", sampleQuizCardList)
)
