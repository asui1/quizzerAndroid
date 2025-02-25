// File: app/src/main/java/com/asu1/quizzer/composables/ImagePicker.kt

package com.asu1.quizzer.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun launchPhotoPicker(
    onPhotoPicked: (Uri) -> Unit,
): ActivityResultLauncher<PickVisualMediaRequest> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onPhotoPicked(uri)
            }
        }
    )
}

suspend fun uriToByteArray(context: Context, uri: Uri, maxWidth: Dp?, maxHeight: Dp?): Bitmap? {
    return withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val resizedBitmap = resizeBitmap(originalBitmap, maxWidth, maxHeight)
            resizedBitmap
        }
    }
}

fun resizeBitmap(bitmap: Bitmap, maxWidth: Dp?, maxHeight: Dp?): Bitmap {
    if(maxWidth == null || maxHeight == null) {
        return bitmap
    }
    val width = bitmap.width
    val height = bitmap.height
    val newWidth: Int
    val newHeight: Int

    val titleSize = 200
    val aspectRatio = maxWidth / maxHeight

    if (width > height * aspectRatio) {
        newWidth = (height * aspectRatio).toInt()
        newHeight = height
    } else {
        newWidth = width
        newHeight = (width / aspectRatio).toInt()
    }

    val xOffset = (width - newWidth) / 2
    val yOffset = (height - newHeight) / 2
    val changedBitmap = Bitmap.createBitmap(bitmap, xOffset, yOffset, newWidth, newHeight)
    if(maxWidth == 200.dp) {
        return Bitmap.createScaledBitmap(changedBitmap, titleSize, titleSize, true)
    }
    else{
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
