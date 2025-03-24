package com.asu1.utils.images

import android.graphics.Bitmap
import android.graphics.Color
import com.google.mlkit.vision.segmentation.SegmentationMask
import androidx.core.graphics.set
import androidx.core.graphics.get
import androidx.core.graphics.createBitmap

fun applyMaskToBitmap(original: Bitmap, mask: SegmentationMask): Bitmap {
    val width = original.width
    val height = original.height
    val output = createBitmap(width, height)

    // The mask’s buffer may be smaller than the original image.
    // You might need to scale it or map its values appropriately.
    val maskBuffer = mask.buffer
    maskBuffer.rewind()
    // Assume the mask’s dimensions match the original for this example.
    val maskPixels = FloatArray(width * height)
    maskBuffer.asFloatBuffer().get(maskPixels)

    // Iterate over each pixel and apply the mask.
    for (y in 0 until height) {
        for (x in 0 until width) {
            val index = y * width + x
            // The mask value is in the range [0, 1].
            val maskValue = maskPixels[index]
            if (maskValue < 0.5f) {
                // Pixel is likely background—make it transparent.
                output[x, y] = Color.TRANSPARENT
            } else {
                // Pixel is part of the foreground—copy it.
                output[x, y] = original[x, y]
            }
        }
    }
    return output
}
