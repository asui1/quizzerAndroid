package com.asu1.quizzer.data

import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.QuizTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class QuizDataSerializer(
    val shuffleQuestions: Boolean = false,
    val title: String,
    val creator: String = "Guest",
    val titleImage: ByteArray = byteArrayOf(),
    val uuid: String,
    val tags: Set<String> = emptySet(),
    val quizzes: List<QuizJson>,
)

@Serializable
data class QuizLayoutSerializer(
    val quizData: QuizDataSerializer,
    val quizTheme: QuizTheme,
    val scoreCard: ScoreCard
)

fun QuizLayoutViewModel.quizDataToJson(): QuizDataSerializer {
    if(quizData.value.uuid == null) this.generateUUIDWithTitle()
    val quizDataSerializer = QuizDataSerializer(
        title = quizData.value.title,
        creator = quizData.value.creator,
        titleImage = quizData.value.image ?: byteArrayOf(),
        uuid = quizData.value.uuid!!,
        tags = quizData.value.tags,
        quizzes = quizzes.value?.map { it.changeToJson() } ?: emptyList()
    )
    return quizDataSerializer
}

fun QuizLayoutViewModel.toJson(scoreCard: ScoreCard): QuizLayoutSerializer {
    val quizLayoutSerializer = QuizLayoutSerializer(
        quizData = quizDataToJson(),
        quizTheme = quizTheme.value,
        scoreCard = scoreCard
    )
    return quizLayoutSerializer
}


fun QuizLayoutViewModel.loadQuizData(json: String) {
    val quizDataSerializer = Json.decodeFromString<QuizDataSerializer>(json)
    this.updateShuffleQuestions(quizDataSerializer.shuffleQuestions)
    this.quizData.value.title = quizDataSerializer.title
    this.quizData.value.creator = quizDataSerializer.creator
    this.quizData.value.image = quizDataSerializer.titleImage
    this.quizData.value.uuid = quizDataSerializer.uuid
    this.quizData.value.tags = quizDataSerializer.tags
    quizDataSerializer.quizzes.map{
        this.addQuiz(it.load())
    }
}
