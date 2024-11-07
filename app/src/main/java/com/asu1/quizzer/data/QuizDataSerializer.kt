package com.asu1.quizzer.data

import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.QuizTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Base64

@Serializable
data class QuizDataSerializer(
    val shuffleQuestions: Boolean = false,
    val title: String,
    val creator: String = "Guest",
    val titleImage: String = "", //BASE 64 ENCODED STRING
    val uuid: String,
    val tags: Set<String> = emptySet(),
    val quizzes: List<QuizJson>,
    val description: String,
)

@Serializable
data class QuizLayoutSerializer(
    val quizData: QuizDataSerializer,
    val quizTheme: QuizTheme,
    val scoreCard: ScoreCard
)
// TODO: SERVER CAN NOT USE LIST as input. SO WHEN UPLOADING, things need to get in shape of json or string.

suspend fun QuizLayoutViewModel.quizDataToJson(): QuizDataSerializer {
    if (quizData.value.uuid == null) {
        withContext(Dispatchers.Default) {
            generateUUIDWithTitle()
        }
    }
    val quizDataSerializer = QuizDataSerializer(
        title = quizData.value.title,
        creator = quizData.value.creator,
        titleImage = Base64.getEncoder().encodeToString(quizData.value.image),
        uuid = quizData.value.uuid!!,
        tags = quizData.value.tags,
        quizzes = quizzes.value?.map {
            it.changeToJson()
        } ?: emptyList(),
        description = quizData.value.description
    )
    return quizDataSerializer
}

suspend fun QuizLayoutViewModel.toJson(scoreCard: ScoreCard): QuizLayoutSerializer {
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
    this.quizData.value.image = Base64.getDecoder().decode(quizDataSerializer.titleImage)
    this.quizData.value.uuid = quizDataSerializer.uuid
    this.quizData.value.tags = quizDataSerializer.tags
    this.quizData.value.description = quizDataSerializer.description
    quizDataSerializer.quizzes.map{
        this.addQuiz(it.load())
    }
}

