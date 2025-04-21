package com.asu1.models

import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.serializers.BodyType
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

val sampleQuiz1 = MultipleChoiceQuiz(
    question = "What is the capital of India?",
    options = mutableListOf("Delhi", "Mumbai", "Kolkata", "Chennai"),
    correctFlags = mutableListOf(true, false, false, false),
    bodyType = BodyType.TEXT("This is a sample body text"),
)

val sampleQuiz2 = DateSelectionQuiz(
    question = "Select your birthdate",
    answerDate = mutableSetOf(
        LocalDate.of(2000, 1, 1)),
    centerDate = LocalDate.of(2000, 1, 1),
    yearRange = 20,
)

val sampleQuiz3 = ReorderQuiz(
    question = "Arrange the following in ascending order",
    answers = mutableListOf("1", "2", "3", "4", "5"),
)

val sampleQuiz4 = ConnectItemsQuiz(
    question = "Connect the following",
    answers = mutableListOf("A", "B", "C", "D"),
    connectionAnswers = mutableListOf("1", "2", "3", "4"),
    connectionAnswerIndex = mutableListOf(0, 2, 1, 3),
    bodyType = BodyType.CODE("""suspend fun main() {
    val result = withContext(Dispatchers.Default) {
        delay(1000)
        "Completed"
    }
    println(result)
}""")
)

val sampleQuizList = persistentListOf(
    sampleQuiz1,
    sampleQuiz2,
    sampleQuiz3,
    sampleQuiz4,
)

