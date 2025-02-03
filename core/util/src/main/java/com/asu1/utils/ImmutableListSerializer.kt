package com.asu1.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.builtins.ListSerializer
import com.google.common.collect.ImmutableList
import kotlinx.serialization.descriptors.SerialDescriptor

class ImmutableListSerializer<T>(private val elementSerializer: KSerializer<T>) : KSerializer<ImmutableList<T>> {
    private val listSerializer = ListSerializer(elementSerializer)
    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: ImmutableList<T>) {
        listSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): ImmutableList<T> {
        return ImmutableList.copyOf(listSerializer.deserialize(decoder))
    }
}