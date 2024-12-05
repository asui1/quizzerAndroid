package com.asu1.quizzer.screens.quizlayout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.animations.UploadingAnimation
import com.asu1.quizzer.composables.base.FastCreateDropDown
import com.asu1.quizzer.composables.base.IconButtonWithText
import com.asu1.quizzer.composables.scorecard.ImagePickerWithBaseImages
import com.asu1.quizzer.composables.scorecard.ScoreCardComposable
import com.asu1.quizzer.composables.scorecard.TextColorPickerModalSheet
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.model.Effect
import com.asu1.quizzer.model.ShaderType
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
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var showScoreCardColorPicker by remember { mutableStateOf(false) }
    var colorChange by remember{ mutableIntStateOf(0) }
    var showEffectDropdown by remember { mutableStateOf(false) }
    var showGradientDropdown by remember { mutableStateOf(false) }
    var showTextColorPicker by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val quizLayoutViewModelState by quizLayoutViewModel.viewModelState.observeAsState()

    AnimatedContent(
        targetState = quizLayoutViewModelState,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "Design Scorecard",
    ) { targetState ->
        when(targetState){
            ViewModelState.UPLOADING -> UploadingAnimation()
            else -> {
                MaterialTheme(
                    colorScheme = scoreCard.colorScheme
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
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
                                    )
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
                                    text = stringResource(R.string.select_text_color)
                                )
                            }
                        }
                        if(showImagePicker){
                            Dialog(
                                onDismissRequest = {
                                    showImagePicker = false
                                }
                            ) {
                                ImagePickerWithBaseImages(
                                    modifier = Modifier,
                                    onBaseImageSelected = { baseImage ->
                                        scoreCardViewModel.updateBackgroundBase(baseImage)
                                        showImagePicker = false
                                    },
                                    onImageSelected = {byteArray ->
                                        scoreCardViewModel.updateBackgroundImage(byteArray)
                                        showImagePicker = false
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
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            ScoreCardComposable(
                                width = screenWidth,
                                height = screenHeight * 0.9f,
                                scoreCard = scoreCard,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ){
                                Button(
                                    onClick = {
                                        scope.launch {
                                            quizLayoutViewModel.tryUpload(navController, scoreCard, onUpload)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(width = screenWidth * 0.6f, height = 48.dp)
                                        .padding(8.dp)
                                        .testTag("DesignScoreCardUploadButton")
                                ) {
                                    Text(
                                        text = stringResource(R.string.upload),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.CenterEnd)
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh, shape = RoundedCornerShape(4.dp))
                                .padding(end = 4.dp, bottom = 4.dp, top = 4.dp)
                        ){
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
                                text = stringResource(R.string.background_newline),
                                onClick = {
                                    showImagePicker = true
                                },
                                description = "Set Background Image For ScoreCard",
                                modifier = Modifier.testTag("DesignScoreCardSetBackgroundImageButton"),
                                iconSize = iconSize,
                            )
                            colorNames.forEachIndexed { index, colorName ->
                                if(colorName == R.string.effect){
                                    FastCreateDropDown(
                                        showDropdownMenu = showEffectDropdown,
                                        labelText = stringResource(R.string.effects),
                                        onClick = {dropdownIndex ->
                                            showEffectDropdown = false
                                            scoreCardViewModel.updateEffect(Effect.entries[dropdownIndex])
                                        },
                                        onChangeDropDown = { showEffectDropdown = it },
                                        inputItems = remember{ Effect.entries.map { it.stringId }},
                                        imageVector = Icons.Filled.Animation,
                                        modifier = Modifier.width(iconSize * 1.7f),
                                        testTag = "DesignScoreCardAnimationButton",
                                        iconSize = iconSize,
                                        currentSelection = scoreCard.background.effect.ordinal
                                    )
                                }
                                else if(colorName == R.string.gradient){
                                    FastCreateDropDown(
                                        showDropdownMenu = showGradientDropdown,
                                        labelText = stringResource(R.string.gradient),
                                        onClick = {dropdownIndex ->
                                            showGradientDropdown = false
                                            scoreCardViewModel.updateShaderType(ShaderType.entries[dropdownIndex])
                                        },
                                        onChangeDropDown = { showGradientDropdown = it },
                                        inputItems = remember{ ShaderType.entries.map { it.shaderName }},
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
                        if(quizLayoutViewModelState == ViewModelState.UPLOADING){
                            UploadingAnimation()
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DesignScoreCardPreview() {
    val scoreCardViewModel = createSampleScoreCardViewModel()
    DesignScoreCardScreen(
        navController = rememberNavController(),
        scoreCardViewModel = scoreCardViewModel,

        )
}


