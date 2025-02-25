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

fun ColorScheme.withOnPrimaryColor(newOnPrimaryColor: Color): ColorScheme {
    return this.copy(
        onPrimary = newOnPrimaryColor
    )
}

fun ColorScheme.withOnSecondaryColor(newOnSecondaryColor: Color): ColorScheme {
    return this.copy(
        onSecondary = newOnSecondaryColor
    )
}

fun ColorScheme.withOnTertiaryColor(newOnTertiaryColor: Color): ColorScheme {
    return this.copy(
        onTertiary = newOnTertiaryColor
    )
}

fun ColorScheme.withErrorColor(newErrorColor: Color): ColorScheme {
    return this.copy(
        error = newErrorColor
    )
}

fun ColorScheme.withOnErrorColor(newOnErrorColor: Color): ColorScheme {
    return this.copy(
        onError = newOnErrorColor
    )
}

fun ColorScheme.withPrimaryContainerColor(newPrimaryContainerColor: Color): ColorScheme {
    return this.copy(
        primaryContainer = newPrimaryContainerColor
    )
}

fun ColorScheme.withSecondaryContainerColor(newSecondaryContainerColor: Color): ColorScheme {
    return this.copy(
        secondaryContainer = newSecondaryContainerColor
    )
}

fun ColorScheme.withTertiaryContainerColor(newTertiaryContainerColor: Color): ColorScheme {
    return this.copy(
        tertiaryContainer = newTertiaryContainerColor
    )
}

fun ColorScheme.withOnPrimaryContainerColor(newOnPrimaryContainerColor: Color): ColorScheme {
    return this.copy(
        onPrimaryContainer = newOnPrimaryContainerColor
    )
}

fun ColorScheme.withOnSecondaryContainerColor(newOnSecondaryContainerColor: Color): ColorScheme {
    return this.copy(
        onSecondaryContainer = newOnSecondaryContainerColor
    )
}

fun ColorScheme.withOnTertiaryContainerColor(newOnTertiaryContainerColor: Color): ColorScheme {
    return this.copy(
        onTertiaryContainer = newOnTertiaryContainerColor
    )
}

fun ColorScheme.withSurfaceColor(newSurfaceColor: Color): ColorScheme {
    return this.copy(
        surface = newSurfaceColor
    )
}

fun calculateSeedColor(bitmap: ImageBitmap): List<Color> {
    val suitableColors = bitmap.themeColors(fallback = Color.Blue)
    return suitableColors.take(3)
}
