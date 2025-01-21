package com.asu1.quizcardmodel

import android.util.Base64
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import java.lang.reflect.Type

class QuizCardListDeserializer : JsonDeserializer<List<QuizCard>> {
    private val quizCardDeserializer = QuizCardDeserializer()

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<QuizCard> {
        val jsonArray = json.asJsonArray
        return jsonArray.map { quizCardDeserializer.deserialize(it, QuizCard::class.java, context) }
    }
}
class QuizCardDeserializer : JsonDeserializer<QuizCard> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): QuizCard {
        val jsonObject = json.asJsonObject

        val id = jsonObject.get("id").asString
        val title = jsonObject.get("title").asString
        val tags = jsonObject.get("tags").asJsonArray.map { it.asString }
        val creator = jsonObject.get("creator").asString
        val imageString = jsonObject.get("image").asString
        val image = Base64.decode(imageString, Base64.DEFAULT)
        val imageOrNull = if (image.size < 5) null else image
        val count = jsonObject.get("count").asInt
        val description = if (jsonObject.has("description")) jsonObject.get("description").asString else ""

        return QuizCard(
            id,
            title,
            tags,
            creator,
            imageOrNull,
            count,
            description
        )
    }
}

object KQuizCardDeserializer : KSerializer<QuizCard> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("QuizCard") {
        element<String>("id")
        element<String>("title")
        element<List<String>>("tags")
        element<String>("creator")
        element<String>("image")
        element<Int>("count")
        element<String>("description")
    }

    override fun deserialize(decoder: Decoder): QuizCard {
        return decoder.decodeStructure(descriptor) {
            var id = ""
            var title = ""
            var tags: List<String> = emptyList()
            var creator = ""
            var image: ByteArray? = null
            var count = 0
            var description = ""

            while (true) {
                when (decodeElementIndex(descriptor)) {
                    0 -> id = decodeStringElement(descriptor, 0)
                    1 -> title = decodeStringElement(descriptor, 1)
                    2 -> tags = decodeSerializableElement(descriptor, 2, ListSerializer(String.serializer()))
                    3 -> creator = decodeStringElement(descriptor, 3)
                    4 -> {
                        val imageString = decodeStringElement(descriptor, 4)
                        image = Base64.decode(imageString, Base64.DEFAULT)
                    }
                    5 -> count = decodeIntElement(descriptor, 5)
                    6 -> description = decodeStringElement(descriptor, 6)
                    else -> break
                }
            }
            QuizCard(id, title, tags, creator, image, count, description)
        }
    }

    override fun serialize(encoder: Encoder, value: QuizCard) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.title)
            encodeSerializableElement(descriptor, 2, ListSerializer(String.serializer()), value.tags)
            encodeStringElement(descriptor, 3, value.creator)
            encodeStringElement(descriptor, 4, Base64.encodeToString(value.image, Base64.DEFAULT))
            encodeIntElement(descriptor, 5, value.count)
            encodeStringElement(descriptor, 6, value.description)
        }
    }
}