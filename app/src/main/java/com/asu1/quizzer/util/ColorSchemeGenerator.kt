package com.asu1.quizzer.util

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.materialkolor.hct.Hct
import com.materialkolor.ktx.from
import com.materialkolor.palettes.TonalPalette

//USED BODY FROM materialkolor
fun tonalPalette(color: Color): Map<Int, Color> {
    val hct = Hct.from(color)
    val palette = TonalPalette.fromHct(hct)
    val colors = mutableMapOf<Int, Color>()

    for (i in 0 ..  100) {
        colors[i] = Color(palette.tone(i))
    }

    return colors
}

fun toScheme(isLight: Boolean = true, primary: Color, secondary: Color, tertiary: Color): ColorScheme {
    val primaryPalette = tonalPalette(primary)
    val secondaryPalette = tonalPalette(secondary)
    val tertiaryPalette = tonalPalette(tertiary)
    val errorPalette = tonalPalette(Color.Red)
    val neutralPalette = tonalPalette(Color.Gray)

    return ColorScheme(
        primary = if (isLight) primaryPalette[40]!! else primaryPalette[80]!!,
        onPrimary = if (isLight) primaryPalette[100]!! else primaryPalette[20]!!,
        primaryContainer = if (isLight) primaryPalette[90]!! else primaryPalette[30]!!,
        onPrimaryContainer = if (isLight) primaryPalette[10]!! else primaryPalette[90]!!,
        inversePrimary = if (isLight) primaryPalette[80]!! else primaryPalette[40]!!,
        secondary = if (isLight) secondaryPalette[40]!! else secondaryPalette[80]!!,
        onSecondary = if (isLight) secondaryPalette[100]!! else secondaryPalette[20]!!,
        secondaryContainer = if (isLight) secondaryPalette[90]!! else secondaryPalette[30]!!,
        onSecondaryContainer = if (isLight) secondaryPalette[10]!! else secondaryPalette[90]!!,
        tertiary = if (isLight) tertiaryPalette[40]!! else tertiaryPalette[80]!!,
        onTertiary = if (isLight) tertiaryPalette[100]!! else tertiaryPalette[20]!!,
        tertiaryContainer = if (isLight) tertiaryPalette[90]!! else tertiaryPalette[30]!!,
        onTertiaryContainer = if (isLight) tertiaryPalette[10]!! else tertiaryPalette[90]!!,
        background = if (isLight) neutralPalette[99]!! else neutralPalette[10]!!,
        onBackground = if (isLight) neutralPalette[10]!! else neutralPalette[90]!!,
        surface = if (isLight) neutralPalette[99]!! else neutralPalette[10]!!,
        onSurface = if (isLight) neutralPalette[10]!! else neutralPalette[90]!!,
        surfaceVariant = if (isLight) neutralPalette[90]!! else neutralPalette[30]!!,
        onSurfaceVariant = if (isLight) neutralPalette[30]!! else neutralPalette[80]!!,
        surfaceTint = if (isLight) primaryPalette[40]!! else primaryPalette[80]!!,
        inverseSurface = if (isLight) neutralPalette[20]!! else neutralPalette[90]!!,
        inverseOnSurface = if (isLight) neutralPalette[95]!! else neutralPalette[20]!!,
        error = if (isLight) errorPalette[40]!! else errorPalette[80]!!,
        onError = if (isLight) errorPalette[100]!! else errorPalette[20]!!,
        errorContainer = if (isLight) errorPalette[90]!! else errorPalette[30]!!,
        onErrorContainer = if (isLight) errorPalette[10]!! else errorPalette[90]!!,
        outline = if (isLight) neutralPalette[50]!! else neutralPalette[60]!!,
        outlineVariant = if (isLight) neutralPalette[80]!! else neutralPalette[30]!!,
        scrim = neutralPalette[0]!!,
        surfaceBright = if (isLight) neutralPalette[98]!! else neutralPalette[24]!!,
        surfaceDim = if (isLight) neutralPalette[87]!! else neutralPalette[6]!!,
        surfaceContainer = if (isLight) neutralPalette[94]!! else neutralPalette[12]!!,
        surfaceContainerHigh = if (isLight) neutralPalette[92]!! else neutralPalette[17]!!,
        surfaceContainerHighest = if (isLight) neutralPalette[90]!! else neutralPalette[24]!!,
        surfaceContainerLow = if (isLight) neutralPalette[96]!! else neutralPalette[10]!!,
        surfaceContainerLowest = if (isLight) neutralPalette[100]!! else neutralPalette[4]!!,
    )
}

