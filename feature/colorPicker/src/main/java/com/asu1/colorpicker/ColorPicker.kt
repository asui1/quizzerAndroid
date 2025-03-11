package com.asu1.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun ColorPicker(
    initialColor: Color,
    testTag: String = "",
    onColorSelected: (Color) -> Unit,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val controller = rememberColorPickerController()
    var localColor by remember { mutableStateOf(initialColor) }
    var rgbValue by remember(localColor) { mutableStateOf(localColor.toRgbHex()) }

    // Initialize color picker with the initial color
    LaunchedEffect(Unit) {
        controller.selectByColor(initialColor, false)
        localColor = initialColor
        rgbValue = initialColor.toRgbHex()
    }

    // Listen for color changes using controller.getColorFlow()
    LaunchedEffect(controller) {
        controller.getColorFlow(100)
            .collect { colorEnvelope ->
                val selectedColor = colorEnvelope.color.copy(alpha = 1.0f)

                // Update localColor to reflect brightness changes
                localColor = selectedColor
                rgbValue = selectedColor.toRgbHex()

                // Notify parent with the updated color
                onColorSelected(selectedColor)
            }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // HSV Color Picker
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            controller = controller,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Brightness Slider
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .background(Color.White),
            controller = controller,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // RGB TextField for Manual Input
        TextField(
            value = rgbValue,
            onValueChange = { newValue ->
                if (newValue.length <= 6) {
                    rgbValue = newValue
                }
                if (newValue.length == 6) {
                    val color = try {
                        Color("#$newValue".toColorInt())
                    } catch (_: IllegalArgumentException) {
                        initialColor
                    }
                    controller.selectByColor(color, true)
                    localColor = color
                }
            },
            label = { Text("RGB Value") },
            modifier = Modifier.fillMaxWidth().testTag(testTag),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                onClose()
            }
        )
    }
}

// Function to convert Color to HEX without Alpha
fun Color.toRgbHex(): String {
    val argb = this.toArgb()
    val r = (argb shr 16) and 0xFF
    val g = (argb shr 8) and 0xFF
    val b = argb and 0xFF
    return String.format("%02X%02X%02X", r, g, b) // Format as 6-digit hex
}

@Preview
@Composable
fun ColorPickerPreview() {
    ColorPicker(Color.Red, onColorSelected =  {})
}