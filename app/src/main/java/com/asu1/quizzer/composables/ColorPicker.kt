package com.asu1.quizzer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import okhttp3.internal.toHexString

@Composable
fun ColorPicker(
    initialColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val controller = rememberColorPickerController()
    var argbValue by remember { mutableStateOf(initialColor.toArgb().toUInt().toString(16).padStart(8, '0').uppercase()) }

    LaunchedEffect(initialColor){
        controller.selectByColor(initialColor, false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp),
            controller = controller,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                val color: Color = colorEnvelope.color
                onColorSelected(color)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(35.dp)
                .background(Color.White), // Ensure background color is set correctly
        controller = controller,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = argbValue,
            onValueChange = { newValue ->
                if (newValue.length <= 8) {
                    argbValue = newValue
                    val color = try {
                        Color(android.graphics.Color.parseColor("#$newValue"))
                    } catch (e: IllegalArgumentException) {
                        initialColor
                    }
                    onColorSelected(color)
                }
            },
            label = { Text("ARGB Value") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun ColorPickerPreview() {
    ColorPicker(Color.Red) {}
}