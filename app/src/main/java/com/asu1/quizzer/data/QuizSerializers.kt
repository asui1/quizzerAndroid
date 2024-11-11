package com.asu1.quizzer.data

import androidx.compose.ui.geometry.Offset
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.contextual
import java.time.LocalDate
import java.time.YearMonth
import java.util.Base64

val quizSerializersModule = SerializersModule {
    polymorphic(QuizJson::class) {
        subclass(QuizJson.Quiz1Json::class)
        subclass(QuizJson.Quiz2Json::class)
        subclass(QuizJson.Quiz3Json::class)
        subclass(QuizJson.Quiz4Json::class)
    }
}

val colorSerializersModule = SerializersModule{
    contextual(ColorSerializer)
}

val combinedSerializersModule = SerializersModule {
    include(quizSerializersModule)
    include(colorSerializersModule)
}
val json = Json {
    classDiscriminator = "layoutType"
    serializersModule = combinedSerializersModule
    ignoreUnknownKeys = true
}

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("layoutType")
@Serializable
sealed class QuizJson {
    abstract fun load() : Quiz
    abstract fun toQuiz(): Quiz

    @Serializable
    @SerialName("0")
    data class Quiz1Json(
        val body: Quiz1Body
    ) : QuizJson(){
        override fun load(): Quiz1{
            val quiz = Quiz1()
            quiz.load(this.toString())
            return quiz
        }

        override fun toQuiz(): Quiz1 {
            val quiz = Quiz1(
                question = body.question,
                answers = body.answers.toMutableList(),
                ans = body.ans.toMutableList(),
                shuffleAnswers = body.shuffleAnswers,
                bodyImage = Base64.getDecoder().decode(body.bodyImage),
                bodyText = body.bodyText,
                bodyType = BodyType.entries[body.bodyType],
                youtubeId = body.youtubeId,
                youtubeStartTime = body.youtubeStartTime,
                point = body.points
            )
            quiz.initViewState()
            return quiz
        }
    }


    @Serializable
    data class Quiz1Body(
        val shuffleAnswers: Boolean,
        val answers: List<String>,
        val ans: List<Boolean>,
        val question: String,
        val bodyImage: String = "",
        val bodyText: String = "",
        val bodyType: Int = 0,
        val youtubeId: String = "",
        val youtubeStartTime: Int = 0,
        val points: Int = 10,
    )

    @Serializable
    @SerialName("1")
    data class Quiz2Json(
        val body: Quiz2Body
    ) : QuizJson(){
        override fun load(): Quiz2{
            val quiz = Quiz2()
            quiz.load(this.toString())
            return quiz
        }

        override fun toQuiz(): Quiz2 {
            val quiz = Quiz2(
                question = body.question,
                answers = body.answers.toMutableList(),
                maxAnswerSelection = body.maxAnswerSelection,
                yearRange = body.yearRange,
                centerDate = YearMonth.of(body.centerDate[0], body.centerDate[1]),
                answerDate = body.answerDate.map{it -> LocalDate.of(it[0], it[1], it[2])}.toMutableSet(),
                point = body.points,
            )
            quiz.initViewState()
            return quiz
        }
    }

    @Serializable
    data class Quiz2Body(
        val centerDate: List<Int>,
        val yearRange: Int,
        val answerDate: List<List<Int>>,
        val maxAnswerSelection: Int,
        val answers: List<String>,
        val ans: List<Boolean>,
        val points: Int,
        val question: String
    )

    @Serializable
    @SerialName("2")
    data class Quiz3Json(
        val body: Quiz3Body
    ) : QuizJson(){
        override fun load(): Quiz3 {
            val quiz = Quiz3()
            quiz.load(this.toString())
            return quiz
        }

        override fun toQuiz(): Quiz3 {
            val quiz = Quiz3(
                question = body.question,
                answers = body.answers.toMutableList(),
                bodyImage = Base64.getDecoder().decode(body.bodyImage),
                bodyText = body.bodyText,
                bodyType = BodyType.entries[body.bodyType],
                youtubeId = body.youtubeId,
                youtubeStartTime = body.youtubeStartTime,
                point = body.points
            )
            quiz.initViewState()
            return quiz
        }
    }

    @Serializable
    data class Quiz3Body(
        val layoutType: Int,
        val maxAnswerSelection: Int,
        val answers: List<String>,
        val ans: List<Boolean>,
        val question: String,
        val bodyImage: String = "",
        val bodyText: String = "",
        val bodyType: Int = 0,
        val youtubeId: String = "",
        val youtubeStartTime: Int = 0,
        val points: Int = 10,
    )

    @Serializable
    @SerialName("3")
    data class Quiz4Json(
        val body: Quiz4Body
    ) : QuizJson(){
        override fun load(): Quiz4 {
            val quiz = Quiz4()
            quiz.load(this.toString())
            return quiz
        }

        override fun toQuiz(): Quiz {
            val quiz = Quiz4(
                question = body.question,
                answers = body.answers.toMutableList(),
                connectionAnswers = body.connectionAnswers.toMutableList(),
                connectionAnswerIndex = body.connectionAnswerIndex.toMutableList(),
                dotPairOffsets = mutableListOf<Pair<Offset?, Offset?>>().apply {
                    body.connectionAnswers.forEach { add(null to null) }
                },
                bodyImage = Base64.getDecoder().decode(body.bodyImage),
                bodyText = body.bodyText,
                bodyType = BodyType.entries[body.bodyType],
                youtubeId = body.youtubeId,
                youtubeStartTime = body.youtubeStartTime,
                point = body.points
            )
            quiz.initViewState()
            return quiz
        }
    }

    @Serializable
    data class Quiz4Body(
        val connectionAnswers: List<String>,
        val connectionAnswerIndex: List<Int?>,
        val layoutType: Int,
        val maxAnswerSelection: Int,
        val answers: List<String>,
        val ans: List<Boolean>,
        val question: String,
        val bodyImage: String = "",
        val bodyText: String = "",
        val bodyType: Int = 0,
        val youtubeId: String = "",
        val youtubeStartTime: Int = 0,
        val points: Int = 10,
    )
}

