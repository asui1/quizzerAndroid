package com.asu1.colormodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.INT)

    @OptIn(ExperimentalStdlibApi::class)
    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString(value.toArgb().toHexString())
    }

    override fun deserialize(decoder: Decoder): Color {
        val hexString = decoder.decodeString()
        val color = hexString.toColor()
        return color
    }
    private fun String.toColor(): Color {
        val value = this.substring(1, 9).toLong(16)
        return Color(value.toInt())
    }

}