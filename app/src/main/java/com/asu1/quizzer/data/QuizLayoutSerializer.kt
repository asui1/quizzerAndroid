package com.asu1.quizzer.data

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class QuizLayoutSerializer(
    val backgroundImage: ImageColor,
    val shuffleQuestions: Boolean = false,
    val title: String,
    @Serializable(with = ColorSchemeSerializer::class) val colorScheme: ColorScheme,
    val creator: String = "Guest",
    val titleImage: ByteArray = byteArrayOf(),
    val uuid: String,
    val questionTextStyle: List<Int> = listOf(0, 0, 1, 0),
    val bodyTextStyle: List<Int> = listOf(0, 0, 2, 1),
    val answerTextStyle: List<Int> = listOf(0, 0, 0, 2),
    val tags: Set<String> = emptySet(),
    val scoreCard: ScoreCard,
    val quizzes: List<QuizJson>,
)

fun QuizLayoutViewModel.changeToJson(scoreCard: ScoreCard): String {
    if(quizData.value.uuid == null) this.generateUUIDWithTitle()
    val quizLayoutSerializer = QuizLayoutSerializer(
        backgroundImage = ImageColor(
            color = Color(0xFF000000),
            imageData = byteArrayOf(),
            color2 = Color(0xFFFFFFFF),
            state = ImageColorState.COLOR
        ),
        title = quizData.value.title,
        colorScheme = quizTheme.value.colorScheme,
        creator = quizData.value.creator,
        titleImage = quizData.value.image ?: byteArrayOf(),
        uuid = quizData.value.uuid!!,
        questionTextStyle = quizTheme.value.questionTextStyle,
        bodyTextStyle = quizTheme.value.bodyTextStyle,
        answerTextStyle = quizTheme.value.answerTextStyle,
        tags = quizData.value.tags,
        scoreCard = scoreCard,
        quizzes = quizzes.value!!.map { it.changeToJson() }
    )
    return Json.encodeToString(quizLayoutSerializer)
}

fun QuizLayoutViewModel.load(json: String) : ScoreCard {
    val quizLayoutSerializer = Json.decodeFromString<QuizLayoutSerializer>(json)
    this.setBackgroundImage(quizLayoutSerializer.backgroundImage)
    this.updateShuffleQuestions(quizLayoutSerializer.shuffleQuestions)
    this.quizData.value.title = quizLayoutSerializer.title
    this.quizTheme.value.colorScheme = quizLayoutSerializer.colorScheme
    this.quizData.value.creator = quizLayoutSerializer.creator
    this.quizData.value.image = quizLayoutSerializer.titleImage
    this.quizData.value.uuid = quizLayoutSerializer.uuid
    this.quizTheme.value.questionTextStyle = quizLayoutSerializer.questionTextStyle
    this.quizTheme.value.bodyTextStyle = quizLayoutSerializer.bodyTextStyle
    this.quizTheme.value.answerTextStyle = quizLayoutSerializer.answerTextStyle
    this.quizData.value.tags = quizLayoutSerializer.tags
    quizLayoutSerializer.quizzes.map{
        this.addQuiz(it.load())
    }
    return quizLayoutSerializer.scoreCard
}
