package com.asu1.models

import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.serializers.BodyType
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

val sampleMultipleChoiceQuiz = MultipleChoiceQuiz(
    question = "What is the capital of India?",
    options = mutableListOf("Delhi", "Mumbai", "Kolkata", "Chennai"),
    correctFlags = mutableListOf(true, false, false, false),
    bodyValue = BodyType.TEXT("This is a sample body text"),
)

val sampleDateSelectionQuiz = DateSelectionQuiz(
    question = "Select your birthdate",
    answerDate = mutableSetOf(
        LocalDate.of(2000, 1, 1)),
    centerDate = LocalDate.of(2000, 1, 1),
)

val sampleReorderQuiz = ReorderQuiz(
    question = "Arrange the following in ascending order",
    answers = mutableListOf("1111", "2222", "3333", "4444", "5555"),
).apply{
    shuffledAnswers = mutableListOf("1111", "4444", "5555", "2222", "3333")
}

val sampleConnectItemsQuiz = ConnectItemsQuiz(
    question = "Connect the following",
    answers = mutableListOf("A", "B", "C", "D"),
    connectionAnswers = mutableListOf("1", "2", "3", "4"),
    connectionAnswerIndex = mutableListOf(0, 2, 1, 3),
    bodyValue = BodyType.CODE("""suspend fun main() {
    val result = withContext(Dispatchers.Default) {
        delay(1000)
        "Completed"
    }
    println(result)
}""")
)

val sampleShortAnswerQuiz = ShortAnswerQuiz(
    question = "What is Capital of Korea?",
    answer = "Seoul",
)

val sampleQuizList = persistentListOf(
    sampleMultipleChoiceQuiz,
    sampleDateSelectionQuiz,
    sampleReorderQuiz,
    sampleConnectItemsQuiz,
)

