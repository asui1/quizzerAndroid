package com.asu1.customComposable.effects.lightingEffects

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Extension function on DrawScope that draws one beam.
// The beam originates at startPoint, extends in direction (baseAngle + angleOffset),
// has a starting width (startWidth) and an ending width (endWidth), and is filled with a gradient.
fun DrawScope.drawConcertBeam(
    startPoint: Offset,
    effectiveEnd: Offset,
    startWidth: Float,
    endWidth: Float,
    color: Color
) {
    // Compute the direction vector from start to end.
    val delta = effectiveEnd - startPoint
    // Normalize (if delta is zero, we fallback to (0, 1)).
    val distance = delta.getDistance().coerceAtLeast(1f)
    val direction = Offset(delta.x / distance, delta.y / distance)
    // Compute a perpendicular vector.
    val perpendicular = Offset(-direction.y, direction.x)
    // Compute the four corners of the trapezoidal beam.
    val p0 = startPoint + perpendicular * (-startWidth / 2f)
    val p1 = startPoint + perpendicular * (startWidth / 2f)
    val p2 = effectiveEnd + perpendicular * (endWidth / 2f)
    val p3 = effectiveEnd + perpendicular * (-endWidth / 2f)

    val path = Path().apply {
        moveTo(p0.x, p0.y)
        lineTo(p1.x, p1.y)
        lineTo(p2.x, p2.y)
        lineTo(p3.x, p3.y)
        close()
    }

    // Apply a linear gradient along the beam’s length.
    drawPath(
        path = path,
        brush = Brush.linearGradient(
            colors = listOf(
                color.copy(alpha = 0.7f),
                color.copy(alpha = 0f)
            ),
            start = startPoint,
            end = effectiveEnd
        )
    )
}

@Composable
fun ConcertLineLightsWithOffsets(
    modifier: Modifier,
    color: Color,
    offsets: List<Offset>,
    endPoint: Offset,
    beamLength: Dp = 150.dp,
    startWidth: Dp = 8.dp,
    endWidthFactor: Float = 2f
) {
    Box(modifier = modifier) {
        val density = LocalDensity.current
        val beamLengthPx = with(density) { beamLength.toPx() }
        val startWidthPx = with(density) { startWidth.toPx() }
        val endWidthPx = startWidthPx * endWidthFactor

        // Shared animation: at 0 the beams end at gatheredEnd; at 1 they end at (endPoint + offset).
        val infiniteTransition = rememberInfiniteTransition()
        val gatherProgress by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            offsets.forEach { offset ->
                // For each beam, the target (spread) endpoint is endPoint + offset.
                val spreadEnd = offset.copy(
                    y = beamLengthPx + offset.y,
                )
                // Interpolate between gatheredEnd and spreadEnd based on gatherProgress.
                val effectiveEnd = lerp(spreadEnd, endPoint, gatherProgress)
                drawConcertBeam(
                    startPoint = offset,
                    effectiveEnd = effectiveEnd,
                    startWidth = startWidthPx,
                    endWidth = endWidthPx,
                    color = color
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConcertLineLightsWithOffsetsPreview() {
    // In this preview, we simulate a stage area.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Example list of offsets to further separate the beams when fully spread.
        // You can tweak these to get the desired spread.
        val beamOffsets = listOf(
            Offset(0f, 0f),
            Offset(100f, 0f),
            Offset(200f, 0f),
            Offset(300f, 0f),
            Offset(400f, 0f),
        )

        val targetEnd = Offset(x = 200f, y = 300f)

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp)
        ) {
            // Underlying stage or content could go here.
        }

        ConcertLineLightsWithOffsets(
            modifier = Modifier.fillMaxSize(),
            color = Color.Cyan,
            offsets = beamOffsets,
            endPoint = targetEnd,
            beamLength = 200.dp,
            startWidth = 8.dp,
            endWidthFactor = 2f
        )
    }
}
