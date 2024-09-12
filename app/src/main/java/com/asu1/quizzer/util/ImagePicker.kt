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
import java.io.ByteArrayOutputStream

@Composable
fun launchPhotoPicker(
    context: Context,
    onPhotoPicked: (ByteArray) -> Unit
): ActivityResultLauncher<PickVisualMediaRequest> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val byteArray = uriToByteArray(
                    context = context,
                    uri = uri,
                    maxWidth = 400,
                    maxHeight = 400
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

fun uriToByteArray(context: Context, uri: Uri, maxWidth: Int, maxHeight: Int): ByteArray? {
    return context.contentResolver.openInputStream(uri)?.use { inputStream ->
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        val resizedBitmap = resizeBitmap(originalBitmap, maxWidth, maxHeight)
        val buffer = ByteArrayOutputStream()
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            resizedBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 85, buffer)
        }
        else {
            resizedBitmap.compress(Bitmap.CompressFormat.WEBP, 85, buffer)
        }
        buffer.toByteArray()
    }
}

fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val aspectRatio = width.toFloat() / height.toFloat()
    val newWidth: Int
    val newHeight: Int

    if (width > height) {
        newWidth = maxWidth
        newHeight = (maxWidth / aspectRatio).toInt()
    } else {
        newHeight = maxHeight
        newWidth = (maxHeight * aspectRatio).toInt()
    }

    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}
