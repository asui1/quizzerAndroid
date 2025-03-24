package com.asu1.customComposable.colorPicker

import android.content.ClipData
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ColorSchemeSheet(
    colorScheme: ColorScheme,
){
    val clipboard = LocalClipboard.current
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
            ColorCircle(primaryColor, clipboard)
        }
        item(key = colorsKey){
            ColorCircle(colors[0], clipboard)
        }
        item{
            ColorCircle(secondaryColor, clipboard)
        }
        item(key = colorsKey+1){
            ColorCircle(colors[1], clipboard)
        }
        item{
            ColorCircle(tertiaryColor, clipboard)
        }
        itemsIndexed(colors.subList(2, colors.size), key = { index, _ -> colorsKey + index + 2 }) { index, color ->
            ColorCircle(color, clipboard)
        }
    }
}

@Composable
private fun ColorCircle(color: Color, clipboard: Clipboard) {
    val scope = rememberCoroutineScope()

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
                    val rgbValue = (color.toArgb() and 0xFFFFFF).toUInt().toString(16).padStart(6, '0').uppercase()
                    scope.launch {
                        val clip = ClipData.newPlainText("copied color", rgbValue)
                        clipboard.setClipEntry(ClipEntry(clip))
                    }
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
