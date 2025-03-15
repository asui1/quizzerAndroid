package com.asu1.quizcardmodel

import com.asu1.baseinterfaces.HasUniqueId
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.UUID
import androidx.compose.runtime.Immutable

@Immutable
@Serializable(with = KQuizCardDeserializer::class)
data class QuizCard(
    @SerializedName("id")
    override val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("creator")
    val creator: String,
    @SerializedName("image")
    val image: ByteArray?,
    @SerializedName("count")
    val count: Int,
    @SerializedName("description")
    val description: String = ""
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

