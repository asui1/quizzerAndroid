package com.asu1.quizzer.model

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asu1.models.serializers.ColorSerializer
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.effects.GradientBrush
import com.asu1.quizzer.data.ShaderTypeSerializer
import kotlinx.serialization.Serializable

//State 0 -> Color, State 1 -> Color1 + Color2, State 2 -> Image
@Serializable
enum class ImageColorState {
    COLOR, BASEIMAGE, IMAGE, GRADIENT
}

@Serializable
enum class BackgroundBase(val resourceId: Int){
    CITY(R.drawable.empty_city_sky),
    NIGHT1(R.drawable.nightsky),
    SKY(R.drawable.sky_background),
    SNOW(R.drawable.snowbase),
    PICNIC(R.drawable.picnic2),
    CONCERT(R.drawable.emptystage),
    FLOWER(R.drawable.prettyflower),
    SUNSET(R.drawable.sunset),
    TROPHY(R.drawable.trophy),
    COAST(R.drawable.seacoast),
    NIGHT2(R.drawable.nightsky2_background),
}

@Serializable
enum class Effect(val stringId: Int){
    NONE(R.string.none),
    FIREWORKS(R.string.firework),
    FIREWORKS2(R.string.fireworks2),
    MOON(R.string.moon),
    SHOOTING_STAR(R.string.shooting_star),
    SNOWFLAKES(R.string.snow),
    CLOUDS(R.string.cloud),
    FLOWERS(R.string.flowers),
    NOTES(R.string.music_notes),
    RAIN(R.string.rain),
    CHRISTMAS(R.string.christmas),
    CHRISTMASBELL(R.string.christmas_bell),
    WREATH(R.string.wreath);
}

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

val sampleImageColor = ImageColor(byteArrayOf(), Color.Red, Color.Blue, Color.Green, ImageColorState.GRADIENT, BackgroundBase.SKY, Effect.SNOWFLAKES, ShaderType.Brush1)

@Composable
fun ImageColorBackground(imageColor: ImageColor, modifier: Modifier = Modifier){
    when(imageColor.state) {
        ImageColorState.IMAGE -> {
            if (imageColor.imageData.isNotEmpty()) {
                val bitmap = remember(
                    imageColor.imageData.take(16),
                    imageColor.imageData.size
                ) {
                    BitmapFactory.decodeByteArray(imageColor.imageData, 0, imageColor.imageData.size).asImageBitmap()
                }
                Image(
                    bitmap = bitmap,
                    contentDescription = stringResource(R.string.selected_image),
                    modifier = modifier,
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Image(
                    painter = ColorPainter(imageColor.color),
                    contentDescription = "Quiz Background",
                    modifier = modifier,
                )
            }
        }
        ImageColorState.GRADIENT -> {
            GradientBrush(imageColor, imageColor.shaderType, modifier,
                thisShape = RoundedCornerShape(0.dp) )
        }
        else -> {
            Image(
                painter = ColorPainter(imageColor.color),
                contentDescription = "Quiz Background",
                modifier = modifier,
            )
        }
    }
}