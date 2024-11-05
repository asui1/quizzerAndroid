package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.composables.ColorPicker
import com.asu1.quizzer.composables.ColorSchemeSheet
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
    ) {
    val colorSchemeState = colorScheme.primary
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var selectedLevel by remember { mutableIntStateOf(2) }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            item {
                GenerateColorScheme(
                    quizImage = quizImage,
                    setColorScheme = onColorSchemeUpdate,
                    primaryColor = colorScheme.primary,
                    selectedLevel = selectedLevel,
                    onUpdateLevel = { level ->
                        selectedLevel = level
                    }
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
            item{
                val colorName = "Primary Color"
                ColorPickerRowOpener(
                    text = colorName,
                    imageColor = colorSchemeState,
                    onColorSelected = {color ->
                        onColorUpdate(colorName, color)
                    },
                    onOpen = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(2)
                        }
                    },
                    buttonTestTag = "QuizLayoutSetColorSchemeButton$colorName",
                    colorSchemeTextFieldTestTag = "QuizLayoutSetColorSchemeTextField$colorName"
                )
            }
            item{
                Text(
                    text = "Colors : ",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
                ColorSchemeSheet(
                    colorScheme = colorScheme
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
    selectedLevel: Int = 2,
    onUpdateLevel: (Int) -> Unit = {},
){
    val isTitleImageSet = quizImage != null
    val levels = 5
    val isDark = isSystemInDarkTheme()

    Column(

    )
    {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            IconButtonWithDisable(
                imageVector = Icons.Default.Autorenew,
                text = "Gen with\nTitle Image",
                onClick = {

                    val seedColor = calculateSeedColor(byteArrayToImageBitmap(quizImage!!))
                    val titleImageColorScheme = randomDynamicColorScheme(seedColor, selectedLevel, isDark)
                    setColorScheme(titleImageColorScheme)
                },
                enabled = isTitleImageSet,
                testTag = "QuizLayoutBuilderColorSchemeGenWithTitleImage"
            )
            IconButtonWithDisable(
                imageVector = Icons.Default.Autorenew,
                text = "Gen with\nPrimary Color",
                onClick = {
                    val primaryColorScheme = randomDynamicColorScheme(primaryColor, selectedLevel, isDark)
                    setColorScheme(primaryColorScheme)
                },
                enabled = true,
                testTag = "QuizLayoutBuilderColorSchemeGenWithPrimaryColor"
            )
        }
        LevelSelector(
            onUpdateLevel = { level ->
                onUpdateLevel(level)
            },
            selectedLevel = selectedLevel,
            levels = levels
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LevelSelector(
    start: String = "Weak\nRandom",
    end: String = "Strong\nRandom",
    onUpdateLevel: (Int) -> Unit = {},
    selectedLevel: Int = 1,
    levels: Int = 4,
){
    val barLevel = selectedLevel * 2 + 1
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val newLevel = (change.position.x / (size.width / levels)).toInt()
                    if (newLevel in 0..levels) {
                        onUpdateLevel(newLevel)
                    }
                }
            }
    ) {
        Text(text = start,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(4.dp))
        for(i in 0 until levels * 2){
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .weight(1f)
                    .background(
                        if(i < barLevel) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.4f
                        ),
                    )
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) {
                        onUpdateLevel(i / 2)
                    }
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = end,
            textAlign = TextAlign.Center
        )
    }
}

fun randomDynamicColorScheme(seedColor: Color, randomStrength: Int = 2, isDark: Boolean): ColorScheme {
    val style = when (randomStrength) {
        0 -> PaletteStyle.TonalSpot
        1 -> listOf(PaletteStyle.TonalSpot, PaletteStyle.Neutral).random()
        2 -> listOf(PaletteStyle.TonalSpot, PaletteStyle.Neutral, PaletteStyle.Vibrant).random()
        3 -> listOf(PaletteStyle.TonalSpot, PaletteStyle.Neutral, PaletteStyle.Vibrant, PaletteStyle.Expressive).random()
        4 -> PaletteStyle.entries.toTypedArray().random()
        else -> PaletteStyle.entries.toTypedArray().random()
    }

    val contrast = when (randomStrength) {
        0 -> Contrast.Default.value
        1 -> listOf(Contrast.Default.value, Contrast.Medium.value).random()
        2 -> listOf(Contrast.Default.value, Contrast.Medium.value, Contrast.High.value).random()
        3 -> listOf(Contrast.Default.value, Contrast.Medium.value, Contrast.High.value, Contrast.Reduced.value).random()
        4 -> Contrast.entries.toTypedArray().random().value
        else -> Contrast.entries.toTypedArray().random().value
    }
    return dynamicColorScheme(seedColor, isDark = isDark, isAmoled = Random.nextBoolean(),
        style = style,
        contrastLevel = contrast,
    )
}

@Composable
fun IconButtonWithDisable(imageVector: ImageVector, text: String, onClick: () -> Unit, enabled: Boolean, testTag: String = "") {

    Box(
        modifier = Modifier
            .clickable(enabled = enabled, onClick = onClick)
            .background(color = MaterialTheme.colorScheme.surface)
            .testTag(testTag)
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
    buttonTestTag: String = "",
    colorSchemeTextFieldTestTag: String = "",
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
                    .testTag(buttonTestTag)
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
                        },
                        testTag = colorSchemeTextFieldTestTag
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
        )
    }
}