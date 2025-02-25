package com.asu1.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.materialkolor.ktx.themeColors

fun ColorScheme.withPrimaryColor(newPrimaryColor: Color): ColorScheme {
    return this.copy(
        primary = newPrimaryColor
    )
}

fun ColorScheme.withSecondaryColor(newSecondaryColor: Color): ColorScheme {
    return this.copy(
        secondary = newSecondaryColor
    )
}

fun ColorScheme.withTertiaryColor(newTertiaryColor: Color): ColorScheme {
    return this.copy(
        tertiary = newTertiaryColor
    )
}

fun calculateSeedColor(bitmap: ImageBitmap): List<Color> {
    val suitableColors = bitmap.themeColors(fallback = Color.Blue)
    return suitableColors.take(3)
}
