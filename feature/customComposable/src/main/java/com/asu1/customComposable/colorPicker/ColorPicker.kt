package com.asu1.customComposable.colorPicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.toColorInt
import com.asu1.utils.Logger
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    initialColor: Color,
    colorName: String = "",
    testTag: String = "",
    onColorSelected: (Color) -> Unit,
    onClose: () -> Unit = {},
) {
    val controller = rememberColorPickerController()
    var localColor by remember(initialColor) { mutableStateOf(initialColor) }
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
                Logger.debug("Update Color to $selectedColor")
                // Notify parent with the updated color
                onColorSelected(selectedColor)
            }
    }

    fun onColorSelectedOrUpdate(
        newValue: String,
        onColorSelected: (Color) -> Unit,
        controller: ColorPickerController,
        fallbackColor: Color
    ) {
        if (newValue.length == 6) {
            val color = try {
                Color("#$newValue".toColorInt())
            } catch (_: IllegalArgumentException) {
                fallbackColor
            }
            onColorSelected(color)
            controller.selectByColor(color, true)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val isWide = this.maxWidth > 250.dp

        if (isWide) {
            // Horizontal Layout
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val (labelRef, sampleRef, pickerRef, sliderRef, inputRef) = createRefs()

                Text(
                    text = colorName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .constrainAs(labelRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color = localColor, shape = CircleShape)
                        .constrainAs(sampleRef) {
                            top.linkTo(labelRef.bottom, margin = 4.dp)
                            start.linkTo(parent.start)
                        }
                )

                // HSV Picker (top-right)
                HsvColorPicker(
                    modifier = Modifier
                        .constrainAs(pickerRef) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(sliderRef.top)
                            height = Dimension.fillToConstraints
                        },
                    controller = controller
                )

                // Brightness Slider (below picker)
                BrightnessSlider(
                    modifier = Modifier
                        .constrainAs(sliderRef) {
                            top.linkTo(pickerRef.bottom, margin = 8.dp)
                            start.linkTo(pickerRef.start)
                            end.linkTo(pickerRef.end)
                            width = Dimension.fillToConstraints
                        }
                        .height(25.dp)
                        .background(Color.White),
                    controller = controller
                )

                // RGB TextField (below slider)
                TextField(
                    value = rgbValue,
                    onValueChange = { newValue ->
                        if (newValue.length <= 6) {
                            if (newValue.length == 6) {
                                val color = try {
                                    Color("#$newValue".toColorInt())
                                } catch (_: IllegalArgumentException) {
                                    initialColor
                                }
                                onColorSelected(color)
                                controller.selectByColor(color, true)
                            }
                        }
                    },
                    label = { Text("RGB Value") },
                    modifier = Modifier
                        .constrainAs(inputRef) {
                            top.linkTo(sliderRef.bottom, margin = 4.dp)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(pickerRef.start)
                            end.linkTo(pickerRef.end)
                            width = Dimension.fillToConstraints
                        }
                        .testTag(testTag),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions {
                        onClose()
                    },
                    maxLines = 1,
                )
            }
        } else {
            // Vertical Layout (mobile / narrow screens)
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = colorName,
                        fontWeight = FontWeight.Normal,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(localColor)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    controller = controller,
                )

                Spacer(modifier = Modifier.height(4.dp))

                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .background(Color.White),
                    controller = controller,
                )

                Spacer(modifier = Modifier.height(4.dp))

                TextField(
                    value = rgbValue,
                    onValueChange = { newValue ->
                        if (newValue.length <= 6) {
                            onColorSelectedOrUpdate(newValue, onColorSelected, controller, initialColor)
                        }
                    },
                    label = { Text("RGB Value") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(testTag),
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