package com.asu1.models.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate

object LocalDateSerializer : KSerializer<LocalDate> {
    // <-- use buildSerialDescriptor, not SerialDescriptor(...)
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("LocalDate", StructureKind.LIST)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val listEnc = encoder.beginCollection(descriptor, 3)
        listEnc.encodeIntElement(descriptor, 0, value.year)
        listEnc.encodeIntElement(descriptor, 1, value.monthValue)
        listEnc.encodeIntElement(descriptor, 2, value.dayOfMonth)
        listEnc.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val dec = decoder.beginStructure(descriptor)
        var year = 0
        var month = 0
        var day = 0
        loop@ while (true) {
            when (dec.decodeElementIndex(descriptor)) {
                0 -> year  = dec.decodeIntElement(descriptor, 0)
                1 -> month = dec.decodeIntElement(descriptor, 1)
                2 -> day   = dec.decodeIntElement(descriptor, 2)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> throw SerializationException("Unexpected index \$idx")
            }
        }
        dec.endStructure(descriptor)
        return LocalDate.of(year, month, day)
    }
}

@Suppress("unused")
object LocalDateListSerializer :
    KSerializer<List<LocalDate>> by ListSerializer(LocalDateSerializer)

object  LocalDateSetSerializer :
        KSerializer<Set<LocalDate>> by SetSerializer(LocalDateSerializer)
