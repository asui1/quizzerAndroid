package com.asu1.customeffects.LightingEffects

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

// Extension function on DrawScope that draws one beam.
// The beam originates at startPoint, extends in direction (baseAngle + angleOffset),
// has a starting width (startWidth) and an ending width (endWidth), and is filled with a gradient.
fun DrawScope.drawConcertBeam(
    startPoint: Offset,
    baseAngle: Float,       // e.g. -90° to have beam going upward
    angleOffset: Float,     // animated offset (in degrees) relative to baseAngle
    beamLength: Float,
    startWidth: Float,
    endWidth: Float,
    color: Color
) {
    // Compute the actual angle (in degrees) and convert to radians.
    val actualAngle = baseAngle + angleOffset
    val radians = Math.toRadians(actualAngle.toDouble())
    // Unit vector in beam direction.
    val direction = Offset(cos(radians).toFloat(), sin(radians).toFloat())
    // Perpendicular vector (rotate direction by 90°).
    val perpendicular = Offset(-direction.y, direction.x)
    // At the fixed starting point, the beam is narrow.
    val p0 = startPoint + perpendicular * (-startWidth / 2)
    val p1 = startPoint + perpendicular * (startWidth / 2)
    // The far end of the beam.
    val endPoint = startPoint + direction * beamLength
    val p2 = endPoint + perpendicular * (endWidth / 2)
    val p3 = endPoint + perpendicular * (-endWidth / 2)

    // Build the trapezoidal beam.
    val path = Path().apply {
        moveTo(p0.x, p0.y)
        lineTo(p1.x, p1.y)
        lineTo(p2.x, p2.y)
        lineTo(p3.x, p3.y)
        close()
    }

    // Fill the beam with a gradient that fades from opaque at the start to transparent at the end.
    drawPath(
        path = path,
        brush = Brush.linearGradient(
            colors = listOf(
                color.copy(alpha = 0.8f),
                color.copy(alpha = 0f)
            ),
            start = startPoint,
            end = endPoint
        )
    )
}

@Composable
fun ConcertLineLightsFixedStart(
    modifier: Modifier,
    color: Color,
    beamLength: Dp = 150.dp,
    startWidth: Dp = 8.dp,
    // The far end's width will be startWidth * endWidthFactor.
    endWidthFactor: Float = 2f,
    beamCount: Int = 3,
    // Angle offset relative to the base angle.
    minAngleOffset: Float = -15f,
    maxAngleOffset: Float = 15f,
    // Base angle: -90° means beams emanate upward.
    baseAngle: Float = -90f
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val beamLengthPx = with(density) { beamLength.toPx() }
        val startWidthPx = with(density) { startWidth.toPx() }
        val endWidthPx = startWidthPx * endWidthFactor

        // The fixed starting point: bottom center of the canvas.
        val startPoint = Offset(constraints.maxWidth / 2f, constraints.maxHeight.toFloat())

        val infiniteTransition = rememberInfiniteTransition()
        val angleOffsetAnim by infiniteTransition.animateFloat(
            initialValue = minAngleOffset,
            targetValue = maxAngleOffset,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw multiple beams.
            for (i in 0 until beamCount) {
                // Draw one beam with the current animated angle.
                drawConcertBeam(
                    startPoint = startPoint,
                    baseAngle = baseAngle,
                    angleOffset = angleOffsetAnim,
                    beamLength = beamLengthPx,
                    startWidth = startWidthPx,
                    endWidth = endWidthPx,
                    color = color
                )
            }
        }
    }
}

@Preview
@Composable
fun StageCardWithLinearLightPreview() {
    val cardHeight = 300.dp
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
        ConcertLineLightsFixedStart(
            modifier = Modifier.fillMaxSize(),
            color = Color.Cyan,
            beamLength = 200.dp,
            startWidth = 8.dp,
            endWidthFactor = 2f,
            beamCount = 3,
            minAngleOffset = -15f,
            maxAngleOffset = 15f,
            baseAngle = -90f
        )
    }
}
