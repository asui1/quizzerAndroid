package com.asu1.quizzer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColorPicker(
    initialColor: Color,
    testTag: String = "",
    onColorSelected: (Color) -> Unit,
) {
    val controller = rememberColorPickerController()
    var localColor by remember { mutableStateOf(Color.Red) }
    var argbValue by remember { mutableStateOf(localColor.toArgb().toUInt().toString(16).padStart(8, '0').uppercase()) }

    LaunchedEffect(Unit){
        controller.selectByColor(initialColor, false)
        localColor = initialColor
        argbValue = initialColor.toArgb().toUInt().toString(16).padStart(8, '0').uppercase()
    }
    LaunchedEffect(initialColor) {
        argbValue = initialColor.toArgb().toUInt().toString(16).padStart(8, '0').uppercase()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            controller = controller,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                val color: Color = colorEnvelope.color
                localColor = color
                onColorSelected(color)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .background(Color.White), // Ensure background color is set correctly
        controller = controller,
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = argbValue,
            onValueChange = { newValue ->
                if (newValue.length <= 8) {
                    argbValue = newValue
                }
                if(newValue.length == 8) {
                    val color = try {
                        Color(android.graphics.Color.parseColor("#$newValue"))
                    } catch (e: IllegalArgumentException) {
                        initialColor
                    }
                    localColor = color
                    onColorSelected(color)
                }
            },
            label = { Text("ARGB Value") },
            modifier = Modifier.fillMaxWidth().testTag(testTag),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
        )
    }
}

@Preview
@Composable
fun ColorPickerPreview() {
    ColorPicker(Color.Red) {}
}