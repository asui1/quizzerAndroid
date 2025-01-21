package com.asu1.quizzer.composables.effects

import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.asu1.imagecolor.ImageColor
import com.asu1.resources.ShaderType
import com.asu1.utils.shaders.diagonalHalf
import com.asu1.utils.shaders.horizontalHalf
import com.asu1.utils.shaders.leftBottomDist
import com.asu1.utils.shaders.leftDist
import com.asu1.utils.shaders.topDist
import com.asu1.utils.shaders.verticalRepeat
import com.asu1.utils.shaders.verticalhalf

@Composable
fun GradientBrush(
    imageColor: ImageColor,
    shaderType: ShaderType,
    modifier: Modifier = Modifier,
    thisShape: Shape = RoundedCornerShape(16.dp)
    ){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val shader = remember(shaderType){RuntimeShader(
            when(shaderType){
                ShaderType.Brush1 -> leftBottomDist
                ShaderType.Brush2 -> leftDist
                ShaderType.Brush3 -> topDist
                ShaderType.Brush4 -> diagonalHalf
                ShaderType.Brush5 -> horizontalHalf
                ShaderType.Brush6 -> verticalhalf
                ShaderType.Brush7 -> verticalRepeat
            }
        )}
        Image(
            painter = ColorPainter(Color.Transparent),
            contentDescription = "Background with Gradient",
            modifier = modifier.graphicsLayer {
                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setColorUniform(
                    "color",
                    imageColor.color.toArgb(),
                )
                shader.setColorUniform(
                    "color2",
                    imageColor.colorGradient.toArgb()
                )
                renderEffect = android.graphics.RenderEffect.createShaderEffect(shader).asComposeRenderEffect()
                shape = thisShape
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
                        start = Offset.Zero,
                        end = Offset.Infinite,
                        tileMode = TileMode.Clamp
                    ),
                    shape = thisShape
                )
        )
    }
}