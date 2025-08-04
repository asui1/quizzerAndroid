package com.asu1.models.serializers

import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val quizSerializersModule = SerializersModule {
    polymorphic(Quiz::class){
        subclass(MultipleChoiceQuiz::class)
        subclass(DateSelectionQuiz::class)
        subclass(ReorderQuiz::class)
        subclass(ConnectItemsQuiz::class)
        subclass(ShortAnswerQuiz::class)
        subclass(FillInBlankQuiz::class)
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

@OptIn(ExperimentalSerializationApi::class)
object QuizSerializer : JsonTransformingSerializer<Quiz>(
    PolymorphicSerializer(Quiz::class)
) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val obj = element.jsonObject
        val typeField = obj["layoutType"]
        val bodyObj  = obj["body"]?.jsonObject
            ?: return element

        return buildJsonObject {
            bodyObj.forEach { (k,v) -> put(k, v) }
            if (typeField != null) put("layoutType", typeField)
        }
    }
}
