package com.asu1.customComposable.colorPicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun rememberColorPickerState(
    initialColor: Color,
    onColorSelected: (Color) -> Unit
): ColorPickerState {
    val controller = rememberColorPickerController()
    var color by remember { mutableStateOf(initialColor) }
    var hex by remember { mutableStateOf(initialColor.toRgbHex()) }

    // init and flow‐collect
    LaunchedEffect(Unit) {
        controller.selectByColor(initialColor, false)
    }
    LaunchedEffect(controller) {
        controller.getColorFlow(50).collect { envelope ->
            if (envelope.fromUser) {
                color = envelope.color.copy(alpha = 1f)
                hex = color.toRgbHex()
                onColorSelected(color)
            }
        }
    }

    return ColorPickerState(controller, color, hex) { newHex ->
        // moved from onColorSelectedOrUpdate
        hex = newHex.take(6)
        if (hex.length == 6) {
            runCatching {
                Color("#$hex".toColorInt())
            }.getOrNull()?.let { c ->
                color = c
                onColorSelected(c)
                controller.selectByColor(c, true)
            }
        }
    }
}

data class ColorPickerState(
    val controller: ColorPickerController,
    val color: Color,
    val hex: String,
    val onHexChange: (String) -> Unit
)
