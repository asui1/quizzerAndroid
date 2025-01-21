package com.asu1.imagecolor

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import com.asu1.colormodel.ColorSerializer
import com.asu1.resources.ShaderType
import com.asu1.utils.ShaderTypeSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ImageColor(
    val imageData: ByteArray = byteArrayOf(),
    @Serializable(with = ColorSerializer::class) val color: Color = Color.Transparent,
    @Serializable(with = ColorSerializer::class) val color2: Color = Color.White,
    @Serializable(with = ColorSerializer::class) val colorGradient: Color = Color.Black,
    val state: ImageColorState = ImageColorState.COLOR,
    val backgroundBase: BackgroundBase = BackgroundBase.SKY,
    val effect: Effect = Effect.NONE,
    @Serializable(with = ShaderTypeSerializer::class) var shaderType: ShaderType = ShaderType.Brush1,
    val overlayImage: ByteArray = byteArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageColor) return false

        if (!imageData.contentEquals(other.imageData)) return false
        if (color != other.color) return false
        if (color2 != other.color2) return false
        if (colorGradient != other.colorGradient) return false
        if (state != other.state) return false
        if(backgroundBase != other.backgroundBase) return false
        if(effect != other.effect) return false
        if(shaderType != other.shaderType) return false
        if(!overlayImage.contentEquals(other.overlayImage)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = imageData.contentHashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + color2.hashCode()
        result = 31 * result + colorGradient.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + backgroundBase.hashCode()
        result = 31 * result + effect.hashCode()
        result = 31 * result + shaderType.hashCode()
        result = 31 * result + overlayImage.contentHashCode()
        return result
    }

    fun getAsImage(): Painter {
        return when(state) {
            ImageColorState.COLOR -> ColorPainter(color)
            ImageColorState.BASEIMAGE -> ColorPainter(color2)
            ImageColorState.IMAGE -> if (imageData.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                BitmapPainter(bitmap.asImageBitmap())
            } else {
                ColorPainter(color)
            }
            else -> ColorPainter(color)
        }
    }
}