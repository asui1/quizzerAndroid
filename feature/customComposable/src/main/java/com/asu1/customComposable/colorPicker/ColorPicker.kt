package com.asu1.customComposable.colorPicker

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import java.util.Locale

@OptIn(FlowPreview::class)
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    initialColor: Color,
    colorName: String = "",
    testTag: String = "",
    onColorSelected: (Color) -> Unit,
) {
    val state = rememberColorPickerState(initialColor, onColorSelected)

    BoxWithConstraints(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        if (maxWidth > 250.dp) {
            ColorPickerWide(
                state = state,
                colorName = colorName,
                testTag = testTag
            )
        } else {
            ColorPickerCompact(
                state = state,
                colorName = colorName,
                testTag = testTag
            )
        }
    }
}

// Function to convert Color to HEX without Alpha
fun Color.toRgbHex(): String {
    val argb = this.toArgb()
    val r = (argb shr 16) and 0xFF
    val g = (argb shr 8) and 0xFF
    val b = argb and 0xFF
    return String.format(Locale.US, "%02X%02X%02X", r, g, b)
}

@Preview(showBackground = true)
@Composable
fun ColorPickerPreview() {
    ColorPicker(
        modifier = Modifier.height(300.dp).width(600.dp),
        colorName = "Test Color",
        initialColor = Color.Red,
        onColorSelected =  {}
    )
}
