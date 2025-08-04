package com.asu1.models.serializers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.graphics.createBitmap
import com.asu1.utils.Logger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
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
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
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

    override fun deserialize(decoder: Decoder): BodyType =
        if (decoder !is JsonDecoder) {
            BodyType.NONE
        } else {
            try {
                val rawElem = decoder.decodeJsonElement()
                val obj = rawElem.unwrapToJsonObject()
                val rawType = obj?.getString("type")
                val type = rawType?.removePrefix(LEGACY_FQCN)

                when (type) {
                    "NONE" -> BodyType.NONE
                    "TEXT" -> BodyType.TEXT(obj.getString("bodyText").orEmpty())
                    "IMAGE" -> BodyType.IMAGE(obj.getImageBitmap())
                    "YOUTUBE" -> BodyType.YOUTUBE(
                        obj.getString("youtubeId").orEmpty(),
                        obj.getInt("youtubeStartTime") ?: 0
                    )
                    "CODE" -> BodyType.CODE(obj.getString("code").orEmpty())
                    else -> BodyType.NONE
                }
            } catch (e: SerializationException) {
                Logger.debug("BodyType deserialization failed (serialization): $e")
                BodyType.NONE
            } catch (e: IllegalArgumentException) {
                Logger.debug("BodyType deserialization failed (invalid value): $e")
                BodyType.NONE
            }
        }

    private fun JsonElement.unwrapToJsonObject(): JsonObject? {
        var current: JsonElement = this
        while (current is JsonPrimitive && current.isString) {
            current = runCatching { Json.parseToJsonElement(current.content) }.getOrNull() ?: return null
        }
        return current as? JsonObject
    }

    private fun JsonObject.getString(key: String): String? =
        this[key]?.jsonPrimitive?.contentOrNull

    private fun JsonObject.getInt(key: String): Int? =
        this[key]?.jsonPrimitive?.intOrNull

    private fun JsonObject.getImageBitmap(): Bitmap {
        val imgElem = this["bodyImage"]
        return when (imgElem) {
            is JsonArray -> {
                val bytes = imgElem.mapNotNull { it.jsonPrimitive.intOrNull?.toByte() }.toByteArray()
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: createEmptyBitmap()
            }
            is JsonPrimitive -> {
                val enc = imgElem.content.trim()
                if (enc.isEmpty() || enc == "[]") return createEmptyBitmap()
                val data = Base64.decode(enc, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(data, 0, data.size) ?: createEmptyBitmap()
            }
            else -> createEmptyBitmap()
        }
    }

    private fun createEmptyBitmap(): Bitmap {
        // implement your empty-bitmap creation logic
        return createBitmap(1, 1)
    }
}
