package com.asu1.quizzer.model

import android.graphics.BitmapFactory
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
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

//State 0 -> Color, State 1 -> Color1 + Color2, State 2 -> Image
enum class ImageColorState {
    COLOR, COLOR2, IMAGE
}


data class ImageColor(
    val color: Color,
    val image: ByteArray,
    val color2: Color,
    val state: ImageColorState,
) {
    fun getAsImage(): Painter {
        return when(state) {
            ImageColorState.COLOR -> ColorPainter(color)
            ImageColorState.COLOR2 -> ColorPainter(color2)
            ImageColorState.IMAGE -> if (image.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
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
            ImageColorState.IMAGE -> if (image.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                Modifier.paint(BitmapPainter(bitmap.asImageBitmap()))
            } else {
                Modifier.background(color)
            }
        }
    }

    fun asBackgroundModifierForScoreCard(shaderOption: ShaderBrush, time: Float): Modifier {
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
                        shader.setFloatUniform("time", time)
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
            ImageColorState.IMAGE -> if (image.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                Modifier.paint(BitmapPainter(bitmap.asImageBitmap()))
            } else {
                Modifier.background(color)
            }
        }
    }
}