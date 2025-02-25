package com.asu1.models.serializers

import android.graphics.Bitmap
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
                if(this.bodyImage.width <= 1){
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