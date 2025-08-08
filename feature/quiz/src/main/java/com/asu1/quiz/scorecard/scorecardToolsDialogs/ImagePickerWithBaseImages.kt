package com.asu1.quiz.scorecard.scorecardToolsDialogs

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.asu1.customComposable.imageGetter.ImageGetter
import com.asu1.imagecolor.BackgroundBase
import com.asu1.imagecolor.ImageColorState
import com.asu1.resources.LightPrimary
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import com.asu1.utils.images.createEmptyBitmap

@Composable
fun ImagePickerWithBaseImages(
    modifier: Modifier = Modifier,
    onBaseImageSelected: (BackgroundBase) -> Unit = {},
    onImageSelected: (Bitmap?) -> Unit = {},
    imageColorState: ImageColorState = ImageColorState.BASEIMAGE,
    currentSelection: BackgroundBase = BackgroundBase.SKY_WARM,
    currentImage: Bitmap,
    width: Dp = 200.dp,
    height: Dp = 200.dp,
) {
    val cardShape = RoundedCornerShape(16.dp)
    val boxModifier = modifier
        .size(width = width * 0.7f, height = height * 0.7f)
        .background(MaterialTheme.colorScheme.surface, shape = cardShape)
        .border(2.dp, MaterialTheme.colorScheme.outline, shape = cardShape)
        .padding(16.dp)

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = boxModifier) {
        Text(stringResource(R.string.background),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        BaseImageGrid(
            imageColorState = imageColorState,
            currentSelection = currentSelection,
            currentImage = currentImage,
            width = width,
            onPickBase = onBaseImageSelected,
            onPickImage = onImageSelected,
        )
    }
}

/* ---------- Pieces ---------- */

@Composable
private fun BaseImageGrid(
    imageColorState: ImageColorState,
    currentSelection: BackgroundBase,
    currentImage: Bitmap,
    width: Dp,
    onPickBase: (BackgroundBase) -> Unit,
    onPickImage: (Bitmap?) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("BaseImagePickerLazyVerticalGrid")
    ) {
        // Current custom image
        item(key = currentImage.hashCode()) { // prefer stable id if you have one
            CurrentImageItem(
                bitmap = currentImage,
                selected = imageColorState == ImageColorState.IMAGE,
                width = width,
                onPick = onPickImage
            )
        }

        // Built-in base images
        items(
            items = BackgroundBase.entries,
            key = { it } // enums are stable keys
        ) { base ->
            BaseImageItem(
                base = base,
                selected = imageColorState == ImageColorState.BASEIMAGE && base == currentSelection,
                onPick = { onPickBase(base) }
            )
        }
    }
}

@Composable
private fun CurrentImageItem(
    bitmap: Bitmap,
    selected: Boolean,
    width: Dp,
    onPick: (Bitmap?) -> Unit,
) {
    val borderMod = if (selected) {
        Modifier.border(BorderStroke(4.dp, LightPrimary))
    } else Modifier

    ImageGetter(
        image = bitmap,
        onImageUpdate = onPick,
        modifier = Modifier
            .aspectRatio(0.6f)
            .then(borderMod),
        width = width * 0.18f
    )
}

@Composable
private fun BaseImageItem(
    base: BackgroundBase,
    selected: Boolean,
    onPick: () -> Unit,
) {
    val shape = RoundedCornerShape(4.dp)
    val borderMod = if (selected) {
        Modifier.border(BorderStroke(4.dp, LightPrimary), shape)
    } else Modifier

    Image(
        painter = painterResource(id = base.resourceId),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(0.6f)
            .clip(shape)
            .then(borderMod)
            .clickable(onClick = onPick)
            .testTag("DesignScoreCardBaseImage_${base.ordinal}")
    )
}

@Preview(showBackground = true)
@Composable
fun ImagePickerWithBaseImagesPreview() {
    QuizzerAndroidTheme {
        Dialog(
            onDismissRequest = { },
        ) {
            ImagePickerWithBaseImages(
                currentImage = createEmptyBitmap(),
                onBaseImageSelected = {},
                onImageSelected = {},
                imageColorState = ImageColorState.BASEIMAGE,
                currentSelection = BackgroundBase.SKY_WARM,
                width = 200.dp,
                height = 200.dp
            )
        }
    }
}