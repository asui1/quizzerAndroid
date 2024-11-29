package com.asu1.quizzer.composables.effects

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class Snowflake(
    val x: Float,
    val radius: Float,
    val speed: Float,
    val animation: Animatable<Float, AnimationVector1D>
)

@Composable
fun Snowflake(
    color2: Color,
    imageWidthPx: Float,
    imageHeightPx: Float,
    modifier: Modifier = Modifier
) {
    val snowflakes = remember { mutableStateListOf<Snowflake>() }
    val coroutineScope = rememberCoroutineScope()
    val targetValue = remember(imageHeightPx) { imageHeightPx * 0.95f }

    LaunchedEffect(Unit) {
        while (snowflakes.size < 100) {
            val snowflake = Snowflake(
                x = Random.nextFloat() * imageWidthPx,
                radius = Random.nextFloat() * 4 + 2,
                speed = Random.nextFloat() * 2 + 1,
                animation = Animatable(0f)
            )
            snowflakes.add(snowflake)
            coroutineScope.launch {
                snowflake.animation.animateTo(
                    targetValue = targetValue + snowflake.radius,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = (5000 / snowflake.speed).toInt(), easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
            delay(100L)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            snowflakes.forEach { snowflake ->
                val y = snowflake.animation.value
                drawCircle(
                    color = color2,
                    radius = snowflake.radius,
                    center = Offset(snowflake.x, y)
                )
            }
        }
    }
}