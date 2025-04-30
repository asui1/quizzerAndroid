package com.asu1.models.serializers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.graphics.createBitmap
import com.asu1.utils.Logger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

object BodyTypeSerializer : KSerializer<BodyType> {
    private const val LEGACY_FQCN = "com.asu1.quizzer.model.BodyType."

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("BodyType") {
        element<String>("type")
        element<String>("bodyText", isOptional = true)
        element<String>("bodyImage", isOptional = true)
        element<String>("youtubeId", isOptional = true)
        element<Int>("youtubeStartTime", isOptional = true)
        element<String>("code", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: BodyType) {
        require(encoder is JsonEncoder)
        val jsonElem = when (value) {
            BodyType.NONE -> buildJsonObject { put("type", "NONE") }
            is BodyType.TEXT -> buildJsonObject {
                put("type", "TEXT")
                put("bodyText", value.bodyText)
            }
            is BodyType.IMAGE -> buildJsonObject {
                put("type", "IMAGE")
                // Convert Bitmap to Base64 string
                val stream = java.io.ByteArrayOutputStream()
                value.bodyImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val encoded = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
                put("bodyImage", encoded)
            }
            is BodyType.YOUTUBE -> buildJsonObject {
                put("type", "YOUTUBE")
                put("youtubeId", value.youtubeId)
                put("youtubeStartTime", value.youtubeStartTime)
            }
            is BodyType.CODE -> buildJsonObject {
                put("type", "CODE")
                put("code", value.code)
            }
        }
        encoder.encodeJsonElement(jsonElem)
    }

    override fun deserialize(decoder: Decoder): BodyType {
        return try {
            require(decoder is JsonDecoder)
            // Decode raw element (could be object or string)
            val rawElem = decoder.decodeJsonElement()

            // Unwrap potentially nested JSON-in-string formats
            val obj = when {
                rawElem is JsonObject -> rawElem
                rawElem is JsonPrimitive && rawElem.isString -> {
                    // Recursively parse string content until we get a JsonObject
                    var parsed: JsonElement = rawElem
                    while (parsed is JsonPrimitive && parsed.isString) {
                        parsed = Json.parseToJsonElement(parsed.content)
                    }
                    if (parsed is JsonObject) parsed.jsonObject else return BodyType.NONE
                }
                else -> return BodyType.NONE
            }

            // Extract and normalize type
            val rawType = obj["type"]?.jsonPrimitive?.content ?: return BodyType.NONE
            val type = rawType.removePrefix(LEGACY_FQCN)

            // Build the appropriate BodyType
            return when (type) {
                "NONE" -> BodyType.NONE
                "TEXT" -> BodyType.TEXT(obj["bodyText"]?.jsonPrimitive?.content.orEmpty())
                "IMAGE" -> {
                    val imgElem = obj["bodyImage"]
                    val bitmap = when (imgElem) {
                        is JsonArray -> {
                            val bytes = imgElem.map { it.jsonPrimitive.int.toByte() }.toByteArray()
                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: createEmptyBitmap()
                        }
                        is JsonPrimitive -> {
                            val enc = imgElem.content.trim()
                            if (enc.isEmpty() || enc == "[]") createEmptyBitmap()
                            else {
                                val data = Base64.decode(enc, Base64.DEFAULT)
                                BitmapFactory.decodeByteArray(data, 0, data.size) ?: createEmptyBitmap()
                            }
                        }
                        else -> createEmptyBitmap()
                    }
                    BodyType.IMAGE(bitmap)
                }
                "YOUTUBE" -> BodyType.YOUTUBE(
                    obj["youtubeId"]?.jsonPrimitive?.content.orEmpty(),
                    obj["youtubeStartTime"]?.jsonPrimitive?.int ?: 0
                )
                "CODE" -> BodyType.CODE(obj["code"]?.jsonPrimitive?.content.orEmpty())
                else -> BodyType.NONE
            }
        } catch (e: Exception) {
            Logger.debug("Body Type deserialization failed : $e")
            BodyType.NONE
        }
    }

    private fun createEmptyBitmap(): Bitmap {
        // implement your empty-bitmap creation logic
        return createBitmap(1, 1)
    }
}