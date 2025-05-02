package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetColorScheme(
    modifier: Modifier = Modifier,
    currentColors: ColorScheme,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
    scrollTo: () -> Unit = {},
){
    var selectedColor: ThemeColorPicker? by remember { mutableStateOf(null) }
    val state = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.color_colon),
            fontWeight = FontWeight.ExtraBold,
        )
        LazyRow(
            state = state,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxWidth(),
        ) {
            items(ThemeColorPicker.entries, key = {it.name}) { color ->
                OverlappingColorCircles(
                    modifier = Modifier
                        .testTag("QuizLayoutSetColorSchemeButton${color.name}")
                        .then(
                            if (color == selectedColor) Modifier.border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
                            else Modifier
                        )
                        .clickable {
                            if(selectedColor == color){
                                selectedColor = null
                            }else{
                                selectedColor = color
                                scrollTo()
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
            key(selectedColor){
                ColorPicker(
                    modifier = Modifier
                        .height(300.dp),
                    initialColor = selectedColor?.colorAccessor(currentColors) ?: Color.White,
                    colorName = stringResource(selectedColor?.stringResourceId ?: R.string.empty_string),
                    onColorSelected = { color ->
                        updateQuizTheme(
                            QuizThemeActions.UpdateColor(
                                colorType = selectedColor ?: ThemeColorPicker.Primary,
                                color = color,
                            )
                        )
                    },
                    onClose = {
                        selectedColor = null
                    },
                    testTag = "QuizLayoutSetColorSchemeTextField"
                )
            }
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

