package com.asu1.quizzer.composables

import android.graphics.BitmapFactory
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.asu1.quizzer.R
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.launchPhotoPicker
import loadImageAsByteArray

@Composable
fun ImageGetter(image: ByteArray, onImageUpdate: (ByteArray) -> Unit, onImageDelete: () -> Unit, width: Dp? = null, height: Dp? = null) {

    val context = LocalContext.current
    val photoPickerLauncher = launchPhotoPicker(context, width, height) { byteArray ->
        onImageUpdate(byteArray)
    }

    val localWidth = width ?: 200.dp
    val localHeight = height ?: 200.dp
    val size = min(localWidth, localHeight)

    Box(
        modifier = Modifier
            .width(localWidth)
            .height(localHeight)
            .clickable {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (image.isEmpty()) {
            Icon(
                imageVector = Icons.Default.Photo,
                contentDescription = "Add Image",
                modifier = Modifier.size(size/2)
            )
        } else {
            Image(
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.size).asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = onImageDelete,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = size/10, y = -size/10) // Adjust the offset values as needed
                    .size(size / 5)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveCircle,
                    contentDescription = "Delete Image"
                )
            }
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