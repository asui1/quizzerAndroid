package com.asu1.quizzer.model

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.customeffects.GradientBrush
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
            com.asu1.customeffects.GradientBrush(
                imageColor, imageColor.shaderType, modifier,
                thisShape = RoundedCornerShape(0.dp)
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