package com.asu1.quizzer.data

import com.asu1.models.serializers.QuizJson
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.QuizTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
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
        quizzes = quizzes.value.map {
            it.changeToJson()
        },
        description = quizData.value.description
    )
    return quizDataSerializer
}

suspend fun QuizLayoutViewModel.toJson(scoreCard: ScoreCard): QuizLayoutSerializer {
    val localQuizData = quizDataToJson()
    val scoreCardCopy = scoreCard.copy(quizUuid = localQuizData.uuid)
    Logger.debug("ScoreCard: $scoreCardCopy")
    val quizLayoutSerializer = QuizLayoutSerializer(
        quizData = localQuizData,
        quizTheme = quizTheme.value,
        scoreCard = scoreCardCopy
    )
    return quizLayoutSerializer
}