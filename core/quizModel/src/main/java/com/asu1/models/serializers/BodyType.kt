package com.asu1.models.serializers

import kotlinx.serialization.Serializable

@Serializable(with = BodyTypeSerializer::class)
sealed class BodyType(val value: Int) {

    object NONE : BodyType(value = 0)

    data class TEXT(val bodyText: String) : BodyType(value = 1)

    data class IMAGE(val bodyImage: ByteArray) : BodyType(value = 2) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            if (!super.equals(other)) return false

            other as IMAGE

            return bodyImage.contentEquals(other.bodyImage)
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + bodyImage.contentHashCode()
            return result
        }
    }

    data class YOUTUBE(val youtubeId: String, val youtubeStartTime: Int) : BodyType(value = 3)

    fun validate() : BodyType {
        return when(this){
            is TEXT -> {
                if(this.bodyText.isEmpty()){
                    NONE
                }else{
                    this
                }
            }
            is IMAGE -> {
                if(this.bodyImage.isEmpty()){
                    NONE
                }else{
                    this
                }
            }
            is YOUTUBE -> {
                if(this.youtubeId.isEmpty()){
                    NONE
                }else{
                    this
                }
            }
            is NONE -> NONE
            else -> NONE
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