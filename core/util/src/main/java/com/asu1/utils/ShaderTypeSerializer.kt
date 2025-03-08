package com.asu1.utils

import com.asu1.utils.shaders.ShaderType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ShaderTypeSerializer : KSerializer<ShaderType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ShaderType", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: ShaderType) {
        encoder.encodeInt(value.index)
    }
    fun removePrefixAndConvertToInt(text: String): Int {
        return text.removePrefix("Brush").toInt()
    }
    override fun deserialize(decoder: Decoder): ShaderType {
        val text = decoder.decodeInt()
        return ShaderType.entries.first { it.index == text }
    }
}
