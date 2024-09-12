package com.asu1.quizzer.util

import androidx.annotation.OptIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColor
import androidx.media3.common.util.UnstableApi
import com.asu1.quizzer.ui.theme.DarkOnPrimary
import com.asu1.quizzer.ui.theme.DarkOnSecondary
import com.asu1.quizzer.ui.theme.LightError
import com.asu1.quizzer.ui.theme.LightErrorContainer
import com.asu1.quizzer.ui.theme.LightInversePrimary
import com.asu1.quizzer.ui.theme.LightInverseSurface
import com.asu1.quizzer.ui.theme.LightOnError
import com.asu1.quizzer.ui.theme.LightOnErrorContainer
import com.asu1.quizzer.ui.theme.LightOnPrimary
import com.asu1.quizzer.ui.theme.LightOnPrimaryContainer
import com.asu1.quizzer.ui.theme.LightOnSecondary
import com.asu1.quizzer.ui.theme.LightOnSecondaryContainer
import com.asu1.quizzer.ui.theme.LightOnSurface
import com.asu1.quizzer.ui.theme.LightOnSurfaceVariant
import com.asu1.quizzer.ui.theme.LightOnTertiary
import com.asu1.quizzer.ui.theme.LightOnTertiaryContainer
import com.asu1.quizzer.ui.theme.LightOutline
import com.asu1.quizzer.ui.theme.LightOutlineVariant
import com.asu1.quizzer.ui.theme.LightPrimary
import com.asu1.quizzer.ui.theme.LightPrimaryContainer
import com.asu1.quizzer.ui.theme.LightScrim
import com.asu1.quizzer.ui.theme.LightSecondary
import com.asu1.quizzer.ui.theme.LightSecondaryContainer
import com.asu1.quizzer.ui.theme.LightSurface
import com.asu1.quizzer.ui.theme.LightTertiary
import com.asu1.quizzer.ui.theme.LightTertiaryContainer
import com.materialkolor.PaletteStyle
import com.materialkolor.contrast.Contrast
import com.materialkolor.dynamicColorScheme
import com.materialkolor.dynamiccolor.*
import com.materialkolor.rememberDynamicMaterialThemeState
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