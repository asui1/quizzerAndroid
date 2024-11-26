package com.asu1.quizzer.composables.ImageColorColor2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
fun NightWithShootingStar(
    colorMatrix1: ColorMatrix,
    colorMatrix2: ColorMatrix,
    imageWidthPx: Float,
    imageHeightPx: Float,
    width: Dp,
    time: Float
) {
    val widthConst = remember(imageWidthPx){imageWidthPx * 1.5f}
    val xVal = ((time * 100) % widthConst) * 2 - imageWidthPx
    val xVal2 = (((time * 1.2f) * 100) % widthConst) * 2 - imageWidthPx
    val xVal3 = (((time * 1.4f) * 100) % widthConst) * 2 - imageWidthPx
    val yVal = ((time * 100) % widthConst) * 2 - imageHeightPx
    val yVal2 = (((time * 1.2f) * 100) % widthConst) * 2 - imageWidthPx * 1.5f
    val yVal3 = (((time * 1.4f) * 100) % widthConst) * 2 - imageWidthPx /2
    val xPositions = listOf(xVal, xVal2, xVal3)
    val yPositions = listOf(yVal, yVal2, yVal3)
    Image(
        painter = painterResource(id = R.drawable.nightsky2_background),
        colorFilter = ColorFilter.colorMatrix(colorMatrix1),
        contentDescription = stringResource(R.string.background),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
    xPositions.forEachIndexed { index, position ->
        Image(
            painter = painterResource(id = R.drawable.shootingstar),
            colorFilter = ColorFilter.colorMatrix(colorMatrix2),
            contentDescription = stringResource(R.string.background),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .graphicsLayer {
                    translationX = position
                    translationY = yPositions[index]
                }
                .size(width)
        )
    }
}
