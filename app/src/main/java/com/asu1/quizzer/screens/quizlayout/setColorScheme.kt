package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.composables.ColorPicker
import com.asu1.quizzer.composables.ImageGetter
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.byteArrayToImageBitmap
import com.asu1.quizzer.util.calculateSeedColor
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
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
fun QuizLayoutSetColorScheme(
    colorScheme: ColorScheme = lightColorScheme(),
    quizImage: ByteArray? = null,
    onColorUpdate: (String, Color) -> Unit = {_, _ -> },
    onColorSchemeUpdate: (ColorScheme) -> Unit = { },
    backgroundImage: ImageColor = ImageColor( color = Color.White, imageData = ByteArray(0), color2 = Color.White, state = ImageColorState.COLOR),
    onBackgroundImageUpdate: (ImageColor) -> Unit = { },
    proceed: () -> Unit) {
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
    var colorSchemeState =
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

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
                GenerateColorScheme(
                    quizImage = quizImage,
                    setColorScheme = onColorSchemeUpdate,
                    primaryColor = colorScheme.primary,
                )
            }
            item{
                BackgroundRow(
                    text = "Background",
                    background = backgroundImage,
                    onImageSelected = { imageColor ->
                        onBackgroundImageUpdate(imageColor)
                    },
                    onOpen = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(1)
                        }
                    }
                )
            }
            items(colorStrings.size) { index ->
                val colorName = colorStrings[index]
                ColorPickerRowOpener(
                    text = colorName,
                    imageColor = colorSchemeState[index],
                    onColorSelected = {color ->
                        onColorUpdate(colorName, color)
                    },
                    onOpen = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index + 2)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BackgroundRow(
    text: String,
    background: ImageColor,
    onImageSelected: (ImageColor) -> Unit,
    onOpen: () -> Unit,
) {
    var isOpen by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(background) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val gradient = Brush.linearGradient(
        colors = listOf(Color.Red, Color.Blue)
    )
    LaunchedEffect(background) {
        selectedColor = background
        selectedTabIndex = when (background.state) {
            ImageColorState.COLOR -> 0
            ImageColorState.COLOR2 -> 1
            ImageColorState.IMAGE -> 2
        }
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
                    Text(
                        text = text, style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.width(200.dp),
                        overflow = TextOverflow.Ellipsis,
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .paint(
                                painter = selectedColor?.getAsImage() ?: ColorPainter(
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                    )
                    Icon(
                        imageVector = if (isOpen) Icons.Default.ArrowDropDown
                        else Icons.Default.ArrowDropUp,
                        contentDescription = "Icon to Open Color Picker",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (isOpen) {
                //SET 3 Tabs for 1 color, 2 colors with gradient, image
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
                            onImageSelected(
                                ImageColor(
                                    color = background.color,
                                    imageData = background.imageData,
                                    color2 = background.color2,
                                    state = ImageColorState.COLOR
                                )
                            )
                        }) {
                            Text("Single Color")
                        }
                        Tab(selected = selectedTabIndex == 1, onClick = {
                            onImageSelected(
                                ImageColor(
                                    color = background.color,
                                    imageData = background.imageData,
                                    color2 = background.color2,
                                    state = ImageColorState.COLOR
                                )
                            )
                        }) {
                            Text("Gradient",
                                style = TextStyle(
                                    brush = gradient,
                                    textDecoration = TextDecoration.LineThrough
                                ),
                            )
                        }
                        Tab(selected = selectedTabIndex == 2, onClick = {
                            onImageSelected(
                                ImageColor(
                                    color = background.color,
                                    imageData = background.imageData,
                                    color2 = background.color2,
                                    state = ImageColorState.IMAGE
                                )
                            )
                        }) {
                            Text("Image")
                        }
                    }
                    when (selectedTabIndex) {
                        0 -> {
                            // Single Color Picker
                            ColorPicker(
                                initialColor = selectedColor.color,
                                onColorSelected = { color ->
                                    onImageSelected(
                                        ImageColor(
                                            color = color,
                                            imageData = selectedColor.imageData,
                                            color2 = selectedColor.color2,
                                            state = ImageColorState.COLOR
                                        )
                                    )
                                }
                            )
                        }

                        1 -> {
                            // Gradient Picker (Placeholder)
                            // TODO Make opengl functions using two colors and draw gradient with two selected colors and selected method
                            Text("Gradient Picker Placeholder\n To be implemented")
                        }

                        2 -> {
                            // Image Picker (Placeholder)
                            Spacer(modifier = Modifier.height(24.dp))
                            ImageGetter(
                                image = selectedColor.imageData,
                                onImageUpdate = { byteArray ->
                                    onImageSelected(
                                        ImageColor(
                                            color = selectedColor.color,
                                            imageData = byteArray,
                                            color2 = selectedColor.color2,
                                            state = ImageColorState.IMAGE
                                        )
                                    )
                                },
                                onImageDelete = {
                                    onImageSelected(
                                        ImageColor(
                                            color = selectedColor.color,
                                            imageData = ByteArray(0),
                                            color2 = selectedColor.color2,
                                            state = ImageColorState.IMAGE
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            }

        }
    }
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
fun GenerateColorScheme(
    quizImage: ByteArray? = byteArrayOf(),
    setColorScheme: (ColorScheme) -> Unit = {},
    primaryColor: Color = MaterialTheme.colorScheme.primary,
){
    val isTitleImageSet = quizImage != null

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        IconButtonWithDisable(imageVector = Icons.Default.Autorenew, text = "Gen with\nTitle Image", onClick = {

            val seedColor = calculateSeedColor(byteArrayToImageBitmap(quizImage!!))
            val titleImageColorScheme = randomDynamicColorScheme(seedColor)
            setColorScheme(titleImageColorScheme)
        }, enabled = isTitleImageSet)
        IconButtonWithDisable(imageVector = Icons.Default.Autorenew, text = "Gen with\nPrimary Color", onClick = {
            val primaryColorScheme = randomDynamicColorScheme(primaryColor)
            setColorScheme(primaryColorScheme)
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
            proceed = {},
        )
    }
}