package com.asu1.customComposable.effects.LightingEffects

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircularLight(
    modifier: Modifier,
    color: Color,
    alpha: Float, // Now a Float instead of a lambda
) {
    Canvas(modifier = modifier) {
        // Adjust center and radius to get the desired spotlight effect
        val spotlightCenter = Offset(x = size.width / 2, y = size.height / 4)
        val spotlightRadius = size.width / 2

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = alpha),
                    Color.Transparent
                ),
                center = spotlightCenter,
                radius = spotlightRadius
            ),
            center = spotlightCenter,
            radius = spotlightRadius
        )
    }
}

@Preview
@Composable
fun StageCardWithCircleLightPreview() {
    val cardHeight = 300.dp
    val infiniteTransition = rememberInfiniteTransition()
    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
    ) {
        // The card that holds your content
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Place your card content here
        }
        CircularLight(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            color = Color.Red,
            alpha = animatedAlpha  // Directly passing the Float value
        )
    }
}
