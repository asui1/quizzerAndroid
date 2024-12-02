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

data class ShootingStar(
    val startHeight: Float,
    val angle: Float,
    val sin: Float,
    val cos: Float,
    val distance: Animatable<Float, AnimationVector1D>
)

@Composable
fun WithShootingStar(
    colorMatrix2: ColorMatrix,
    imageWidthPx: Float,
    imageHeightPx: Float,
    width: Dp,
) {
    val shootingStars = remember { mutableStateListOf<ShootingStar>() }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        while(true){
            val angle = Math.random().toFloat() * 20f + 10f
            val shootingStar = ShootingStar(
                startHeight = imageHeightPx * 0.4f * Math.random().toFloat() - imageHeightPx * 0.1f,
                angle = 90f + angle,
                sin = Math.sin(Math.toRadians(angle.toDouble())).toFloat(),
                cos = Math.cos(Math.toRadians(angle.toDouble())).toFloat(),
                distance = Animatable(-imageWidthPx*0.5f)
            )
            shootingStars.add(shootingStar)
            coroutineScope.launch {
                shootingStar.distance.animateTo(imageWidthPx * 2f, animationSpec = tween(4000, easing = LinearEasing))
                shootingStars.remove(shootingStar)
            }
            delay(1000L)
        }
    }
    shootingStars.forEachIndexed { index, item ->
        val translationX = item.distance.value * item.cos
        val translationY = item.startHeight + item.distance.value * item.sin

        Image(
            painter = painterResource(id = R.drawable.starfall),
            colorFilter = ColorFilter.colorMatrix(colorMatrix2),
            contentDescription = stringResource(R.string.background),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = translationX
                    this.translationY = translationY
                    rotationZ = item.angle
                }
                .size(width / 2)
        )
    }
}
