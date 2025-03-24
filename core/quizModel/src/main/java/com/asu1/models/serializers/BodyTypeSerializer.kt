package com.asu1.models.serializers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.util.Base64
import com.asu1.utils.Logger
import com.asu1.utils.images.createEmptyBitmap
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import java.io.ByteArrayOutputStream

object BodyTypeSerializer : KSerializer<BodyType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BodyType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BodyType) {
        val json = when (value) {
            is BodyType.NONE -> JsonObject(mapOf("type" to JsonPrimitive("NONE")))
            is BodyType.TEXT -> JsonObject(
                mapOf(
                    "type" to JsonPrimitive("TEXT"),
                    "bodyText" to JsonPrimitive(value.bodyText)
                )
            )
            is BodyType.IMAGE -> {
                // Convert the Bitmap to a Base64 encoded string
                val stream = ByteArrayOutputStream()
                value.bodyImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
                JsonObject(mapOf("type" to JsonPrimitive("IMAGE"), "bodyImage" to JsonPrimitive(encoded)))
            }
            is BodyType.YOUTUBE -> JsonObject(
                mapOf(
                    "type" to JsonPrimitive("YOUTUBE"),
                    "youtubeId" to JsonPrimitive(value.youtubeId),
                    "youtubeStartTime" to JsonPrimitive(value.youtubeStartTime)
                )
            )
            is BodyType.CODE -> JsonObject(
                mapOf(
                    "type" to JsonPrimitive("CODE"),
                    "code" to JsonPrimitive(value.code)
                )
            )
        }
        encoder.encodeString(json.toString())
    }

    override fun deserialize(decoder: Decoder): BodyType {
        try {
            val json = Json.parseToJsonElement(decoder.decodeString()).jsonObject
            val type = json["type"]?.jsonPrimitive?.content
            return when (type) {
                "NONE" -> BodyType.NONE
                "TEXT" -> BodyType.TEXT(json["bodyText"]?.jsonPrimitive?.content ?: "")
                "IMAGE" -> {
                    Logger.debug("Quiz Body Deserialize")
                    val bodyImageElement: JsonElement? = json["bodyImage"]

                    val bitmap: Bitmap = when {
                        // If the element is null, return an empty Bitmap.
                        bodyImageElement == null -> {
                            createEmptyBitmap()
                        }
                        // If it's an empty JSON array, return an empty Bitmap.
                        bodyImageElement is JsonArray && bodyImageElement.isEmpty() -> {
                            createEmptyBitmap()
                        }
                        // If it's a JSON array of numbers, convert it to a ByteArray.
                        bodyImageElement is JsonArray -> {
                            val byteArray = bodyImageElement.map { it.jsonPrimitive.int.toByte() }.toByteArray()
                            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                                ?: createEmptyBitmap()
                        }
                        // If it's a JSON primitive string, treat it as a Base64 string.
                        bodyImageElement is JsonPrimitive && bodyImageElement.isString -> {
                            val encoded = bodyImageElement.content.trim()
                            if (encoded.isEmpty() || encoded == "[]") {
                                createEmptyBitmap()
                            } else {
                                val byteArray = Base64.decode(encoded, Base64.DEFAULT)
                                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                                    ?: createEmptyBitmap()
                            }
                        }
                        // Fallback for unexpected JSON element types.
                        else -> {
                            createEmptyBitmap()
                        }
                    }
                    BodyType.IMAGE(bitmap)
                }
                "YOUTUBE" -> BodyType.YOUTUBE(
                    json["youtubeId"]?.jsonPrimitive?.content ?: "",
                    json["youtubeStartTime"]?.jsonPrimitive?.int ?: 0
                )
                "CODE" -> BodyType.CODE(json["code"]?.jsonPrimitive?.content ?: "")
                "com.asu1.quizzer.model.BodyType.NONE" -> BodyType.NONE
                else -> throw IllegalArgumentException("Unknown BodyType: $type")
            }
        } catch (e: Exception) {
            return BodyType.NONE
        }
    }
}