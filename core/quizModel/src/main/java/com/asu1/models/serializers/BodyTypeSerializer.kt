package com.asu1.models.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object BodyTypeSerializer : KSerializer<BodyType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BodyType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BodyType) {
        val json = when (value) {
            is BodyType.NONE -> JsonObject(mapOf("type" to JsonPrimitive("NONE")))
            is BodyType.TEXT -> JsonObject(mapOf("type" to JsonPrimitive("TEXT"), "bodyText" to JsonPrimitive(value.bodyText)))
            is BodyType.IMAGE -> JsonObject(mapOf("type" to JsonPrimitive("IMAGE"), "bodyImage" to JsonPrimitive(value.bodyImage.decodeToString())))
            is BodyType.YOUTUBE -> JsonObject(mapOf("type" to JsonPrimitive("YOUTUBE"), "youtubeId" to JsonPrimitive(value.youtubeId), "youtubeStartTime" to JsonPrimitive(value.youtubeStartTime)))
        }
        encoder.encodeString(json.toString())
    }

    override fun deserialize(decoder: Decoder): BodyType {
        try{
            val json = Json.parseToJsonElement(decoder.decodeString()).jsonObject
            val type = json["type"]?.jsonPrimitive?.content
            return when (type) {
                "NONE" -> BodyType.NONE
                "TEXT" -> BodyType.TEXT(json["bodyText"]?.jsonPrimitive?.content ?: "")
                "IMAGE" -> BodyType.IMAGE(json["bodyImage"]?.jsonPrimitive?.content?.toByteArray() ?: ByteArray(0))
                "YOUTUBE" -> BodyType.YOUTUBE(
                    json["youtubeId"]?.jsonPrimitive?.content ?: "",
                    json["youtubeStartTime"]?.jsonPrimitive?.int ?: 0
                )
                "com.asu1.quizzer.model.BodyType.NONE" -> BodyType.NONE
                // Add other cases for different BodyType values
                else -> throw IllegalArgumentException("Unknown BodyType: $type")
            }
        } catch (e: Exception){
            return BodyType.NONE
        }
    }
}