package com.asu1.quizzer.model

import kotlinx.serialization.Serializable

@Serializable
sealed class BodyType(val value: Int) {
    @Serializable
    object NONE : BodyType(value = 0) {
        override fun equals(other: Any?): Boolean {
            return other is NONE
        }

        override fun hashCode(): Int {
            return value
        }
    }

    @Serializable
    data class TEXT(val bodyText: String) : BodyType(value = 1) {
        override fun equals(other: Any?): Boolean {
            return other is TEXT && other.bodyText == this.bodyText
        }

        override fun hashCode(): Int {
            return 31 * value + bodyText.hashCode()
        }
    }

    @Serializable
    data class IMAGE(val bodyImage: ByteArray) : BodyType(value = 2) {
        override fun equals(other: Any?): Boolean {
            return other is IMAGE && other.bodyImage.contentEquals(this.bodyImage)
        }

        override fun hashCode(): Int {
            return 31 * value + bodyImage.contentHashCode()
        }
    }

    @Serializable
    data class YOUTUBE(val youtubeId: String, val youtubeStartTime: Int) : BodyType(value = 3) {
        override fun equals(other: Any?): Boolean {
            return other is YOUTUBE && other.youtubeId == this.youtubeId && other.youtubeStartTime == this.youtubeStartTime
        }

        override fun hashCode(): Int {
            var result = 31 * value + youtubeId.hashCode()
            result = 31 * result + youtubeStartTime
            return result
        }
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