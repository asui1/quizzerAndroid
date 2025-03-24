package com.asu1.quizzer.screens.quizlayout

import android.content.Context
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.colorpicker.ColorSchemeSheet
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.quizzer.model.ImageColorBackground
import com.asu1.quizzer.viewModels.quizModels.QuizCoordinatorActions
import com.asu1.quizzer.viewModels.quizModels.QuizThemeActions
import com.asu1.resources.ColorList
import com.asu1.resources.GenerateWith
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.resources.paletteSize
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun QuizLayoutSetColorScheme(
    colorScheme: ColorScheme = lightColorScheme(),
    isTitleImageSet: Boolean = false,
    backgroundImage: ImageColor = ImageColor( color = Color.White, color2 = Color.White, state = ImageColorState.COLOR),
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
    updateQuizCoordinator: (QuizCoordinatorActions) -> Unit = {}
) {
    val context = LocalContext.current
    val colors = listOf(
        colorScheme.primary,
        colorScheme.secondary,
        colorScheme.tertiary
    )
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var paletteLevel by remember { mutableIntStateOf(7) }
    var contrastLevel by remember { mutableIntStateOf(0) }
    val colorNames = context.resources.getStringArray(R.array.Colors)

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp)
        ) {
            item {
                GenerateColorScheme(
                    isTitleImageSet = isTitleImageSet,
                    contrastLevel = contrastLevel,
                    onContrastLevelUpdate = { level ->
                        contrastLevel = level
                    },
                    paletteLevel = paletteLevel,
                    onPaletteLevelUpdate = { level ->
                        paletteLevel = level
                    },
                    generateColorScheme = {base, isDark ->
                        updateQuizCoordinator(
                            QuizCoordinatorActions.GenerateColorScheme(
                                generateWith = base,
                                palette = paletteLevel,
                                contrast = contrastLevel,
                                isDark = isDark,
                            )
                        )
                    }
                )
            }
            item{
                BackgroundRow(
                    text = stringResource(R.string.background),
                    background = backgroundImage,
                    updateQuizTheme = updateQuizTheme,
                    onOpen = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(1)
                        }
                    },
                )
            }
            items(ColorList.size, key = { index -> ColorList[index] }) { index ->
                if (index == 0 || paletteLevel == paletteSize) {
                    ColorPickerRowOpener(
                        text = colorNames[index],
                        imageColor = colors[index],
                        onColorSelected = { color ->
                            updateQuizTheme(
                                QuizThemeActions.UpdateColor(ColorList[index], color)
                            )
                        },
                        onOpen = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(2 + index)
                            }
                        },
                        buttonTestTag = "QuizLayoutSetColorSchemeButton${ColorList[index]}",
                        colorSchemeTextFieldTestTag = "QuizLayoutSetColorSchemeTextField${ColorList[index]}"
                    )
                }
            }
            item{
                Text(
                    text = stringResource(R.string.will_be_using_colors),
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
    onOpen: () -> Unit,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
) {
    var isOpen by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(background.state) {
        selectedTabIndex = when (background.state) {
            ImageColorState.COLOR -> 0
            ImageColorState.GRADIENT -> 1
            ImageColorState.IMAGE -> 2
            else -> 0
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
                        text = text,
                        style = QuizzerTypographyDefaults.quizzerBodyLarge,
                        modifier = Modifier.width(200.dp),
                        overflow = TextOverflow.Ellipsis,
                    )
                    ImageColorBackground(
                        imageColor = background,
                        modifier = Modifier.size(36.dp)
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
                BackgroundTabs(
                    selectedTabIndex = selectedTabIndex,
                    updateQuizTheme = updateQuizTheme,
                    background = background,
                    onClose = {
                        isOpen = false
                    }
                )
            }
        }
    }
}

fun getPaletteStyleStringMap(context: Context): Map<PaletteStyle, String> {
    val paletteStyleNames = context.resources.getStringArray(R.array.palette_style_names)
    return PaletteStyle.entries.associateWith { paletteStyleNames[it.ordinal] }
}

fun getContrastStringMap(context: Context): Map<Contrast, String>{
    val contrastNames = context.resources.getStringArray(R.array.contrast_names)
    return Contrast.entries.associateWith { contrastNames[it.ordinal] }
}

@Composable
fun GenerateColorScheme(
    isTitleImageSet: Boolean = false,
    contrastLevel: Int = 2,
    onContrastLevelUpdate: (Int) -> Unit = {},
    paletteLevel: Int = 2,
    onPaletteLevelUpdate: (Int) -> Unit = {},
    generateColorScheme: (genWith: GenerateWith, isDark:Boolean) -> Unit = { _, _ -> },
){
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current
    val paletteStyleStringMap = remember{getPaletteStyleStringMap(context)}
    val contrastStringMap = remember{getContrastStringMap(context)}

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
                text = stringResource(R.string.gen_with_title_image),
                onClick = {
                    generateColorScheme(GenerateWith.TITLE_IMAGE, isDark)
                },
                enabled = isTitleImageSet,
                testTag = "QuizLayoutBuilderColorSchemeGenWithTitleImage"
            )
            IconButtonWithDisable(
                imageVector = Icons.Default.Autorenew,
                text = stringResource(R.string.gen_with_primary_color),
                onClick = {
                    generateColorScheme(GenerateWith.COLOR, isDark)
                },
                enabled = true,
                testTag = "QuizLayoutBuilderColorSchemeGenWithPrimaryColor"
            )
        }
        LevelSelector(
            prefix = stringResource(R.string.palette),
            items = PaletteStyle.entries.map { paletteStyleStringMap[it] ?: "" } + stringResource(
                R.string.strict
            ),
            onUpdateLevel = { level ->
                onPaletteLevelUpdate(level)
            },
            selectedLevel = paletteLevel,
        )
        LevelSelector(
            prefix = stringResource(R.string.contrast),
            items = Contrast.entries.map { contrastStringMap[it] ?: "" },
            onUpdateLevel = { level ->
                onContrastLevelUpdate(level)
            },
            selectedLevel = contrastLevel,
        )
    }
}
@Composable
fun LevelSelector(
    prefix: String = "Level : ",
    items: List<String> = listOf("1", "2", "3", "4"),
    onUpdateLevel: (Int) -> Unit = {},
    selectedLevel: Int = 1,
){
    val barLevel = selectedLevel * 2 + 1
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Text(
            text = prefix + items[selectedLevel],
            style = QuizzerTypographyDefaults.quizzerBodyMediumBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val newLevel = (change.position.x / (size.width / items.size)).toInt()
                        if (newLevel in items.indices) {
                            onUpdateLevel(newLevel)
                        }
                    }
                }
        ) {
            for(i in 0 until items.size * 2){
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .weight(1f)
                        .background(
                            if (i < barLevel) MaterialTheme.colorScheme.primary
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
        }
    }

}



@Composable
fun IconButtonWithDisable(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    testTag: String = ""
) {

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
                style = QuizzerTypographyDefaults.quizzerBodyMediumNormal,
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
                    com.asu1.colorpicker.ColorPicker(
                        initialColor = selectedColor,
                        onColorSelected = { color ->
                            selectedColor = color
                            onColorSelected(color)
                        },
                        onClose = {
                            isOpen = false
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
    com.asu1.resources.QuizzerAndroidTheme {
        QuizLayoutSetColorScheme(
        )
    }
}