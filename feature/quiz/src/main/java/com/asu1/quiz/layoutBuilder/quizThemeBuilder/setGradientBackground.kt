package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.colorPicker.ColorPicker
import com.asu1.customComposable.dropdown.FastCreateDropDownWithTextButton
import com.asu1.imagecolor.ImageColor
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeActions
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import com.asu1.utils.shaders.ShaderType

@Composable
fun SetGradientBackground(
    backgroundImageColor: ImageColor,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
){
    var showGradientDropdown by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.gradient),
                fontWeight = FontWeight.Medium,
            )
            Spacer(
                modifier = Modifier.width(16.dp)
            )
            FastCreateDropDownWithTextButton(
                showDropdownMenu = showGradientDropdown,
                labelText = stringResource(backgroundImageColor.shaderType.shaderName),
                onClick = { index ->
                    updateQuizTheme(
                        QuizThemeActions.UpdateGradientType(ShaderType.entries[index])
                    )
                    showGradientDropdown = false
                },
                onChangeDropDown = { showGradientDropdown = it },
                inputStringResourceItems = ShaderType.entries.map { it.shaderName },
                currentSelection = backgroundImageColor.shaderType.index,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            ColorPicker(
                initialColor = backgroundImageColor.color,
                colorName = "Gradient 1",
                onColorSelected = { color ->
                    updateQuizTheme(
                        QuizThemeActions.UpdateBackgroundColor(color)
                    )
                },
                modifier = Modifier.weight(1f),
            )
            ColorPicker(
                initialColor = backgroundImageColor.colorGradient,
                colorName = "Gradient 2",
                onColorSelected = { color ->
                    updateQuizTheme(
                        QuizThemeActions.UpdateGradientColor(color)
                    )
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSetGradientBackground(){
    QuizzerAndroidTheme {
        SetGradientBackground(
            backgroundImageColor = ImageColor(),

        )
    }
}