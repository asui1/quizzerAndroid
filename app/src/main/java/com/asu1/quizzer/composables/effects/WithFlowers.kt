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

enum class FlowerImage(val resourceId: Int){
    Flower1(R.drawable.flower1),
    Flower2(R.drawable.flower2),
    Flower3(R.drawable.flower3),
}

data class Flower(
    val startHeight: Float,
    val time: Int,
    val image: FlowerImage,
    val distance: Animatable<Float, AnimationVector1D>
)

@Composable
fun WithFlowers(
    colorMatrix: ColorMatrix,
    imageWidthPx: Float,
    imageHeightPx: Float,
    width: Dp,
) {
    val flowers = remember{ mutableStateListOf<Flower>() }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        while(true){
            val flower = Flower(
                startHeight = imageHeightPx * 0.7f * Math.random().toFloat(),
                time = 6000 + (Math.random() * 3000).toInt(),
                image = FlowerImage.values().random(),
                distance = Animatable(-imageWidthPx),
            )
            flowers.add(flower)
            coroutineScope.launch {
                flower.distance.animateTo(imageWidthPx * 2f, animationSpec = tween(flower.time, easing = LinearEasing))
                flowers.remove(flower)
            }
            delay(1500L)
        }
    }
    flowers.forEachIndexed { index, flower ->
        val moveX = flower.distance.value
        val rotationAngle = (moveX / imageWidthPx) * 360f
        Image(
            painter = painterResource(id = flower.image.resourceId),
            colorFilter = ColorFilter.colorMatrix(colorMatrix),
            contentDescription = stringResource(R.string.scorecard_effect),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .graphicsLayer {
                    translationX = moveX
                    translationY = flower.startHeight
                    rotationZ = rotationAngle
                }
                .size(width)
        )
    }
}
