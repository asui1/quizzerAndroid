package com.asu1.quiz.scorecard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.Gradient
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.asu1.customComposable.Switch.LabeledSwitch
import com.asu1.customComposable.button.IconButtonWithText
import com.asu1.customComposable.dialog.FastCreateDialog
import com.asu1.customComposable.dropdown.FastCreateDropDownWithIcon
import com.asu1.customComposable.imageGetter.ImageGetter
import com.asu1.imagecolor.Effect
import com.asu1.imagecolor.ImageBlendMode
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.quiz.scorecard.scorecardToolsDialogs.BackgroundImagePickerDialog
import com.asu1.quiz.scorecard.scorecardToolsDialogs.OverlayImagePickerDialog
import com.asu1.quiz.scorecard.scorecardToolsDialogs.ResetToTransparentButton
import com.asu1.quiz.scorecard.scorecardToolsDialogs.ScoreCardColorPickerDialog
import com.asu1.quiz.scorecard.scorecardToolsDialogs.TextColorPickerDialog
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModelActions
import com.asu1.resources.R
import com.asu1.utils.shaders.ShaderType

sealed class ScoreCardDialog {
    object None : ScoreCardDialog()
    object ColorPicker : ScoreCardDialog()
    object OverlayImagePicker : ScoreCardDialog()
    object TextColorPicker : ScoreCardDialog()
    object BackgroundImagePicker : ScoreCardDialog()
}

@Composable
fun DesignScoreCardTools(
    updateQuizCoordinate: (QuizCoordinatorActions) -> Unit = {},
    removeBackground: Boolean = false,
    scoreCard: ScoreCard,
    screenWidth: Dp,
    screenHeight: Dp,
) {
    var showEffectDropdown by remember { mutableStateOf(false) }
    var showGradientDropdown by remember { mutableStateOf(false) }
    var colorIndex by remember{ mutableIntStateOf(0) }
    var showEffectsDialog by remember {mutableStateOf(false)}
    var scoreCardDialog by remember {mutableStateOf<ScoreCardDialog>(ScoreCardDialog.None)}

    fun onDismiss() {
        scoreCardDialog = ScoreCardDialog.None
    }
    if(scoreCardDialog != ScoreCardDialog.None) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(
                dismissOnBackPress = true,
            )
        ) {
            when(scoreCardDialog){
                ScoreCardDialog.ColorPicker -> {
                    ScoreCardColorPickerDialog(
                        colorIndex = colorIndex,
                        scoreCard = scoreCard,
                        updateQuizCoordinate = updateQuizCoordinate,
                        onDismiss = {onDismiss()}
                    )
                }
                ScoreCardDialog.OverlayImagePicker -> {
                    OverlayImagePickerDialog(
                        scoreCard = scoreCard,
                        removeBackground = removeBackground,
                        updateQuizCoordinate = updateQuizCoordinate,
                        onDismiss = {onDismiss()}
                    )
                }
                ScoreCardDialog.TextColorPicker -> {
                    TextColorPickerDialog(
                        scoreCard = scoreCard,
                        updateQuizCoordinate = updateQuizCoordinate,
                        onDismiss = {onDismiss()}
                    )
                }
                ScoreCardDialog.BackgroundImagePicker -> {
                    BackgroundImagePickerDialog(
                        scoreCard = scoreCard,
                        updateQuizCoordinate = updateQuizCoordinate,
                        onDismiss = {onDismiss()},
                        screenWidth = screenWidth,
                        screenHeight = screenHeight
                    )
                }
                ScoreCardDialog.None -> { /* Do nothing */ }
            }
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
                scoreCardDialog = ScoreCardDialog.TextColorPicker
            },
            description = "Set Text Color For ScoreCard",
            modifier = Modifier.testTag("DesignScoreCardSetTextColorButton"),
            iconSize = iconSize,
        )
        IconButtonWithText(
            imageVector = Icons.Default.ImageSearch,
            text = stringResource(R.string.image_on_top),
            onClick = {
                scoreCardDialog = ScoreCardDialog.OverlayImagePicker
            },
            description = "Set Image to go on top",
            modifier = Modifier.testTag("DesignScoreCardSetOverlayImage"),
            iconSize = iconSize,
        )
        IconButtonWithText(
            imageVector = Icons.Default.ImageSearch,
            text = stringResource(R.string.background_newline),
            onClick = {
                scoreCardDialog = ScoreCardDialog.BackgroundImagePicker
            },
            description = "Set Background Image For ScoreCard",
            modifier = Modifier.testTag("DesignScoreCardSetBackgroundImageButton"),
            iconSize = iconSize,
        )
        colorNames.forEachIndexed { index, colorName ->
            if (colorName == R.string.effect) {
                FastCreateDialog(
                    showDialog = showEffectsDialog,
                    labelText = stringResource(R.string.effects),
                    onClick = { dropdownIndex ->
                        showEffectDropdown = false
                        updateQuizCoordinate(
                            QuizCoordinatorActions.UpdateScoreCard(
                                ScoreCardViewModelActions.UpdateEffect(Effect.entries[dropdownIndex])
                            )
                        )
                    },
                    onChangeDialog = { showEffectsDialog = it },
                    inputItems = remember { Effect.entries.map { it.stringId } },
                    modifier = Modifier.width(iconSize * 1.7f),
                    imageVector = Icons.Filled.Animation,
                    testTag = "DesignScoreCardAnimationButton",
                    iconSize = iconSize,
                    currentSelection = scoreCard.background.effect.ordinal
                )
            } else if (colorName == R.string.gradient) {
                FastCreateDropDownWithIcon(
                    showDropdownMenu = showGradientDropdown,
                    labelText = stringResource(R.string.gradient),
                    onClick = { dropdownIndex ->
                        showGradientDropdown = false
                        updateQuizCoordinate(
                            QuizCoordinatorActions.UpdateScoreCard(
                                ScoreCardViewModelActions.UpdateShaderType(ShaderType.entries[dropdownIndex])
                            )
                        )
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
                    colorIndex = index
                    scoreCardDialog = ScoreCardDialog.ColorPicker
                },
                description = "Set Color For ScoreCard",
                modifier = Modifier.testTag("DesignScoreCardSetColorButton$index"),
                iconSize = iconSize,
            )
        }
    }
}

@Composable
fun ToggleTextWithSwitch(
    textA: String,
    textB: String,
    isBSelected: Boolean,
    onClick: (Boolean) -> Unit = {},
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // A switch to toggle the state.
        Text(
            text = textA,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = isBSelected,
            onCheckedChange = { onClick(it) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = textB,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ToggleTextWithSwitchPreview() {
    ToggleTextWithSwitch(
        textA = "COLOR",
        textB = "HUE",
        isBSelected = true,
        
    )
}

@Preview(showBackground = true)
@Composable
fun DesignScoreCardToolsPreview() {
    val scoreCard = sampleScoreCard
    DesignScoreCardTools(
        scoreCard = scoreCard,
        screenHeight = 600.dp,
        screenWidth = 80.dp
    )
}

