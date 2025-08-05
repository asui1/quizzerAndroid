package com.asu1.customComposable.effects

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import com.asu1.utils.shaders.ShaderType

@Composable
fun GradientBrush(
    color: Color,
    color2: Color,
    shaderType: ShaderType,
    modifier: Modifier = Modifier,
    ){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val shader = remember(shaderType){RuntimeShader(
            shaderType.agslShader
        )}
        val colorState = rememberUpdatedState(color.toArgb())
        val gradientState = rememberUpdatedState(color2.toArgb())
        Image(
            painter = ColorPainter(Color.Transparent),
            contentDescription = "Background with Gradient",
            modifier = modifier.graphicsLayer {
                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setColorUniform(
                    "color",
                    colorState.value
                )
                shader.setColorUniform(
                    "color2",
                    gradientState.value
                )
                renderEffect = RenderEffect.createShaderEffect(shader).asComposeRenderEffect()
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
                        colors = listOf(color, color2),
                        start = Offset.Zero,
                        end = Offset.Infinite,
                        tileMode = TileMode.Clamp
                    ),
                )
        )
    }
}
