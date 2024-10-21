package com.asu1.quizzer.data

import com.asu1.quizzer.model.ShaderType
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

    override fun deserialize(decoder: Decoder): ShaderType {
        val index = decoder.decodeInt()
        return ShaderType.values().first { it.index == index }
    }
}
