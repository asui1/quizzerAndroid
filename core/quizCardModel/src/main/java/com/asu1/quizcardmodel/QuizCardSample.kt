package com.asu1.quizcardmodel

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
