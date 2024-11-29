package com.asu1.quizzer.composables.effects

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
    var randoms1: List<Float> = List(5) { Random.nextFloat()  * 2f },
    var randoms2: List<Float> = List(5) { Random.nextFloat()  * 2f },
    var randoms3: List<Float> = List(5) { Random.nextFloat() * 2f  },
    var randoms4: List<Float> = List(5) { Random.nextFloat() * 2f  }
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
    val distanceModifiers = remember{ listOf(0.3f, 0.6f, 0.8f, 1f) }
    val explosionCount = remember{ 10 }

    LaunchedEffect(Unit) {
        while (true) {
            val centerX = Random.nextFloat() * imageWidthPx
            val centerY = imageHeightPx * (0.1f + Random.nextFloat() * 0.4f)
            val targetDistance = 100f + Random.nextFloat() * 70f
            val explosionParticles = List(explosionCount) { index ->
                index * (360 / explosionCount).toFloat() + Random.nextFloat() * 20f - 10f
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
        Canvas(modifier = Modifier.fillMaxSize()) {
            explosions.forEach { explosion ->
                explosion.particles.forEach { angle ->
                    distanceModifiers.forEachIndexed { index, distMod ->
                        val randoms = when(index){
                            0 -> explosion.randoms1
                            1 -> explosion.randoms2
                            2 -> explosion.randoms3
                            else -> explosion.randoms4
                        }
                        randoms.forEach{angleMod ->
                            val path = Path().apply {
                                moveTo(explosion.centerX, explosion.centerY)
                                val endX = explosion.centerX + (distMod * explosion.distance.value * cos(angleMod +toRadians(angle.toDouble()))).toFloat()
                                val endY = explosion.centerY - (distMod * explosion.distance.value * sin(angleMod + toRadians(angle.toDouble()))).toFloat()
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