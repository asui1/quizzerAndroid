// File: app/src/main/java/com/asu1/quizzer/composables/ImagePicker.kt

package com.asu1.utils

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
import androidx.core.graphics.scale
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

fun resizeBitmap(
    bitmap: Bitmap,
    maxWidth: Dp?,
    maxHeight: Dp?
): Bitmap {
    Logger.debug("update with bitmap size=${bitmap.width}x${bitmap.height}")
    Logger.debug("constraints: maxWidth=$maxWidth, maxHeight=$maxHeight")

    val result: Bitmap = if (maxWidth == null || maxHeight == null) {
        // no constraints → leave as is
        bitmap
    } else {
        val width = bitmap.width
        val height = bitmap.height
        val titleSize = 200
        val aspectRatio = maxWidth / maxHeight

        // compute the crop dimensions
        val (newWidth, newHeight) = if (width > height * aspectRatio) {
            ( (height * aspectRatio).toInt() to height )
        } else {
            ( width to (width / aspectRatio).toInt() )
        }

        // center‐crop
        val xOffset = (width - newWidth) / 2
        val yOffset = (height - newHeight) / 2
        val cropped = Bitmap.createBitmap(bitmap, xOffset, yOffset, newWidth, newHeight)

        // then scale; only one branch here
        if (maxWidth == 200.dp) {
            cropped.scale(titleSize, titleSize)
        } else {
            cropped.scale(newWidth, newHeight)
        }
    }
    return result
}
