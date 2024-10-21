package com.asu1.quizzer.model

import android.graphics.BitmapFactory
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import com.asu1.quizzer.data.ColorSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

//State 0 -> Color, State 1 -> Color1 + Color2, State 2 -> Image
@Serializable
enum class ImageColorState {
    COLOR, COLOR2, IMAGE
}

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
    fun asBackgroundModifier(): Modifier {
        return when(state) {
            ImageColorState.COLOR -> Modifier.background(color)
            ImageColorState.COLOR2 -> Modifier.background(color2)
            ImageColorState.IMAGE -> if (imageData.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                Modifier.paint(BitmapPainter(bitmap.asImageBitmap()))
            } else {
                Modifier.background(color)
            }
        }
    }

    fun asBackgroundModifierForScoreCard(shaderOption: ShaderType, time: Float): Modifier {
        return when(state) {
            ImageColorState.COLOR -> Modifier.background(color)
            ImageColorState.COLOR2 -> {
                if (Build.VERSION_CODES.TIRAMISU > Build.VERSION.SDK_INT) {
                    Modifier.background(
                        brush = Brush.linearGradient(
                            colors = listOf(color, color2),
                            start = Offset(0f, 0f),
                            end = Offset(100f, 100f),
                            tileMode = TileMode.Clamp
                        )
                    )
                } else {
                    val shader = RuntimeShader(
                        shaderOption.getShader(),
                    )
                    Modifier.graphicsLayer {
                        shader.setFloatUniform("resolution", size.width, size.height)
                        if(shaderOption != ShaderType.Brush1) {
                            shader.setFloatUniform("time", time)
                        }
                        shader.setColorUniform(
                            "color",
                            color.toArgb(),
                        )
                        shader.setColorUniform(
                            "color2",
                            color2.toArgb()
                        )
                        renderEffect = RenderEffect.createShaderEffect(shader).asComposeRenderEffect()
                    }
                }
            }
            ImageColorState.IMAGE -> if (imageData.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                Modifier.paint(BitmapPainter(bitmap.asImageBitmap()),
                    contentScale = ContentScale.FillBounds
                    )
            } else {
                Modifier.background(color)
            }
        }
    }
    fun getAsJson(): Json {
        return Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }
}