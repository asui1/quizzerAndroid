package com.asu1.models.serializers

import androidx.compose.ui.geometry.Offset
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.Quiz3
import com.asu1.models.quiz.Quiz4
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.time.LocalDate
import java.time.YearMonth

val quizSerializersModule = SerializersModule {
    polymorphic(QuizJson::class) {
        subclass(QuizJson.Quiz1Json::class)
        subclass(QuizJson.Quiz2Json::class)
        subclass(QuizJson.Quiz3Json::class)
        subclass(QuizJson.Quiz4Json::class)
    }
}

val colorSerializersModule = SerializersModule{
    contextual(com.asu1.colormodel.ColorSerializer)
}
val bodyTypeSerializersModule = SerializersModule {
    contextual(BodyTypeSerializer)
}

val combinedSerializersModule = SerializersModule {
    include(quizSerializersModule)
    include(colorSerializersModule)
    include(bodyTypeSerializersModule)
}
val json = Json {
    classDiscriminator = "layoutType"
    serializersModule = combinedSerializersModule
    ignoreUnknownKeys = true
}

fun extractYouTubeDetails(jsonString: String): Pair<String, Int> {
    val regex = """"youtubeId":"(.*?)","youtubeStartTime":(\d+)""".toRegex()
    val matchResult = regex.find(jsonString)
    val youtubeId = matchResult?.groups?.get(1)?.value ?: ""
    val youtubeStartTime = matchResult?.groups?.get(2)?.value?.toInt() ?: 0
    return Pair(youtubeId, youtubeStartTime)
}
fun extractBodyText(jsonString: String): String {
    val regex = """"bodyText":"(.*?)"""".toRegex()
    val matchResult = regex.find(jsonString)
    val bodyText = matchResult?.groups?.get(1)?.value ?: ""
    return bodyText.replace("\\n", "\n")
}
fun manualDeserializer(input: String): BodyType{
    if(input.contains("quizzer.model.BodyType.NONE")){
        return BodyType.NONE
    }else if(input.contains("quizzer.model.BodyType.TEXT")) {
//Old Input: {"type":"com.asu1.quizzer.model.BodyType.TEXT","value":1,"bodyText":"I'm stanning, just stanning you\n(_________________)\n\n오늘도 스치듯 그 말이"}
        val bodyText = extractBodyText(input)
        return BodyType.TEXT(bodyText)
    }else if(input.contains("quizzer.model.BodyType.IMAGE")) {
        return BodyType.IMAGE(ByteArray(0))
    }else if(input.contains("quizzer.model.BodyType.YOUTUBE")) {
//Old Input: {"type":"com.asu1.quizzer.model.BodyType.YOUTUBE","value":3,"youtubeId":"v7bnOxV4jAc","youtubeStartTime":0}
        val (youtubeId, youtubeStartTime) = extractYouTubeDetails(input)
        return BodyType.YOUTUBE(youtubeId, youtubeStartTime)
    }
    return json.decodeFromString(input)
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
        override fun load(): Quiz1 {
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
                bodyType = manualDeserializer(body.bodyValue),
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
        val bodyValue: String,
        val points: Int = 10,
    )

    @Serializable
    @SerialName("1")
    data class Quiz2Json(
        val body: Quiz2Body
    ) : QuizJson(){
        override fun load(): Quiz2 {
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
                bodyType = manualDeserializer(body.bodyValue),
                point = body.points,
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
        val bodyValue: String,
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
                    body.connectionAnswers.forEach { _ -> add(null to null) }
                },
                bodyType = manualDeserializer(body.bodyValue),
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
        val bodyValue: String,
        val points: Int = 10,
    )
}

