package com.asu1.quizzer.composables.effects

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
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
    val particles: List<Float>,
)

@Composable
fun Fireworks(
    color2: Color,
    imageWidthPx: Float,
    imageHeightPx: Float,
    modifier: Modifier = Modifier
) {
    val explosions = remember { mutableStateListOf<Explosion>() }
    val coroutineScope = rememberCoroutineScope()
    val distanceModifiers = listOf(0.7f, 1f)
    val explosionCount = 24
    val angle = 360f / explosionCount

    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            while (true) {
                if (explosions.size > 5) {
                    delay(1000L)
                    continue
                }
                val centerX = Random.nextFloat() * imageWidthPx
                val centerY = imageHeightPx * Random.nextFloat() * 0.25f
                val targetDistance = 150f + Random.nextFloat() * 70f
                val explosionParticles = List(explosionCount) { index ->
                    index * angle
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
                    explosion.distance.animateTo(
                        targetValue = targetDistance,
                        animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
                    )
                    explosion.alpha.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing)
                    )
                    delay(4000L)
                    explosions.remove(explosion)
                }
                delay(750L)
            }
        }
        onDispose {
            job.cancel()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            explosions.forEach { explosion ->
                explosion.particles.forEach { angle ->
                    distanceModifiers.forEachIndexed { index, distMod ->
                        val angleMod = if (index == 0) angle + 7.5f else angle
                        val endX = explosion.centerX + (distMod * explosion.distance.value * cos(toRadians(angleMod.toDouble()))).toFloat()
                        val endY = explosion.centerY - (distMod * explosion.distance.value * sin(toRadians(angleMod.toDouble()))).toFloat()
                        val path = Path().apply {
                            moveTo(explosion.centerX, explosion.centerY)
                            lineTo(endX, endY)
                        }
                        val brush = Brush.linearGradient(
                            colors = listOf(Color.Transparent, color2),
                            start = Offset(explosion.centerX, explosion.centerY),
                            end = Offset(endX, endY)
                        )
                        drawPath(
                            path = path,
                            brush = brush,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.cornerPathEffect(10f)
                            )
                        )
                    }
                }
            }
        }
    }
}