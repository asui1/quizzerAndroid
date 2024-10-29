package com.asu1.quizzer.data

import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val quizSerializersModule = SerializersModule {
    polymorphic(QuizJson::class) {
        subclass(Quiz1Json::class)
        subclass(Quiz2Json::class)
        subclass(Quiz3Json::class)
        subclass(Quiz4Json::class)
    }
}
val json = Json {
    serializersModule = quizSerializersModule
    ignoreUnknownKeys = true
}

@Serializable
@Polymorphic
sealed class QuizJson {
    abstract val layoutType: Int
    abstract fun load() : Quiz
}

@Serializable
@SerialName("Quiz1Json")
data class Quiz1Json(
    override val layoutType: Int,
    val body: Quiz1Body
) : QuizJson(){
    override fun load(): Quiz1{
        val quiz = Quiz1()
        quiz.load(this.toString())
        return quiz
    }
}


@Serializable
data class Quiz1Body(
    val bodyType: Int,
    val image: String?,
    val bodyText: String,
    val shuffleAnswers: Boolean,
    val maxAnswerSelection: Int,
    val answers: List<String>,
    val ans: List<Boolean>,
    val question: String,
    val youtubeId: String?,
    val points: Int,
    val youtubeStartTime: Int?
)

@Serializable
@SerialName("Quiz2Json")
data class Quiz2Json(
    override val layoutType: Int,
    val body: Quiz2Body
) : QuizJson(){
    override fun load(): Quiz2{
        val quiz = Quiz2()
        quiz.load(this.toString())
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
@SerialName("Quiz3Json")
data class Quiz3Json(
    override val layoutType: Int,
    val body: Quiz3Body
) : QuizJson(){
    override fun load(): Quiz3 {
        val quiz = Quiz3()
        quiz.load(this.toString())
        return quiz
    }
}

@Serializable
data class Quiz3Body(
    val layoutType: Int,
    val maxAnswerSelection: Int,
    val answers: List<String>,
    val ans: List<Boolean>,
    val points: Int,
    val question: String
)

@Serializable
@SerialName("Quiz4Json")
data class Quiz4Json(
    override val layoutType: Int,
    val body: Quiz4Body
) : QuizJson(){
    override fun load(): Quiz4 {
        val quiz = Quiz4()
        quiz.load(this.toString())
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
    val points: Int,
    val question: String
)