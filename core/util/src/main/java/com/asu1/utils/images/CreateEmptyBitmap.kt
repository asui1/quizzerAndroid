package com.asu1.utils.images

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

fun createEmptyBitmap(): Bitmap {
    return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}

