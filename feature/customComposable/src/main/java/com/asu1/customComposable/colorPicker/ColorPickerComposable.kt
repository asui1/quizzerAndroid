package com.asu1.customComposable.colorPicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker

@Composable
fun ColorPickerWide(
    state: ColorPickerState,
    colorName: String,
    testTag: String
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Text, PreviewCircle, HsvColorPicker, BrightnessSlider, RgbInput…
        Header(state.color, colorName)
        PreviewCircle(modifier = Modifier, state.color)
        ColorSpectrum(modifier = Modifier, state.controller)
        BrightnessSlider(modifier = Modifier, state.controller)
        RgbInput(modifier = Modifier, hex =state.hex, onHexChange = state.onHexChange, testTag =  testTag)
    }
}

@Composable
fun ColorPickerCompact(
    state: ColorPickerState,
    colorName: String,
    testTag: String
) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
            Text(colorName)
            Spacer(Modifier.weight(1f))
            PreviewCircle(size = 24.dp, color = state.color)
        }
        ColorSpectrum(modifier = Modifier.weight(1f), controller = state.controller)
        BrightnessSlider(modifier = Modifier, controller = state.controller)
        RgbInput(
            modifier = Modifier,
            hex = state.hex, onHexChange = state.onHexChange, testTag =  testTag)
    }
}

@Composable
private fun Header(
    color: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        color = color,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        modifier = modifier
    )
}

@Composable
private fun PreviewCircle(
    modifier: Modifier = Modifier,
    color: Color,
    size: Dp = 36.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color = color, shape = CircleShape)
    )
}

@Composable
private fun ColorSpectrum(
    modifier: Modifier = Modifier,
    controller: ColorPickerController,
) {
    HsvColorPicker(
        controller = controller,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)  // keep it square
    )
}

@Composable
private fun RgbInput(
    modifier: Modifier = Modifier,
    hex: String,
    onHexChange: (String) -> Unit,
    testTag: String = "",
) {
    TextField(
        value = hex,
        onValueChange = onHexChange,
        label = { Text("RGB") },
        singleLine = true,
        maxLines = 1,
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done
        )
    )
}
