package com.asu1.quizzer.model

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter

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
}