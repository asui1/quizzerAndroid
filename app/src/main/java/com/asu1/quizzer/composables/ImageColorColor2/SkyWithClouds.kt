package com.asu1.quizzer.composables.ImageColorColor2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.asu1.quizzer.R

@Composable
fun SkyWithClouds(
    colorMatrix1: ColorMatrix,
    colorMatrix2: ColorMatrix,
    imageWidthPx: Float,
    width: Dp,
    time: Float
) {
    val xVal1 = ((time * 100) % imageWidthPx) * 2 - imageWidthPx
    val xVal2 = (((time * 1.1f) * 100) % imageWidthPx) * 2 - imageWidthPx
    val xVal3 = (((time * 1.2f) * 100) % imageWidthPx) * 2 - imageWidthPx
    val xVal4 = (((time * 1.4f) * 100) % imageWidthPx) * 2 - imageWidthPx
    val xVal5 = (((time * 1.6f) * 100) % imageWidthPx) * 2 - imageWidthPx
    val imagePositions = listOf(xVal1, xVal2, xVal3, xVal4, xVal5)
    Image(
        painter = painterResource(id = R.drawable.sky_background),
        colorFilter = ColorFilter.colorMatrix(colorMatrix1),
        contentDescription = stringResource(R.string.background),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
    imagePositions.forEachIndexed { index, position ->
        Image(
            painter = painterResource(id = R.drawable.single_cloud_nobackground),
            colorFilter = ColorFilter.colorMatrix(colorMatrix2),
            contentDescription = stringResource(R.string.background),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .graphicsLayer {
                    translationX = position
                    translationY = when (index) {
                        0 -> 0f
                        1 -> imageWidthPx / 4
                        2 -> imageWidthPx * 0.75f
                        3 -> imageWidthPx
                        else -> imageWidthPx / 2
                    }

                }
                .size(width)
        )
    }
}
