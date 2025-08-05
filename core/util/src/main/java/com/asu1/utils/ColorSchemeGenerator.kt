package com.asu1.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.hct.Hct
import com.materialkolor.ktx.from
import com.materialkolor.palettes.TonalPalette
import kotlin.random.Random

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

private fun Map<Int, Color>.role(
    lightKey: Int,
    darkKey: Int,
    isLight: Boolean
): Color = this[if (isLight) lightKey else darkKey]!!

fun computeOnColor(hct: Hct): Double{
    val tone = hct.tone
    val computedOnTone = if (tone < 50) {
        (tone + 55).coerceAtMost(100.0)
    } else {
        (tone - 35).coerceAtLeast(0.0)
    }
    return computedOnTone
}

fun computeOnColorToColor(color: Color): Color {
    val hctColor = Hct.from(color)
    val tone = hctColor.tone
    val computedTone = if (tone < 50) {
        (tone + 55).coerceAtMost(100.0)
    } else {
        (tone - 35).coerceAtLeast(0.0)
    }
    val computedHct = Hct.from(hctColor.hue, hctColor.chroma, computedTone)
    return Color(computedHct.toInt())
}

@Suppress("ComplexMethod", "LongMethod")
fun toScheme(isLight: Boolean = true, primary: Color, secondary: Color, tertiary: Color): ColorScheme {
    val pp: Map<Int, Color> = tonalPalette(primary)
    val sp: Map<Int, Color> = tonalPalette(secondary)
    val tp: Map<Int, Color> = tonalPalette(tertiary)
    val np: Map<Int, Color> = tonalPalette(Color.Gray)
    val ep: Map<Int, Color> = tonalPalette(Color.Red)

    return ColorScheme(
        primary                = pp.role(40, 80, isLight),
        onPrimary              = pp.role(100, 20, isLight),
        primaryContainer       = pp.role(90, 30, isLight),
        onPrimaryContainer     = pp.role(10, 90, isLight),
        inversePrimary         = pp.role(80, 40, isLight),

        secondary              = sp.role(40, 80, isLight),
        onSecondary            = sp.role(100, 20, isLight),
        secondaryContainer     = sp.role(90, 30, isLight),
        onSecondaryContainer   = sp.role(10, 90, isLight),

        tertiary               = tp.role(40, 80, isLight),
        onTertiary             = tp.role(100, 20, isLight),
        tertiaryContainer      = tp.role(90, 30, isLight),
        onTertiaryContainer    = tp.role(10, 90, isLight),

        background             = np.role(99, 10, isLight),
        onBackground           = np.role(10, 90, isLight),
        surface                = np.role(99, 10, isLight),
        onSurface              = np.role(10, 90, isLight),
        surfaceVariant         = np.role(90, 30, isLight),
        onSurfaceVariant       = np.role(30, 80, isLight),
        surfaceTint            = pp.role(40, 80, isLight),

        inverseSurface         = np.role(20, 90, isLight),
        inverseOnSurface       = np.role(95, 20, isLight),

        error                  = ep.role(40, 80, isLight),
        onError                = ep.role(100, 20, isLight),
        errorContainer         = ep.role(90, 30, isLight),
        onErrorContainer       = ep.role(10, 90, isLight),

        outline                = np.role(50, 60, isLight),
        outlineVariant         = np.role(80, 30, isLight),
        scrim                  = np[0]!!,

        surfaceBright          = np.role(98, 24, isLight),
        surfaceDim             = np.role(87, 6, isLight),
        surfaceContainer       = np.role(94, 12, isLight),
        surfaceContainerHigh   = np.role(92, 17, isLight),
        surfaceContainerHighest= np.role(90, 24, isLight),
        surfaceContainerLow    = np.role(96, 10, isLight),
        surfaceContainerLowest = np.role(100, 4, isLight),

        // fixed roles always from the "light" keys (but you could inline np[90]!! etc. if you prefer)
        primaryFixed           = pp[90]!!,
        primaryFixedDim        = pp[80]!!,
        onPrimaryFixed         = pp[10]!!,
        onPrimaryFixedVariant  = pp[30]!!,

        secondaryFixed         = sp[90]!!,
        secondaryFixedDim      = sp[80]!!,
        onSecondaryFixed       = sp[10]!!,
        onSecondaryFixedVariant= sp[30]!!,

        tertiaryFixed          = tp[90]!!,
        tertiaryFixedDim       = tp[80]!!,
        onTertiaryFixed        = tp[10]!!,
        onTertiaryFixedVariant = tp[30]!!
    )
}

fun randomDynamicColorScheme(
    seedColor: Color, paletteLevel: PaletteStyle,
    contrastLevel: Contrast, isDark: Boolean): ColorScheme {
    return dynamicColorScheme(primary = seedColor, isDark = isDark, isAmoled = Random.nextBoolean(),
        style = paletteLevel,
        contrastLevel = contrastLevel.value,
    )
}
