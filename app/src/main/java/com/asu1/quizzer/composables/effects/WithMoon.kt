package com.asu1.quizzer.composables.effects

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.asu1.quizzer.R

@Composable
fun WithMoon(
    colorMatrix2: ColorMatrix,
    imageHeightPx: Float,
    width: Dp,
) {
    val time by produceState(0f) {
        while(true){
            withInfiniteAnimationFrameMillis {
                value = it/1000f
            }
        }
    }
    val radius = remember(imageHeightPx){imageHeightPx/2}
    val angle = ((time * 0.1f) % 1f - 0.5f) * Math.PI - 1.57f
    val xVal = radius * Math.cos(angle).toFloat()
    val yVal = radius * Math.sin(angle).toFloat() + imageHeightPx / 2
    Image(
        painter = painterResource(id = R.drawable.moon_nobackground),
        colorFilter = ColorFilter.colorMatrix(colorMatrix2),
        contentDescription = stringResource(R.string.background),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .graphicsLayer {
                translationX = xVal
                translationY = yVal
            }
            .size(width * 0.8f)
    )
}
