// File: app/src/main/java/com/asu1/quizzer/composables/ImagePicker.kt

package com.asu1.quizzer.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream

@Composable
fun launchPhotoPicker(
    context: Context,
    width: Dp? = null,
    height: Dp? = null,
    onPhotoPicked: (ByteArray) -> Unit,
): ActivityResultLauncher<PickVisualMediaRequest> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val byteArray = uriToByteArray(
                    context = context,
                    uri = uri,
                    maxWidth = width,
                    maxHeight = height,
                )
                if (byteArray != null) {
                    onPhotoPicked(byteArray)
                }
            }
        }
    )
}

fun loadImageFromDrawable(context: Context, drawableId: Int): ImageBitmap {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
    return bitmap.asImageBitmap()
}

fun uriToByteArray(context: Context, uri: Uri, maxWidth: Dp?, maxHeight: Dp?): ByteArray? {
    return context.contentResolver.openInputStream(uri)?.use { inputStream ->
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        val resizedBitmap = resizeBitmap(originalBitmap, maxWidth, maxHeight)
        val buffer = ByteArrayOutputStream()
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            resizedBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, buffer)
        }
        else {
            resizedBitmap.compress(Bitmap.CompressFormat.WEBP, 100, buffer)
        }
        buffer.toByteArray()
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
