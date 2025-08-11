package com.asu1.quiz.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.asu1.customComposable.effects.GradientBrush
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.resources.R


@Composable
fun ImageColorBackground(imageColor: ImageColor, modifier: Modifier = Modifier){
    when(imageColor.state) {
        ImageColorState.IMAGE -> {
            if (imageColor.imageData.width > 1) {
                val bitmap = remember(
                    imageColor.imageData
                ) {
                    imageColor.imageData.asImageBitmap().apply {
                        prepareToDraw()
                    }
                }
                Image(
                    bitmap = bitmap,
                    contentDescription = stringResource(R.string.selected_image),
                    modifier = modifier,
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Image(
                    painter = ColorPainter(imageColor.color),
                    contentDescription = "Quiz Background",
                    modifier = modifier,
                )
            }
        }
        ImageColorState.GRADIENT -> {
            GradientBrush(
                color = imageColor.color,
                color2 = imageColor.colorGradient,
                imageColor.shaderType,
                modifier,
            )
        }
        else -> {
            Image(
                painter = ColorPainter(imageColor.color),
                contentDescription = "Quiz Background",
                modifier = modifier,
            )
        }
    }
}
