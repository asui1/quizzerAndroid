package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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

val size = 50.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetColorScheme(
    modifier: Modifier = Modifier,
    currentColors: ColorScheme,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
    scrollTo: () -> Unit = {}
) {
    // state: which color slot is open?
    var selectedColor by remember { mutableStateOf<ThemeColorPicker?>(null) }
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColorSchemeHeader()

        ColorSchemeRow(
            state = listState,
            currentColors = currentColors,
            selected = selectedColor,
            onSelect = { color ->
                selectedColor = if (selectedColor == color) null else color
                if (selectedColor != null) scrollTo()
            }
        )

        ColorSchemeDetailPanel(
            selectedColor  = selectedColor,
            currentColors  = currentColors,
            onColorChanged = { newColor ->
                selectedColor?.let { slot ->
                    updateQuizTheme(
                        QuizThemeActions.UpdateColor(slot, newColor)
                    )
                }
            }
        )
    }
}

@Composable
private fun ColorSchemeHeader() {
    Text(
        text       = stringResource(R.string.color_colon),
        fontWeight = FontWeight.ExtraBold,
        modifier   = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun ColorSchemeRow(
    state: LazyListState,
    currentColors: ColorScheme,
    selected: ThemeColorPicker?,
    onSelect: (ThemeColorPicker) -> Unit
) {
    LazyRow(
        state               = state,
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(ThemeColorPicker.entries, key = { it.name }) { colorSlot ->
            OverlappingColorCircles(
                modifier = Modifier
                    .clickable { onSelect(colorSlot) }
                    .then(
                        if (colorSlot == selected)
                            Modifier.border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        else Modifier
                    )
                    .testTag("ColorSlot${colorSlot.name}"),
                label           = stringResource(colorSlot.stringResourceId),
                backgroundColor = colorSlot.colorAccessor(currentColors),
                foregroundColor = colorSlot.onColorAccessor(currentColors),
                size            = size
            )
        }
    }
}

@Composable
private fun ColorSchemeDetailPanel(
    selectedColor: ThemeColorPicker?,
    currentColors: ColorScheme,
    onColorChanged: (Color) -> Unit
) {
    AnimatedVisibility(visible = selectedColor != null) {
        // wrap in a key so re-composition resets the picker
        key(selectedColor) {
            ColorPicker(
                modifier      = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                initialColor  = selectedColor!!.colorAccessor(currentColors),
                colorName     = stringResource(selectedColor.stringResourceId),
                onColorSelected = onColorChanged,
                testTag       = "ColorPicker${selectedColor.name}"
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

