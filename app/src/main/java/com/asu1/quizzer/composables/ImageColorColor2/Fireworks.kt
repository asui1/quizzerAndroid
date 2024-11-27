package com.asu1.quizzer.composables.ImageColorColor2

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.materialcore.toRadians
import com.asu1.quizzer.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Explosion(
    val distance: Animatable<Float, AnimationVector1D>,
    val alpha: Animatable<Float, AnimationVector1D>,
    val centerX: Float,
    val centerY: Float,
    val particles: List<Float>
)

@Composable
fun Fireworks(
    colorMatrix1: ColorMatrix,
    color2: Color,
    imageWidthPx: Float,
    imageHeightPx: Float,
    width: Dp,
    time: Float,
    modifier: Modifier = Modifier
) {
    val explosions = remember { mutableStateListOf<Explosion>() }
    val coroutineScope = rememberCoroutineScope()
    val distanceModifiers = remember{ listOf(0.2f, 0.5f, 0.7f, 0.9f, 1f) }
    val angleModifiers = remember{ listOf(0.8f, 1f, 1.2f) }
    val explosionCount = remember{ 10 }

    LaunchedEffect(Unit) {
        while (true) {
            val centerX = Random.nextFloat() * imageWidthPx
            val centerY = imageHeightPx * (0.1f + Random.nextFloat() * 0.4f)
            val targetDistance = 100f + Random.nextFloat() * 70f
            val explosionParticles = List(explosionCount) { index ->
                index * (360 / explosionCount).toFloat() + Random.nextFloat() * 10f - 5f
            }
            val explosion = Explosion(
                distance = Animatable(0f),
                alpha = Animatable(1f),
                centerX = centerX,
                centerY = centerY,
                particles = explosionParticles
            )
            explosions.add(explosion)
            coroutineScope.launch {
                launch {
                    explosion.distance.animateTo(
                        targetValue = targetDistance,
                        animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
                    )
                }
                explosion.alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
                )
                explosions.remove(explosion)
            }
            delay(1000L)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.empty_city_sky),
            colorFilter = ColorFilter.colorMatrix(colorMatrix1),
            contentDescription = stringResource(R.string.background),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            explosions.forEach { explosion ->
                explosion.particles.forEach { angle ->
                    distanceModifiers.forEach{ distMod ->
                        angleModifiers.forEach{angleMod ->
                            val path = Path().apply {
                                moveTo(explosion.centerX, explosion.centerY)
                                val endX = explosion.centerX + (distMod * explosion.distance.value * cos(angleMod * toRadians(angle.toDouble()))).toFloat()
                                val endY = explosion.centerY - (distMod * explosion.distance.value * sin(angleMod * toRadians(angle.toDouble()))).toFloat()
                                quadraticTo(
                                    explosion.centerX, explosion.centerY - 50, // Control point
                                    endX, endY // End point
                                )
                            }
                            drawPath(
                                path = path,
                                color = color2.copy(alpha = explosion.alpha.value),
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                            )
                        }
                    }
                }
            }
        }
    }
}