package com.asu1.utils.images

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val options = SelfieSegmenterOptions.Builder()
    .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
    .build()
val segmenter = Segmentation.getClient(options)

fun processImage(bitmap: Bitmap, scope: CoroutineScope, onResult: (Bitmap) -> Unit) {
    val image = InputImage.fromBitmap(bitmap, 0)
    segmenter.process(image)
        .addOnSuccessListener { mask ->
            // Create a new bitmap with the background removed.
            scope.launch(Dispatchers.Default) {
                // Optionally use withContext if you need to switch context,
                // although the coroutine is already on Dispatchers.Default.
                val resultBitmap = withContext(Dispatchers.Default) {
                    applyMaskToBitmap(bitmap, mask)
                }
                onResult(resultBitmap)
            }
        }
        .addOnFailureListener { e ->
            // Handle any errors here.
            e.printStackTrace()
        }
}
