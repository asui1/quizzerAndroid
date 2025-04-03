package com.asu1.colormodel

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.asu1.resources.R

enum class ThemeColorPicker(
    val stringResourceId: Int,
    val colorAccessor: (ColorScheme) -> Color,
    val onColorAccessor: (ColorScheme) -> Color,
){
    Primary(
        stringResourceId = R.string.primary,
        colorAccessor = { it.primary },
        onColorAccessor = { it.onPrimary },
    ),
    PrimaryContainer(
        stringResourceId = R.string.primary_container,
        colorAccessor = { it.primaryContainer },
        onColorAccessor = { it.onPrimaryContainer },
    ),
    Secondary(
        stringResourceId = R.string.secondary,
        colorAccessor = { it.secondary },
        onColorAccessor = { it.onSecondary },
    ),
    SecondaryContainer(
        stringResourceId = R.string.secondary_container,
        colorAccessor = { it.secondaryContainer },
        onColorAccessor = { it.onSecondaryContainer },
    ),
    Tertiary(
        stringResourceId = R.string.tertiary,
        colorAccessor = { it.tertiary },
        onColorAccessor = { it.onTertiary },
    ),
    TertiaryContainer(
        stringResourceId = R.string.tertiary_container,
        colorAccessor = { it.tertiaryContainer },
        onColorAccessor = { it.onTertiaryContainer },
    ),
    Error(
        stringResourceId = R.string.error,
        colorAccessor = { it.error },
        onColorAccessor = { it.onError },
    ),
    ErrorContainer(
        stringResourceId = R.string.error_container,
        colorAccessor = { it.errorContainer },
        onColorAccessor = { it.onErrorContainer },
    ),
    Surface(
        stringResourceId = R.string.surface,
        colorAccessor = { it.surfaceDim },
        onColorAccessor = { it.onSurface },
    ),
    Outline(
        stringResourceId = R.string.outline,
        colorAccessor = { it.outline },
        onColorAccessor = { it.outlineVariant },
    ),
}
