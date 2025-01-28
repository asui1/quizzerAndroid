package com.asu1.quizzer.composables.scorecard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.asu1.imagecolor.BackgroundBase
import com.asu1.imagecolor.ImageColorState
import com.asu1.quizzer.composables.ImageGetter

@Composable
fun ImagePickerWithBaseImages(
    modifier: Modifier = Modifier,
    onBaseImageSelected: (BackgroundBase) -> Unit = {},
    onImageSelected: (ByteArray) -> Unit = {},
    imageColorState: ImageColorState = ImageColorState.BASEIMAGE,
    currentSelection: BackgroundBase = BackgroundBase.SKY,
    currentImage: ByteArray = byteArrayOf(),
    width: Dp = 200.dp,
    height: Dp = 200.dp,
) {
    Column(
        modifier = modifier
            .size(width = width * 0.7f, height = height * 0.7f)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item(
                key = currentImage.take(100)
            ){
                val isSelected = (imageColorState == ImageColorState.IMAGE)
                ImageGetter(
                    image = currentImage,
                    onImageUpdate = onImageSelected,
                    onImageDelete = {onImageSelected(byteArrayOf())},
                    width = width,
                    height = height,
                    modifier = Modifier.fillMaxSize()
                        .then(if (isSelected) Modifier.border(BorderStroke(4.dp,
                            com.asu1.resources.LightPrimary
                        )) else Modifier)
                )
            }
            itemsIndexed(
                BackgroundBase.entries.toTypedArray(),
                key = { _, item -> item }
            ) { index, item ->
                val isSelected = (imageColorState == ImageColorState.BASEIMAGE && item == currentSelection)
                Image(
                    painter = painterResource(id = item.resourceId),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .padding(4.dp)
                        .aspectRatio(0.6f)
                        .clickable { onBaseImageSelected(item) }
                        .testTag("DesignScoreCardBaseImage$index")
                        .then(if (isSelected) Modifier.border(BorderStroke(4.dp,
                            com.asu1.resources.LightPrimary
                        )) else Modifier)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ImagePickerWithBaseImagesPreview() {
    com.asu1.resources.QuizzerAndroidTheme {
        Dialog(
            onDismissRequest = { },
        ) {
            ImagePickerWithBaseImages()
        }
    }
}