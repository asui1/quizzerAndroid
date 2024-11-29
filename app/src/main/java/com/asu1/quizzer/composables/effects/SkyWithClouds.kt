package com.asu1.quizzer.composables.effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.asu1.quizzer.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Cloud(
    val startHeight: Float,
    val time: Int,
    val distance: Animatable <Float, AnimationVector1D>
)

@Composable
fun SkyWithClouds(
    colorMatrix: ColorMatrix,
    imageWidthPx: Float,
    width: Dp,
) {
    val clouds = remember{ mutableStateListOf<Cloud>() }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        while(true){
            val cloud = Cloud(
                startHeight = imageWidthPx * 0.7f * Math.random().toFloat() - imageWidthPx * 0.2f,
                time = 4000 + (Math.random() * 3000).toInt(),
                distance = Animatable(-imageWidthPx)
            )
            clouds.add(cloud)
            coroutineScope.launch {
                cloud.distance.animateTo(imageWidthPx * 2f, animationSpec = tween(cloud.time, easing = LinearEasing))
                clouds.remove(cloud)
            }
            delay(1500L)
        }
    }
    clouds.forEachIndexed { index, cloud ->
        Image(
            painter = painterResource(id = R.drawable.single_cloud_nobackground),
            colorFilter = ColorFilter.colorMatrix(colorMatrix),
            contentDescription = stringResource(R.string.background),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .graphicsLayer {
                    translationX = cloud.distance.value
                    translationY = cloud.startHeight
                }
                .size(width)
        )
    }
}
