package com.asu1.imagecolor

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import com.asu1.colormodel.ColorSerializer
import com.asu1.utils.ShaderTypeSerializer
import com.asu1.utils.images.BitmapSerializer
import com.asu1.utils.images.createEmptyBitmap
import com.asu1.utils.shaders.ShaderType
import kotlinx.serialization.Serializable

@Serializable
data class ImageColor(
    @Serializable(with = BitmapSerializer::class)
    val imageData: Bitmap = createEmptyBitmap(),
    @Serializable(with = ColorSerializer::class)
    val color: Color = Color.Transparent,
    @Serializable(with = ColorSerializer::class)
    val color2: Color = Color.Transparent,
    @Serializable(with = ColorSerializer::class)
    val colorGradient: Color = Color.Black,
    val state: ImageColorState = ImageColorState.COLOR,
    val backgroundBase: BackgroundBase = BackgroundBase.SKY_WARM,
    val effect: Effect = Effect.NONE,
    @Serializable(with = ShaderTypeSerializer::class) var shaderType: ShaderType = ShaderType.Brush1,
    @Serializable(with = BitmapSerializer::class)
    val overlayImage: Bitmap = createEmptyBitmap(),
    val effectGraphics: List<EffectGraphicsInfo> = listOf(),
    val imageBlendMode: ImageBlendMode = ImageBlendMode.BLENDHUE,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageColor) return false

        if (imageData != other.imageData) return false
        if (color != other.color) return false
        if (color2 != other.color2) return false
        if (colorGradient != other.colorGradient) return false
        if (state != other.state) return false
        if(backgroundBase != other.backgroundBase) return false
        if(effect != other.effect) return false
        if(shaderType != other.shaderType) return false
        if(overlayImage != other.overlayImage) return false
        if(effectGraphics != other.effectGraphics) return false
        if(imageBlendMode != other.imageBlendMode) return false
        return true
    }

    override fun hashCode(): Int {
        var result = imageData.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + color2.hashCode()
        result = 31 * result + colorGradient.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + backgroundBase.hashCode()
        result = 31 * result + effect.hashCode()
        result = 31 * result + shaderType.hashCode()
        result = 31 * result + overlayImage.hashCode()
        return result
    }
}
