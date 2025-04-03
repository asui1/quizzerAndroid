package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.asu1.colormodel.ThemeColorPicker
import com.asu1.customComposable.colorPicker.ColorPicker
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeActions
import com.asu1.resources.LightColorScheme
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetColorScheme(
    modifier: Modifier = Modifier,
    currentColors: ColorScheme,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
){
    var selectedColor: ThemeColorPicker? by remember { mutableStateOf(ThemeColorPicker.Primary) }
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth(),
        ) {
            Text(
                stringResource(R.string.color_colon),
                fontWeight = FontWeight.ExtraBold,
            )
            ThemeColorPicker.entries.forEach { color ->
                OverlappingColorCircles(
                    modifier = Modifier
                        .then(
                            if (color == selectedColor) Modifier.border(2.dp, MaterialTheme.colorScheme.primary)
                            else Modifier
                        )
                        .clickable {
                            if(selectedColor == color){
                                selectedColor = null
                            }else{
                                selectedColor = color
                            }
                        },
                    label = stringResource(color.stringResourceId),
                    backgroundColor = color.colorAccessor(currentColors),
                    foregroundColor = color.onColorAccessor(currentColors),
                )
            }
        }
        AnimatedVisibility(
            visible = selectedColor != null,
        ) {
            ColorPicker(
                modifier = Modifier
                    .height(300.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary),
                initialColor = selectedColor?.colorAccessor(currentColors) ?: Color.White,
                colorName = stringResource(selectedColor?.stringResourceId ?: R.string.empty_string),
                onColorSelected = { color ->
                    updateQuizTheme(
                        QuizThemeActions.UpdateColor(
                            colorName = selectedColor?.name ?: "",
                            color = color
                        )
                    )
                },
                onClose = {
                    selectedColor = null
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSetColorScheme(){
    QuizzerAndroidTheme {
        SetColorScheme(
            currentColors = LightColorScheme,
        )
    }
}

