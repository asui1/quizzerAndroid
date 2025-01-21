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
import com.asu1.quizzer.composables.effects.GradientBrush
import com.asu1.resources.R


@Composable
fun ImageColorBackground(imageColor: ImageColor, modifier: Modifier = Modifier){
    when(imageColor.state) {
        ImageColorState.IMAGE -> {
            if (imageColor.imageData.isNotEmpty()) {
                val bitmap = remember(
                    imageColor.imageData.take(16),
                    imageColor.imageData.size
                ) {
                    BitmapFactory.decodeByteArray(imageColor.imageData, 0, imageColor.imageData.size).asImageBitmap()
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
            GradientBrush(imageColor, imageColor.shaderType, modifier,
                thisShape = RoundedCornerShape(0.dp) )
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