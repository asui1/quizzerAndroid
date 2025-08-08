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
import androidx.compose.ui.graphics.Color
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
    updateQuizTheme: (QuizThemeActions) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        GradientTypeSelector(
            shaderType      = backgroundImageColor.shaderType,
            expanded        = expanded,
            onSelectType    = { type ->
                updateQuizTheme(QuizThemeActions.UpdateGradientType(type))
                expanded = false
            },
            onExpandToggle  = { expanded = it }
        )

        GradientColorPickerRow(
            color1            = backgroundImageColor.color,
            onColor1Selected  = { c -> updateQuizTheme(QuizThemeActions.UpdateBackgroundColor(c)) },
            color2            = backgroundImageColor.colorGradient,
            onColor2Selected  = { c -> updateQuizTheme(QuizThemeActions.UpdateGradientColor(c)) }
        )
    }
}

@Composable
private fun GradientTypeSelector(
    shaderType: ShaderType,
    expanded: Boolean,
    onSelectType: (ShaderType) -> Unit,
    onExpandToggle: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text       = stringResource(R.string.gradient),
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.width(16.dp))
        FastCreateDropDownWithTextButton(
            expanded        = expanded,
            labelText       = stringResource(shaderType.shaderName),
            onItemSelected  = { idx -> onSelectType(ShaderType.entries[idx]) },
            onToggleExpanded = onExpandToggle,
            itemResIds      = ShaderType.entries.map { it.shaderName },
            selectedIndex   = shaderType.index
        )
    }
}

@Composable
private fun GradientColorPickerRow(
    color1: Color,
    onColor1Selected: (Color) -> Unit,
    color2: Color,
    onColor2Selected: (Color) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        ColorPicker(
            modifier        = Modifier.weight(1f),
            initialColor    = color1,
            colorName       = stringResource(R.string.gradient) + " 1",
            onColorSelected = onColor1Selected
        )
        ColorPicker(
            modifier        = Modifier.weight(1f),
            initialColor    = color2,
            colorName       = stringResource(R.string.gradient) + " 2",
            onColorSelected = onColor2Selected
        )
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