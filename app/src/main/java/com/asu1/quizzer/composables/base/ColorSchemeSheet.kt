package com.asu1.quizzer.composables.base

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorSchemeSheet(
    colorScheme: ColorScheme,
){
    val clipboardManager = LocalClipboardManager.current
    val primaryColor = colorScheme.primary
    val secondaryColor = colorScheme.secondary
    val tertiaryColor = colorScheme.tertiary
    val colors = remember(colorScheme.primaryContainer, colorScheme.secondaryContainer, colorScheme.tertiaryContainer){
        listOf(
            colorScheme.onPrimary,
            colorScheme.onSecondary,
            colorScheme.onTertiary,

            colorScheme.primaryContainer,
            colorScheme.onPrimaryContainer,
            colorScheme.secondaryContainer,
            colorScheme.onSecondaryContainer,
            colorScheme.tertiaryContainer,
            colorScheme.onTertiaryContainer,

            colorScheme.error,
            colorScheme.onError,
            colorScheme.errorContainer,
            colorScheme.onErrorContainer,
            colorScheme.inverseSurface,
            colorScheme.inversePrimary,

            colorScheme.surface,
            colorScheme.surfaceDim,
            colorScheme.onSurface,
            colorScheme.outline,
            colorScheme.surfaceContainer,
            colorScheme.inverseOnSurface,
        )}
    val colorsKey = remember(colorScheme.primaryContainer, colorScheme.secondaryContainer, colorScheme.tertiaryContainer) {
        colors.hashCode()
    }

    LazyVerticalGrid(
        modifier = Modifier.height(200.dp),
        columns = GridCells.Fixed(6),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
    ) {
        item{
            ColorCircle(primaryColor, clipboardManager)
        }
        item(key = colorsKey){
            ColorCircle(colors[0], clipboardManager)
        }
        item{
            ColorCircle(secondaryColor, clipboardManager)
        }
        item(key = colorsKey+1){
            ColorCircle(colors[1], clipboardManager)
        }
        item{
            ColorCircle(tertiaryColor, clipboardManager)
        }
        itemsIndexed(colors.subList(2, colors.size), key = { index, _ -> colorsKey + index + 2 }) { index, color ->
            ColorCircle(color, clipboardManager)
        }
    }
}

@Composable
private fun ColorCircle(color: Color, clipboardManager: ClipboardManager) {
    Box(
        modifier = Modifier
            .size(40.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier
                .size(40.dp)
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = CircleShape
                )
                .clickable {
                    val argbValue =color.toArgb().toUInt().toString(16).padStart(8, '0').uppercase()
                    clipboardManager.setText(AnnotatedString(argbValue))
                }
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ColorSchemeSheetPreview() {
    ColorSchemeSheet(
        colorScheme = MaterialTheme.colorScheme,
    )
}
