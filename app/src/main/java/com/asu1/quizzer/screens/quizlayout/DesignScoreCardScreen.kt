package com.asu1.quizzer.screens.quizlayout

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.Gradient
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.ImageGetter
import com.asu1.quizzer.composables.animations.OpenCloseColumn
import com.asu1.quizzer.composables.animations.UploadingAnimation
import com.asu1.quizzer.composables.base.FastCreateDropDown
import com.asu1.quizzer.composables.base.IconButtonWithText
import com.asu1.quizzer.composables.scorecard.ImagePickerWithBaseImages
import com.asu1.quizzer.composables.scorecard.ScoreCardComposable
import com.asu1.quizzer.composables.scorecard.TextColorPickerModalSheet
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.model.Effect
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.model.ShaderType
import com.asu1.quizzer.model.sampleScoreCard
import com.asu1.quizzer.util.disableImmersiveMode
import com.asu1.quizzer.util.enableImmersiveMode
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.createSampleScoreCardViewModel
import kotlinx.coroutines.launch

val colorNames: List<Int> = listOf(R.string.background_newline,
    R.string.effect, R.string.gradient)

@Composable
fun DesignScoreCardScreen(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    scoreCardViewModel: ScoreCardViewModel = viewModel(),
    onUpload: () -> Unit = { }
) {
    val scoreCard by scoreCardViewModel.scoreCard.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = (configuration.screenWidthDp.dp).coerceAtMost(screenHeight * 0.6f)
    val quizLayoutViewModelState by quizLayoutViewModel.viewModelState.observeAsState()
    var expanded by remember {mutableStateOf(true)}
    var immerseMode by remember { mutableStateOf(false) }
    val context = LocalContext.current as Activity
    val scope = rememberCoroutineScope()

    AnimatedContent(
        targetState = quizLayoutViewModelState,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "Design Scorecard",
    ) { targetState ->
        when(targetState){
            ViewModelState.UPLOADING -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    UploadingAnimation()
                }
            }
            else -> {
                MaterialTheme(
                    colorScheme = scoreCard.colorScheme
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (immerseMode) {
                                    context.disableImmersiveMode()
                                } else {
                                    context.enableImmersiveMode()
                                }
                                immerseMode = !immerseMode
                            }
                    ) {
                        DesignScoreCardBody(
                            scoreCard,
                            immerseMode,
                            onUpload = {
                                scope.launch {
                                    quizLayoutViewModel.tryUpload(navController, scoreCard, onUpload)
                                }
                            },
                        )
                        OpenCloseColumn(
                            isOpen = expanded,
                            onToggleOpen = { expanded = !expanded },
                            height = screenHeight,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                        ){
                            DesignScoreCardTools(
                                scoreCardViewModel,
                                scoreCard,
                                screenWidth,
                                screenHeight
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun DesignScoreCardTools(
    scoreCardViewModel: ScoreCardViewModel,
    scoreCard: ScoreCard,
    screenWidth: Dp,
    screenHeight: Dp,
) {

    var showEffectDropdown by remember { mutableStateOf(false) }
    var showGradientDropdown by remember { mutableStateOf(false) }
    var showScoreCardColorPicker by remember { mutableStateOf(false) }
    var showTextColorPicker by remember { mutableStateOf(false) }
    var showBackgroundImagePicker by remember { mutableStateOf(false) }
    var showOverlayImagePicker by remember { mutableStateOf(false) }
    var colorChange by remember{ mutableIntStateOf(0) }

    if(showScoreCardColorPicker){
        Dialog(
            onDismissRequest = {
                showScoreCardColorPicker = false
            },
            properties = DialogProperties(dismissOnBackPress = true)
        ) {
            TextColorPickerModalSheet(
                initialColor = when(colorChange) {
                    1 -> scoreCard.background.color2
                    2 -> scoreCard.background.colorGradient
                    else -> scoreCard.background.color
                },
                onColorSelected = { color ->
                    scoreCardViewModel.updateColor(color, colorChange)
                },
                text = stringResource(
                    when(colorChange) {
                        1 -> R.string.select_color2
                        2 -> R.string.select_color3
                        else -> R.string.select_color1
                    }
                ),
                onClose = {
                    showScoreCardColorPicker = false
                }
            )
        }
    }
    if(showOverlayImagePicker){
        Dialog(
            onDismissRequest = {
                showOverlayImagePicker = false
            }
        ){
            ImageGetter(
                image = scoreCard.background.overlayImage,
                onImageUpdate = {scoreCardViewModel.updateOverLayImage(it)},
                onImageDelete = {scoreCardViewModel.updateOverLayImage(byteArrayOf())},
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(8.dp))
            )
        }
    }
    if(showTextColorPicker){
        Dialog(
            onDismissRequest = {
                showTextColorPicker = false
            }
        ) {
            TextColorPickerModalSheet(
                initialColor = scoreCard.textColor,
                onColorSelected = { color ->
                    scoreCardViewModel.updateTextColor(color)
                },
                text = stringResource(R.string.select_text_color),
                onClose = {
                    showTextColorPicker = false
                }
            )
        }
    }
    if(showBackgroundImagePicker){
        Dialog(
            onDismissRequest = {
                showBackgroundImagePicker = false
            }
        ) {
            ImagePickerWithBaseImages(
                modifier = Modifier,
                onBaseImageSelected = { baseImage ->
                    scoreCardViewModel.updateBackgroundBase(baseImage)
                    showBackgroundImagePicker = false
                },
                onImageSelected = {byteArray ->
                    scoreCardViewModel.updateBackgroundImage(byteArray)
                    showBackgroundImagePicker = false
                },
                imageColorState = scoreCard.background.state,
                currentSelection = scoreCard.background.backgroundBase,
                currentImage = scoreCard.background.imageData,
                width = screenWidth,
                height = screenHeight,
            )
        }
    }
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        modifier = Modifier
            .wrapContentSize()
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(end = 4.dp, bottom = 4.dp, top = 4.dp)
    ) {
        val iconSize = 32.dp
        IconButtonWithText(
            imageVector = Icons.Default.FormatColorText,
            text = stringResource(R.string.text_color),
            onClick = {
                showTextColorPicker = true
            },
            description = "Set Text Color For ScoreCard",
            modifier = Modifier.testTag("DesignScoreCardSetTextColorButton"),
            iconSize = iconSize,
        )
        IconButtonWithText(
            imageVector = Icons.Default.ImageSearch,
            text = stringResource(R.string.image_on_top),
            onClick = {
                showOverlayImagePicker = true
            },
            description = "Set Image to go on top",
            modifier = Modifier.testTag("DesignScoreCardSetOverlayImage"),
            iconSize = iconSize,
        )
        IconButtonWithText(
            imageVector = Icons.Default.ImageSearch,
            text = stringResource(R.string.background_newline),
            onClick = {
                showBackgroundImagePicker = true
            },
            description = "Set Background Image For ScoreCard",
            modifier = Modifier.testTag("DesignScoreCardSetBackgroundImageButton"),
            iconSize = iconSize,
        )
        colorNames.forEachIndexed { index, colorName ->
            if (colorName == R.string.effect) {
                FastCreateDropDown(
                    showDropdownMenu = showEffectDropdown,
                    labelText = stringResource(R.string.effects),
                    onClick = { dropdownIndex ->

                        showEffectDropdown = false
                        scoreCardViewModel.updateEffect(Effect.entries[dropdownIndex])
                    },
                    onChangeDropDown = { showEffectDropdown = it },
                    inputItems = remember { Effect.entries.map { it.stringId } },
                    imageVector = Icons.Filled.Animation,
                    modifier = Modifier.width(iconSize * 1.7f),
                    testTag = "DesignScoreCardAnimationButton",
                    iconSize = iconSize,
                    currentSelection = scoreCard.background.effect.ordinal
                )
            } else if (colorName == R.string.gradient) {
                FastCreateDropDown(
                    showDropdownMenu = showGradientDropdown,
                    labelText = stringResource(R.string.gradient),
                    onClick = { dropdownIndex ->
                        showGradientDropdown = false
                        scoreCardViewModel.updateShaderType(ShaderType.entries[dropdownIndex])
                    },
                    onChangeDropDown = { showGradientDropdown = it },
                    inputItems = remember { ShaderType.entries.map { it.shaderName } },
                    imageVector = Icons.Filled.Gradient,
                    modifier = Modifier.width(iconSize * 1.7f),
                    testTag = "DesignScoreCardShaderButton",
                    iconSize = iconSize,
                    currentSelection = scoreCard.background.shaderType.index
                )
            }
            IconButtonWithText(
                imageVector = Icons.Default.ColorLens,
                text = stringResource(colorName),
                onClick = {
                    colorChange = index
                    showScoreCardColorPicker = true
                },
                description = "Set Color For ScoreCard",
                modifier = Modifier.testTag("DesignScoreCardSetColorButton$index"),
                iconSize = iconSize,
            )
        }
    }
}

@Composable
private fun DesignScoreCardBody(
    scoreCard: ScoreCard,
    immerseMode: Boolean,
    onUpload: () -> Unit = { },
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ) {
        ScoreCardComposable(
            scoreCard = scoreCard,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .animateContentSize()
        )
        if (!immerseMode) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = {
                        onUpload()
                    },
                    modifier = Modifier
                        .size(width = 200.dp, height = 48.dp)
                        .padding(8.dp)
                        .testTag("DesignScoreCardUploadButton")
                ) {
                    Text(
                        text = stringResource(R.string.upload),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DesignScoreCardPreview() {
    val scoreCard = sampleScoreCard
    DesignScoreCardBody(
        scoreCard = scoreCard,
        immerseMode = false,
        onUpload = {},
    )
}

@Preview(showBackground = true)
@Composable
fun DesignScoreCardToolsPreview() {
    val scoreCard = sampleScoreCard
    DesignScoreCardTools(
        scoreCardViewModel = createSampleScoreCardViewModel(),
        scoreCard = scoreCard,
        screenWidth = 400.dp,
        screenHeight = 800.dp,
    )
}


