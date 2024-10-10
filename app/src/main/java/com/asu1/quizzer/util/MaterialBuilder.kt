package com.asu1.quizzer.util

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import kotlin.random.Random

class MaterialThemeBuilder
    (
    private val primary: Color,
    private val isDark: Boolean = false,
) {

    fun toScheme(): ColorScheme {
        val colorscheme = dynamicColorScheme(
            primary,
            isDark,
            false,
            getRandomPaletteStyle(),
            getRandomContrast(),
        )
        return colorscheme
    }

    fun getRandomContrast(): Double {
        val contrast = listOf(0.0, 0.5, 1.0, -1.0)
        return contrast[Random.nextInt(contrast.size)]
    }

    fun getRandomPaletteStyle(): PaletteStyle {
        val styles = PaletteStyle.entries.toTypedArray()
        return styles[Random.nextInt(styles.size)]
    }
}