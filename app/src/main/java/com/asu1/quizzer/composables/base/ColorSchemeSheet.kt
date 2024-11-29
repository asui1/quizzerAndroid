package com.asu1.quizzer.composables.base

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorSchemeSheet(
    colorScheme: ColorScheme,
){
    val colors = listOf(
        colorScheme.primary,
        colorScheme.onPrimary,
        colorScheme.secondary,
        colorScheme.onSecondary,
        colorScheme.tertiary,
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
    )

    LazyVerticalGrid(
        modifier = Modifier.height(200.dp),
        columns = GridCells.Fixed(6),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
    ) {
        items(colors.size) { index ->
            val color = colors[index]
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
                ) {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorSchemeSheetPreview() {
    ColorSchemeSheet(
        colorScheme = MaterialTheme.colorScheme,
    )
}
