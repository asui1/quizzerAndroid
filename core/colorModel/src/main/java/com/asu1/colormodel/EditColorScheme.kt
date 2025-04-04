package com.asu1.colormodel

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.utils.computeOnColor
import com.asu1.utils.computeOnColorToColor
import com.asu1.utils.tonalPalette
import com.materialkolor.hct.Hct
import com.materialkolor.ktx.from

fun ColorScheme.updatePrimary(color: Color): ColorScheme {
    val hct = Hct.from(color)
    val isLight = hct.tone > 50
    val palette = tonalPalette(color)
    return this.copy(
        primary = color,
        onPrimary = palette[computeOnColor(hct).toInt()]!!,
        primaryContainer = if (isLight) palette[90]!! else palette[30]!!,
        onPrimaryContainer = if (isLight) palette[10]!! else palette[90]!!,
        inversePrimary = if (isLight) palette[80]!! else palette[40]!!
    )
}

fun ColorScheme.updatePrimaryContainer(color: Color): ColorScheme {
    val hct = Hct.from(color)
    val isLight = hct.tone > 50
    val palette = tonalPalette(color)
    return this.copy(
        primary = if (isLight) palette[40]!! else palette[80]!!,
        onPrimary = if (isLight) palette[100]!! else palette[20]!!,
        primaryContainer = color,
        onPrimaryContainer = palette[computeOnColor(hct).toInt()]!!,
        inversePrimary = if (isLight) palette[80]!! else palette[40]!!,
    )
}

fun ColorScheme.updateSecondary(color: Color): ColorScheme {
    val hct = Hct.from(color)
    val isLight = hct.tone > 50
    val palette = tonalPalette(color)
    return this.copy(
        secondary = color,
        onSecondary = palette[computeOnColor(hct).toInt()]!!,
        secondaryContainer = if (isLight) palette[90]!! else palette[30]!!,
        onSecondaryContainer = if (isLight) palette[10]!! else palette[90]!!
    )
}

fun ColorScheme.updateSecondaryContainer(color: Color): ColorScheme {
    val hct = Hct.from(color)
    val isLight = hct.tone > 50
    val palette = tonalPalette(color)
    return this.copy(
        secondary = if (isLight) palette[40]!! else palette[80]!!,
        onSecondary = if (isLight) palette[100]!! else palette[20]!!,
        secondaryContainer = color,
        onSecondaryContainer = palette[computeOnColor(hct).toInt()]!!,
    )
}

fun ColorScheme.updateTertiary(color: Color): ColorScheme {
    val hct = Hct.from(color)
    val isLight = hct.tone > 50
    val palette = tonalPalette(color)
    return this.copy(
        tertiary = color,
        onTertiary = palette[computeOnColor(hct).toInt()]!!,
        tertiaryContainer = if (isLight) palette[90]!! else palette[30]!!,
        onTertiaryContainer = if (isLight) palette[10]!! else palette[90]!!
    )
}

fun ColorScheme.updateTertiaryContainer(color: Color): ColorScheme {
    val hct = Hct.from(color)
    val isLight = hct.tone > 50
    val palette = tonalPalette(color)
    return this.copy(
        tertiary = if (isLight) palette[40]!! else palette[80]!!,
        onTertiary = if (isLight) palette[100]!! else palette[20]!!,
        tertiaryContainer = color,
        onTertiaryContainer = palette[computeOnColor(hct).toInt()]!!,
    )
}

fun ColorScheme.updateError(color: Color): ColorScheme {
    val hct = Hct.from(color)
    val isLight = hct.tone > 50
    val palette = tonalPalette(color)
    return this.copy(
        error = color,
        onError = palette[computeOnColor(hct).toInt()]!!,
        errorContainer = if (isLight) palette[90]!! else palette[30]!!,
        onErrorContainer = if (isLight) palette[10]!! else palette[90]!!
    )
}

fun ColorScheme.updateErrorContainer(color: Color): ColorScheme {
    val hct = Hct.from(color)
    val isLight = hct.tone > 50
    val palette = tonalPalette(color)
    return this.copy(
        error = if (isLight) palette[40]!! else palette[80]!!,
        onError = if (isLight) palette[100]!! else palette[20]!!,
        errorContainer = color,
        onErrorContainer = palette[computeOnColor(hct).toInt()]!!,
    )
}

fun ColorScheme.updateSurfaceGroup(color: Color): ColorScheme {
    val hct = Hct.from(color)
    val isLight = hct.tone > 50
    val palette = tonalPalette(color)
    return this.copy(
        background = if (isLight) palette[99]!! else palette[10]!!,
        onBackground = if (isLight) palette[10]!! else palette[90]!!,
        surface = color,
        onSurface = palette[computeOnColor(hct).toInt()]!!,
        surfaceVariant = if (isLight) palette[90]!! else palette[30]!!,
        onSurfaceVariant = if (isLight) palette[30]!! else palette[80]!!,
        scrim = palette[0]!!,
        surfaceBright = if (isLight) palette[98]!! else palette[24]!!,
        surfaceDim = if (isLight) palette[87]!! else palette[6]!!,
        surfaceContainer = if (isLight) palette[94]!! else palette[12]!!,
        surfaceContainerHigh = if (isLight) palette[92]!! else palette[17]!!,
        surfaceContainerHighest = if (isLight) palette[90]!! else palette[24]!!,
        surfaceContainerLow = if (isLight) palette[96]!! else palette[10]!!,
        surfaceContainerLowest = if (isLight) palette[100]!! else palette[4]!!,
    )
}

fun ColorScheme.updateOutline(color: Color): ColorScheme {
    return this.copy(
        outline = color,
        outlineVariant = computeOnColorToColor(color),
    )
}

