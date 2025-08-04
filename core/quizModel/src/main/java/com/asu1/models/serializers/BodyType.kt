package com.asu1.models.serializers

import android.graphics.Bitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import com.asu1.resources.R
import com.asu1.utils.images.createEmptyBitmap
import kotlinx.serialization.Serializable

@Serializable(with = BodyTypeSerializer::class)
sealed class BodyType(val value: Int) {

    object NONE : BodyType(value = 0)

    data class TEXT(val bodyText: String) : BodyType(value = 1)

    data class IMAGE(val bodyImage: Bitmap = createEmptyBitmap()) : BodyType(value = 2) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            if (!super.equals(other)) return false

            other as IMAGE

            return bodyImage == other.bodyImage
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + bodyImage.hashCode()
            return result
        }
    }

    data class YOUTUBE(val youtubeId: String, val youtubeStartTime: Int) : BodyType(value = 3)

    data class CODE(val code: String) : BodyType(value = 4)

    companion object {
        // Provides a list of BodyType objects for iteration
        val values: List<BodyType> = listOf(
            TEXT(""), IMAGE(), YOUTUBE("", 0), CODE("")
        )
    }
    val icon: ImageVector
        get() = when (this) {
            is NONE -> Icons.Default.Block
            is TEXT -> Icons.Default.TextFields
            is IMAGE -> Icons.Default.Image
            is YOUTUBE -> Icons.Default.VideoLibrary
            is CODE -> Icons.Default.Code
        }
    // Mapping labels to each BodyType
    val labelRes: Int
        get() = when (this) {
            is NONE -> R.string.none
            is TEXT -> R.string.body_text
            is IMAGE -> R.string.add_image
            is YOUTUBE -> R.string.youtube
            is CODE -> R.string.code
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BodyType) return false
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value
    }
}
