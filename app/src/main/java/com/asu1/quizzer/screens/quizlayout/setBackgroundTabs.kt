package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gradient
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.ImageGetter
import com.asu1.quizzer.composables.base.ColorPicker
import com.asu1.quizzer.composables.base.FastCreateDropDown
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.ShaderType

@Composable
fun BackgroundTabs(
    selectedTabIndex: Int,
    updateBackgroundType: (ImageColorState) -> Unit,
    background: ImageColor,
    onBackgroundColorUpdate: (Color) -> Unit,
    onGradientColorUpdate: (Color) -> Unit,
    onGradientTypeUpdate: (ShaderType) -> Unit = {},
    onImageUpdate: (ByteArray) -> Unit,
    onClose: () -> Unit,
) {
    var showGradientDropdown by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                )
            }
        ) {
            Tab(selected = selectedTabIndex == 0, onClick = {
                updateBackgroundType(ImageColorState.COLOR)
            }) {
                Text(stringResource(R.string.single_color))
            }
            Tab(selected = selectedTabIndex == 1, onClick = {
                updateBackgroundType(ImageColorState.GRADIENT)
            }) {
                Text(
                    stringResource(R.string.gradient),
                )
            }
            Tab(selected = selectedTabIndex == 2, onClick = {
                updateBackgroundType(ImageColorState.IMAGE)
            }) {
                Text(stringResource(R.string.image))
            }
        }
        when (selectedTabIndex) {
            0 -> {
                // Single Color Picker
                ColorPicker(
                    initialColor = background.color,
                    onColorSelected = { color ->
                        onBackgroundColorUpdate(color)
                    },
                    onClose = {
                        onClose()
                    }
                )
            }

            1 -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ){
                        FastCreateDropDown(
                            showDropdownMenu = showGradientDropdown,
                            labelText = "Gradient Type",
                            onClick = { index ->
                                onGradientTypeUpdate(ShaderType.entries[index])
                                showGradientDropdown = false
                            },
                            onChangeDropDown = { showGradientDropdown = it },
                            inputItems = ShaderType.entries.map { it.shaderName },
                            imageVector = Icons.Default.Gradient,
                            currentSelection = background.shaderType.index,
                        )
                        Text(
                            text = stringResource(background.shaderType.shaderName),
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ColorPicker(
                            initialColor = background.color,
                            onColorSelected = { color ->
                                onBackgroundColorUpdate(color)
                            },
                            onClose = {
                                keyboardController?.hide()
                            },
                            modifier = Modifier.weight(1f),
                        )
                        ColorPicker(
                            initialColor = background.colorGradient,
                            onColorSelected = { color ->
                                onGradientColorUpdate(color)
                            },
                            onClose = {
                                keyboardController?.hide()
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            2 -> {
                // Image Picker (Placeholder)
                Spacer(modifier = Modifier.height(24.dp))
                ImageGetter(
                    image = background.imageData,
                    onImageUpdate = { byteArray ->
                        onImageUpdate(byteArray)
                    },
                    onImageDelete = {
                        onImageUpdate(byteArrayOf())
                    },
                )
            }
        }
    }
}