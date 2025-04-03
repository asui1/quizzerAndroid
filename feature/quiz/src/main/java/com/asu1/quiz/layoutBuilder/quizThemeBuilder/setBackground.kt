package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gradient
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.colorPicker.ColorPicker
import com.asu1.customComposable.dropdown.FastCreateDropDown
import com.asu1.customComposable.imageGetter.ImageGetter
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeActions
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.utils.shaders.ShaderType

@Composable
fun QuizLayoutSetBackground(
    modifier: Modifier = Modifier,
    backgroundImageColor: ImageColor,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
){
    var isOpen by remember { mutableStateOf(true) }
    var selectedTabIndex by remember { mutableIntStateOf(1) }
    LaunchedEffect(backgroundImageColor.state) {
        selectedTabIndex = when (backgroundImageColor.state) {
            ImageColorState.COLOR -> 0
            ImageColorState.GRADIENT -> 1
            ImageColorState.IMAGE -> 2
            else -> 0
        }
    }
    var showGradientDropdown by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        BackgroundSecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
            updateQuizTheme = updateQuizTheme,
        )
        AnimatedVisibility(
            visible = isOpen
        ) {
            when (selectedTabIndex) {
                0 -> {
                    // Single Color Picker
                    ColorPicker(
                        initialColor = backgroundImageColor.color,
                        onColorSelected = { color ->
                            updateQuizTheme(
                                QuizThemeActions.UpdateBackgroundColor(color)
                            )
                        },
                        onClose = {
                            isOpen = false
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
                        ) {
                            FastCreateDropDown(
                                showDropdownMenu = showGradientDropdown,
                                labelText = stringResource(R.string.gradient),
                                onClick = { index ->
                                    updateQuizTheme(
                                        QuizThemeActions.UpdateGradientType(ShaderType.entries[index])
                                    )
                                    showGradientDropdown = false
                                },
                                onChangeDropDown = { showGradientDropdown = it },
                                inputItems = ShaderType.entries.map { it.shaderName },
                                imageVector = Icons.Default.Gradient,
                                currentSelection = backgroundImageColor.shaderType.index,
                            )
                            Text(
                                text = stringResource(backgroundImageColor.shaderType.shaderName),
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            ColorPicker(
                                initialColor = backgroundImageColor.color,
                                onColorSelected = { color ->
                                    updateQuizTheme(
                                        QuizThemeActions.UpdateBackgroundColor(color)
                                    )
                                },
                                onClose = {
                                    keyboardController?.hide()
                                },
                                modifier = Modifier.weight(1f),
                            )
                            ColorPicker(
                                initialColor = backgroundImageColor.colorGradient,
                                onColorSelected = { color ->
                                    updateQuizTheme(
                                        QuizThemeActions.UpdateGradientColor(color)
                                    )
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
                        image = backgroundImageColor.imageData,
                        onImageUpdate = { bitmap ->
                            updateQuizTheme(
                                QuizThemeActions.UpdateBackgroundImage(bitmap)
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun BackgroundSecondaryTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            "${stringResource(R.string.background)}:",
        )
        Spacer(modifier = Modifier.width(4.dp))
        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
        ) {
            Tab(selected = selectedTabIndex == 0, onClick = {
                updateQuizTheme(
                    QuizThemeActions.UpdateBackgroundType(ImageColorState.COLOR)
                )
            }) {
                Text(
                    stringResource(R.string.single_color),
                    style = QuizzerTypographyDefaults.quizzerBodyMediumBold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Tab(selected = selectedTabIndex == 1, onClick = {
                updateQuizTheme(
                    QuizThemeActions.UpdateBackgroundType(ImageColorState.GRADIENT)
                )
            }) {
                Text(
                    stringResource(R.string.gradient),
                    style = QuizzerTypographyDefaults.quizzerBodyMediumBold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Tab(selected = selectedTabIndex == 2, onClick = {
                updateQuizTheme(
                    QuizThemeActions.UpdateBackgroundType(ImageColorState.IMAGE)
                )
            }) {
                Text(
                    stringResource(R.string.image),
                    style = QuizzerTypographyDefaults.quizzerBodyMediumBold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizLayoutSetBackground(){
    QuizzerAndroidTheme {
        QuizLayoutSetBackground(
            backgroundImageColor = ImageColor(
                state = ImageColorState.GRADIENT
            )
        )
    }
}