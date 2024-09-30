package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.composables.ColorPicker
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.screens.getQuizLayoutState
import com.asu1.quizzer.states.QuizLayoutState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.byteArrayToImageBitmap
import com.asu1.quizzer.util.calculateSeedColor
import com.github.skydoves.colorpicker.compose.ImageColorPicker
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.rememberDynamicColorScheme
import kotlinx.coroutines.launch
import kotlin.random.Random

data class ColorSchemeState(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onTertiary: Color,
    val error: Color,
    val onError: Color,
)

fun updateColorSchemeState(colorScheme: ColorScheme): List<Color> {
    return listOf(
        colorScheme.primary,
        colorScheme.secondary,
        colorScheme.tertiary,
        colorScheme.onPrimary,
        colorScheme.onSecondary,
        colorScheme.onTertiary,
        colorScheme.error,
        colorScheme.onError,
    )
}

@Composable
fun QuizLayoutSetColorScheme(quizLayoutState: QuizLayoutState, proceed: () -> Unit) {
    val colorStrings = listOf(
        "Primary Color",
        "Secondary Color",
        "Territory Color",
        "onPrimary Color",
        "onSecondary Color",
        "onTerritory Color",
        "Error Color",
        "onError Color"
    )
    val colorScheme by quizLayoutState.colorScheme
    var colorSchemeState by remember {
        mutableStateOf(
            listOf(
                colorScheme.primary,
                colorScheme.secondary,
                colorScheme.tertiary,
                colorScheme.onPrimary,
                colorScheme.onSecondary,
                colorScheme.onTertiary,
                colorScheme.error,
                colorScheme.onError
            )
        )
    }
    val background by remember { mutableStateOf(quizLayoutState.backgroundImage.value) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val fullUpdate by remember{ quizLayoutState.fullUpdate }

    LaunchedEffect(fullUpdate){
        Logger().debug("Full Update$fullUpdate")
    }

    LaunchedEffect(colorScheme) {
        colorSchemeState = updateColorSchemeState(colorScheme)
    }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            item {
                GenerateColorScheme(quizLayoutState)
                BackgroundRow(background, quizLayoutState,
                    text = "Background",
                    onOpen = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                )
            }
            items(colorStrings.size) { index ->
                val colorName = colorStrings[index]
                key(fullUpdate, index){
                    ColorPickerRowOpener(
                        text = colorName,
                        imageColor = colorSchemeState[index],
                        onColorSelected = {
                            quizLayoutState.setColorScheme(colorName, it)
                        },
                        onOpen = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(index + 1)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BackgroundRow(background: ImageColor?, quizLayoutState: QuizLayoutState, text: String, onOpen: () -> Unit){
    var isOpen by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(background) }
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = MaterialTheme.colorScheme.surface)
//    ) {
//        Column {
//            Button(
//                onClick = {
//                    isOpen = !isOpen
//                    if (isOpen) onOpen()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(text = text, style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier.width(200.dp),
//                        overflow = TextOverflow.Ellipsis,
//                    )
//                    Box(
//                        modifier = Modifier
//                            .size(24.dp)
//                            .background(color = selectedColor)
//                    )
//                    Icon(
//                        imageVector = if(isOpen) Icons.Default.ArrowDropDown
//                        else Icons.Default.ArrowDropUp,
//                        contentDescription = "Icon to Open Color Picker",
//                        tint = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            }
//            if (isOpen) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 8.dp)
//                ) {
//                    ColorPicker(
//                        initialColor = selectedColor,
//                        onColorSelected = { color ->
//                            selectedColor = color
//                            onColorSelected(color)
//                        }
//                    )
//                    //TODO: GET IMAGE
//                }
//            }
//        }
//    }

}

fun getColorbyName(colorScheme: ColorScheme, name: String): Color {
    return when(name){
        "Primary Color" -> colorScheme.primary
        "Secondary Color" -> colorScheme.secondary
        "Territory Color" -> colorScheme.tertiary
        "onPrimary Color" -> colorScheme.onPrimary
        "onSecondary Color" -> colorScheme.onSecondary
        "onTerritory Color" -> colorScheme.onTertiary
        "Error Color" -> colorScheme.error
        "onError Color" -> colorScheme.onError
        else -> Color.Red
    }
}

@Composable
fun GenerateColorScheme(quizLayoutState: QuizLayoutState){
    val isTitleImageSet = quizLayoutState.quizImage.value != null

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        IconButtonWithDisable(imageVector = Icons.Default.Autorenew, text = "Gen with\nTitle Image", onClick = {

            val seedColor = calculateSeedColor(byteArrayToImageBitmap(quizLayoutState.quizImage.value!!))
            val titleImageColorScheme = randomDynamicColorScheme(seedColor)
            quizLayoutState.setColorSchemeFull(titleImageColorScheme)
        }, enabled = isTitleImageSet)
        IconButtonWithDisable(imageVector = Icons.Default.Autorenew, text = "Gen with\nPrimary Color", onClick = {
            val primaryColorScheme = randomDynamicColorScheme(quizLayoutState.colorScheme.value.primary)
            quizLayoutState.setColorSchemeFull(primaryColorScheme)
        }, enabled = true)
    }
}

fun randomDynamicColorScheme(seedColor: Color): ColorScheme {
    val isDark = seedColor.luminance() < 0.5
    return dynamicColorScheme(seedColor, isDark = isDark, isAmoled = Random.nextBoolean(),
        style = PaletteStyle.entries.toTypedArray().random(),
        contrastLevel = Contrast.entries.toTypedArray().random().value,
    )
}

@Composable
fun IconButtonWithDisable(imageVector: ImageVector, text: String, onClick: () -> Unit, enabled: Boolean){

    Box(
        modifier = Modifier
            .clickable(enabled = enabled, onClick = onClick)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(imageVector = imageVector, contentDescription = "Previous",
                tint = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun ColorPickerRowOpener(
    text: String,
    imageColor: Color,
    onColorSelected: (Color) -> Unit,
    onOpen: () -> Unit,
) {
    var isOpen by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(imageColor) }

    LaunchedEffect(imageColor) {
        selectedColor = imageColor
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Button(
                onClick = {
                    isOpen = !isOpen
                    if (isOpen) onOpen()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = text, style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.width(200.dp),
                        overflow = TextOverflow.Ellipsis,
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color = selectedColor)
                    )
                    Icon(
                        imageVector = if(isOpen) Icons.Default.ArrowDropDown
                        else Icons.Default.ArrowDropUp,
                        contentDescription = "Icon to Open Color Picker",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (isOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    ColorPicker(
                        initialColor = selectedColor,
                        onColorSelected = { color ->
                            selectedColor = color
                            onColorSelected(color)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutSetColorSchemePreview() {
    QuizzerAndroidTheme {
        QuizLayoutSetColorScheme(
            quizLayoutState = getQuizLayoutState(),
            proceed = {},
        )
    }
}