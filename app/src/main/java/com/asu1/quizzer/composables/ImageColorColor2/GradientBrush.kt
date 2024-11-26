package com.asu1.quizzer.composables.ImageColorColor2

import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.util.basicShader

@Composable
fun GradientBrush(
    imageColor: ImageColor,
    modifier: Modifier = Modifier,
){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val shader = RuntimeShader(basicShader)
        Image(
            painter = ColorPainter(Color.Transparent),
            contentDescription = "ScoreCard Background",
            modifier = modifier.graphicsLayer {
                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setColorUniform(
                    "color",
                    imageColor.color.toArgb(),
                )
                shader.setColorUniform(
                    "color2",
                    imageColor.color2.toArgb()
                )
                renderEffect = android.graphics.RenderEffect.createShaderEffect(shader).asComposeRenderEffect()
                shape = RoundedCornerShape(16.dp)
                clip = true
            }
        )
    }else{
        Image(
            painter = ColorPainter(Color.Transparent),
            contentDescription = "ScoreCard Background",
            modifier = modifier.fillMaxSize()
                .background(
                brush = Brush.linearGradient(
                    colors = listOf(imageColor.color, imageColor.color2),
                    start = Offset(0f, 100f),
                    end = Offset(100f, 0f),
                    tileMode = TileMode.Clamp
                ),
                shape = RoundedCornerShape(16.dp)
            )
        )
    }
}