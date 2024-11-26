package com.asu1.quizzer.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.quizcards.loadImageAsByteArray
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable(with = KQuizCardDeserializer::class)
data class QuizCard(
    @SerializedName("id")
    val id: String,
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
) {
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

@Composable
fun getSampleQuizCard(): QuizCard {
    val context = LocalContext.current
    val imageByte = loadImageAsByteArray(context, R.drawable.question2)
    return QuizCard(
        id = "1",
        title = "11Quiz 1. Sample of Quiz Cards. Can you solve this? This will go over 2 lines.",
        tags = listOf("tag1", "tag2"),
        creator = "Creator",
        image = imageByte,
        count = 3,
        description = "This is a sample quiz card. Please check how this is shown on screen."
    )
}