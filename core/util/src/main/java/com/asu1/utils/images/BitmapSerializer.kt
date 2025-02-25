package com.asu1.utils.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.Color
import com.asu1.utils.Logger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import java.io.ByteArrayOutputStream

object BitmapSerializer : KSerializer<Bitmap> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Bitmap", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Bitmap) {
        val stream = ByteArrayOutputStream()
        // Compress the bitmap into PNG format (lossless)
        value.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        // Encode the byte array to a Base64 string
        val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): Bitmap {
        // We assume a JSON decoder so we can inspect the underlying element.
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("This serializer only works with JSON")
        val element: JsonElement = jsonDecoder.decodeJsonElement()

        return when {
            // Handle the case where the element is an empty JSON array
            element is JsonArray && element.isEmpty() -> {
                createEmptyBitmap()
            }
            // Handle the case where the element is a JSON array of numbers
            element is JsonArray -> {
                val byteArray = element.map { jsonElement ->
                    // Expect each element to be a number. Convert to Int then to Byte.
                    jsonElement.jsonPrimitive.int.toByte()
                }.toByteArray()
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    ?:                 createEmptyBitmap()

            }
            // Handle the case where the element is a JSON primitive string (Base64 encoded)
            element is JsonPrimitive && element.isString -> {
                val encoded = element.content.trim()
                if (encoded.isEmpty() || encoded == "[]") {
                    createEmptyBitmap()

                } else {
                    val byteArray = Base64.decode(encoded, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                        ?:                 createEmptyBitmap()
                }
            }
            else -> {
                throw SerializationException("Unexpected JSON element for Bitmap: $element")
            }
        }
    }
}
