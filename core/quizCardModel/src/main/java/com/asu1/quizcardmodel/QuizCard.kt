package com.asu1.quizcardmodel

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable(with = QuizCardSerializer::class)
data class QuizCard(
    override val id: String,
    val title: String,
    val tags: List<String>,
    val creator: String,
    val image: ByteArray?,      // decoded from Base64 string in JSON
    val count: Int,
    val description: String = "" // default if field missing
) : HasUniqueId {
    constructor(
        title: String,
        creator: String,
        tags: List<String>,
        titleImageByte: ByteArray?,
        counts: Int
    ) : this(UUID.randomUUID().toString(), title, tags, creator, titleImageByte, counts)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizCard

        if (id != other.id) return false
        if (title != other.title) return false
        if (tags != other.tags) return false
        if (creator != other.creator) return false
        if (!image.contentEquals(other.image)) return false
        return count == other.count
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + creator.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + count
        return result
    }
}

