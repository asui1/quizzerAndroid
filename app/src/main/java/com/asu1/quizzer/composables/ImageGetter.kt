package com.asu1.quizzer.composables

import android.graphics.BitmapFactory
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.quizcards.loadImageAsByteArray
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.launchPhotoPicker

@Composable
fun ImageGetter(image: ByteArray, onImageUpdate: (ByteArray) -> Unit, onImageDelete: () -> Unit, width: Dp? = null, height: Dp? = null,
                modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val photoPickerLauncher = launchPhotoPicker(context, width, height) { byteArray ->
        onImageUpdate(byteArray)
    }

    val localWidth = width ?: 200.dp
    val localHeight = height ?: 200.dp
    val size = min(localWidth, localHeight)

    Box(
        modifier = modifier
            .width(localWidth)
            .height(localHeight)
            .clickable {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            .testTag("ImageGetterBoxGetImage"),
        contentAlignment = Alignment.Center
    ) {
        if (image.isEmpty()) {
            Icon(
                imageVector = Icons.Default.Photo,
                contentDescription = stringResource(R.string.add_image),
                modifier = Modifier.size(size/2)
            )
        } else {
            val bitmap = remember(image.take(4)) {
                BitmapFactory.decodeByteArray(image, 0, image.size).asImageBitmap().apply {
                    prepareToDraw()
                }
            }
            Image(
                bitmap = bitmap,
                contentDescription = stringResource(R.string.selected_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.RemoveCircle,
                contentDescription = stringResource(R.string.delete_image),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable {
                        onImageDelete()
                    }
                    .padding(top = 4.dp, end = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageGetterPreview() {
    val context = LocalContext.current
    val image = loadImageAsByteArray(context, R.drawable.question2)

    QuizzerAndroidTheme {
        ImageGetter(
            image = image,
            onImageUpdate = {},
            onImageDelete = {}
        )
    }

}