package com.asu1.utils.images

import android.graphics.Bitmap
import com.asu1.utils.Logger
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions

val options = SelfieSegmenterOptions.Builder()
    .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
    .build()
val segmenter = Segmentation.getClient(options)

// TODO : CHECK FOR IMPLEMENTATIONS
fun processImage(bitmap: Bitmap, onResult: (Bitmap) -> Unit) {
    val image = InputImage.fromBitmap(bitmap, 0)
    segmenter.process(image)
        .addOnSuccessListener { mask ->
            // Create a new bitmap with the background removed.
            val resultBitmap = applyMaskToBitmap(bitmap, mask)
            onResult(resultBitmap)
        }
        .addOnFailureListener { e ->
            // Handle any errors here.
            e.printStackTrace()
        }
}
