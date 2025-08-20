package com.asu1.quizcardmodel

import android.util.Base64
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

object QuizCardSerializer : KSerializer<QuizCard> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("QuizCard") {
        element<String>("id")
        element<String>("title")
        element<List<String>>("tags")
        element<String>("creator")
        element<String>("image")
        element<Int>("count")
        element<String>("description", isOptional = true)
    }

    override fun deserialize(decoder: Decoder): QuizCard {
        val input = decoder as? JsonDecoder
            ?: error("QuizCardSerializer can be used only with JSON")

        val obj = input.decodeJsonElement().jsonObject

        fun string(key: String): String =
            obj[key]?.jsonPrimitive?.content
                ?: error("Missing '$key'")

        fun optionalString(key: String): String? =
            obj[key]?.jsonPrimitive?.contentOrNull

        fun stringList(key: String): List<String> =
            obj[key]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()

        val id          = string("id")
        val title       = string("title")
        val tags        = stringList("tags")
        val creator     = string("creator")
        val imageBase64 = optionalString("image")
        val count       = obj["count"]?.jsonPrimitive?.int
            ?: error("Missing 'count'")
        val description = optionalString("description") ?: ""

        val image: ByteArray? = imageBase64
            ?.takeIf { it.isNotBlank() }
            ?.let { Base64.decode(it, Base64.DEFAULT) }
            ?.let { bytes -> if (bytes.size < 5) null else bytes }

        return QuizCard(
            id = id,
            title = title,
            tags = tags,
            creator = creator,
            image = image,
            count = count,
            description = description
        )
    }

    override fun serialize(encoder: Encoder, value: QuizCard) {
        val output = encoder as? JsonEncoder
            ?: error("QuizCardSerializer can be used only with JSON")

        val json = buildJsonObject {
            put("id", value.id)
            put("title", value.title)
            put("tags", JsonArray(value.tags.map { JsonPrimitive(it) }))
            put("creator", value.creator)
            // if null, omit the field; or use JsonNull if your server expects it
            value.image?.let { bytes ->
                put("image", Base64.encodeToString(bytes, Base64.NO_WRAP))
            }
            put("count", value.count)
            // include description only when non-empty, or always if you prefer
            if (value.description.isNotEmpty()) put("description", value.description)
        }

        output.encodeJsonElement(json)
    }
}
