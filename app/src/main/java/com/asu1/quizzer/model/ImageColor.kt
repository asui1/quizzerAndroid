package com.asu1.quizzer.model

import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.data.ColorSerializer
import kotlinx.serialization.Serializable

//State 0 -> Color, State 1 -> Color1 + Color2, State 2 -> Image
@Serializable
enum class ImageColorState {
    COLOR, COLOR2, IMAGE
}

// 1. Simple Gradient.
// 2. Moving tileable images : Cloud, Space(star), Space(moon)
// 3. Graphics with AGSL needs : Snow, Fireworks, maybe spotlight

@Serializable
data class ImageColor(
    @Serializable(with = ColorSerializer::class) val color: Color,
    val imageData: ByteArray,
    @Serializable(with = ColorSerializer::class) val color2: Color,
    val state: ImageColorState,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageColor) return false

        if (color != other.color) return false
        if (!imageData.contentEquals(other.imageData)) return false
        if (color2 != other.color2) return false
        if (state != other.state) return false

        return true
    }

    fun getAsImage(): Painter {
        return when(state) {
            ImageColorState.COLOR -> ColorPainter(color)
            ImageColorState.COLOR2 -> ColorPainter(color2)
            ImageColorState.IMAGE -> if (imageData.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                BitmapPainter(bitmap.asImageBitmap())
            } else {
                ColorPainter(color)
            }
        }
    }

    override fun hashCode(): Int {
        var result = color.hashCode()
        result = 31 * result + imageData.contentHashCode()
        result = 31 * result + color2.hashCode()
        result = 31 * result + state.hashCode()
        return result
    }
}

fun Modifier.asBackgroundModifier(imageColor: ImageColor): Modifier {
    return when(imageColor.state) {
        ImageColorState.COLOR -> this.background(imageColor.color)
        ImageColorState.COLOR2 -> this.background(imageColor.color2)
        ImageColorState.IMAGE -> if (imageColor.imageData.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(imageColor.imageData, 0, imageColor.imageData.size)
            this.paint(BitmapPainter(bitmap.asImageBitmap()))
        } else {
            this.background(imageColor.color)
        }
    }
}

fun Modifier.asBackgroundModifierForScoreCard(imageColor: ImageColor, shaderOption: ShaderType, time: Float): Modifier {
    return when(imageColor.state) {
        ImageColorState.COLOR -> this.background(imageColor.color, shape = RoundedCornerShape(16.dp))
        ImageColorState.COLOR2 -> {
            if (Build.VERSION_CODES.TIRAMISU > Build.VERSION.SDK_INT) {
                this.background(
                    brush = Brush.linearGradient(
                        colors = listOf(imageColor.color, imageColor.color2),
                        start = Offset(0f, 0f),
                        end = Offset(100f, 100f),
                        tileMode = TileMode.Clamp
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            } else {
                this
            }
        }
        ImageColorState.IMAGE -> if (imageColor.imageData.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(imageColor.imageData, 0, imageColor.imageData.size)
            this.paint(BitmapPainter(bitmap.asImageBitmap()),
                contentScale = ContentScale.FillBounds,
            )
        } else {
            this.background(imageColor.color,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}
