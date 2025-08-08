package com.asu1.quiz.scorecard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.asu1.customComposable.button.IconButtonWithText
import com.asu1.customComposable.dropdown.FastCreateDropDownWithIcon
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.quiz.scorecard.scorecardToolsDialogs.BackgroundImagePickerDialog
import com.asu1.quiz.scorecard.scorecardToolsDialogs.EffectPickerDialog
import com.asu1.quiz.scorecard.scorecardToolsDialogs.OverlayImagePickerDialog
import com.asu1.quiz.scorecard.scorecardToolsDialogs.ScoreCardColorPickerDialog
import com.asu1.quiz.scorecard.scorecardToolsDialogs.TextColorPickerDialog
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModelActions
import com.asu1.resources.R
import com.asu1.utils.shaders.ShaderType

@Composable
fun DesignScoreCardTools(
    updateQuizCoordinate: (QuizCoordinatorActions) -> Unit = {},
    removeBackground: Boolean,
    scoreCard: ScoreCard,
    screenWidth: Dp,
    screenHeight: Dp
) {
    // 1️⃣ Keep track of which dialog is open (or none)
    var dialogType by remember { mutableStateOf<ScoreCardDialog>(ScoreCardDialog.None) }
    var colorIndex by remember { mutableIntStateOf(0) }

    // 2️⃣ Host all dialogs in one place
    ToolsDialogHost(
        dialogType       = dialogType,
        colorIndex       = colorIndex,
        scoreCard        = scoreCard,
        removeBackground = removeBackground,
        screenWidth      = screenWidth,
        screenHeight     = screenHeight,
        updateQuiz       = updateQuizCoordinate,
        onDismiss        = { dialogType = ScoreCardDialog.None }
    )

    // 3️⃣ The column of tool buttons
    ToolsButtonColumn(
        scoreCard        = scoreCard,
        onAction = { action ->
            when (action) {
                ToolAction.PickTextColor -> {
                    dialogType = ScoreCardDialog.TextColorPicker
                }
                ToolAction.PickOverlayImage -> {
                    dialogType = ScoreCardDialog.OverlayImagePicker
                }
                ToolAction.PickBackgroundImage -> {
                    dialogType = ScoreCardDialog.BackgroundImagePicker
                }
                ToolAction.OpenEffectPicker -> {
                    dialogType = ScoreCardDialog.EffectPicker
                }
                is ToolAction.PickColor -> {
                    colorIndex = action.index
                    dialogType = ScoreCardDialog.ColorPicker
                }
                is ToolAction.ChangeShader -> {
                    updateQuizCoordinate(
                        QuizCoordinatorActions.UpdateScoreCard(
                            ScoreCardViewModelActions.UpdateShaderType(action.type)
                        )
                    )
                }
            }
        },
    )
}

sealed class ScoreCardDialog {
    object None : ScoreCardDialog()
    object ColorPicker : ScoreCardDialog()
    object OverlayImagePicker : ScoreCardDialog()
    object TextColorPicker : ScoreCardDialog()
    object BackgroundImagePicker : ScoreCardDialog()
    object EffectPicker : ScoreCardDialog()
}

@Composable
private fun ToolsDialogHost(
    dialogType: ScoreCardDialog,
    colorIndex: Int,
    scoreCard: ScoreCard,
    removeBackground: Boolean,
    screenWidth: Dp,
    screenHeight: Dp,
    updateQuiz: (QuizCoordinatorActions) -> Unit,
    onDismiss: () -> Unit
) {
    if (dialogType == ScoreCardDialog.None) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true)
    ) {
        when (dialogType) {
            ScoreCardDialog.ColorPicker ->
                ScoreCardColorPickerDialog(colorIndex, scoreCard, updateQuiz)

            ScoreCardDialog.OverlayImagePicker ->
                OverlayImagePickerDialog(scoreCard, removeBackground, updateQuiz)

            ScoreCardDialog.TextColorPicker ->
                TextColorPickerDialog(scoreCard, updateQuiz)

            ScoreCardDialog.BackgroundImagePicker ->
                BackgroundImagePickerDialog(
                    scoreCard, updateQuiz, onDismiss,
                    screenWidth, screenHeight
                )

            ScoreCardDialog.EffectPicker ->
                EffectPickerDialog(
                    onClick = { eff ->
                        updateQuiz(
                            QuizCoordinatorActions.UpdateScoreCard(
                                ScoreCardViewModelActions.UpdateEffect(eff)
                            )
                        )
                        onDismiss()
                    },
                    currentSelection = scoreCard.background.effect,
                    onDismiss = onDismiss
                )

            ScoreCardDialog.None -> Unit
        }
    }
}

@Composable
private fun ToolsButtonColumn(
    scoreCard: ScoreCard,
    onAction: (ToolAction) -> Unit,
) {
    val iconSize = 32.dp

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        modifier = Modifier
            .wrapContentSize()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(4.dp))
            .padding(end = 4.dp, bottom = 4.dp, top = 4.dp)
    ) {
        ToolIconButton(
            icon = Icons.Default.FormatColorText,
            label = stringResource(R.string.text_color),
            tag = "DesignScoreCardSetTextColorButton",
            iconSize = iconSize,
            onClick = { onAction(ToolAction.PickTextColor) }
        )

        ToolIconButton(
            icon = Icons.Default.ImageSearch,
            label = stringResource(R.string.image_on_top),
            tag = "DesignScoreCardSetOverlayImage",
            iconSize = iconSize,
            onClick = { onAction(ToolAction.PickOverlayImage) }
        )

        ToolIconButton(
            icon = Icons.Default.ImageSearch,
            label = stringResource(R.string.background_newline),
            tag = "DesignScoreCardSetBackgroundImageButton",
            iconSize = iconSize,
            onClick = { onAction(ToolAction.PickBackgroundImage) }
        )

        ToolIconButton(
            icon = Icons.Filled.Animation,
            label = stringResource(R.string.effects),
            tag = "DesignScoreCardAnimationButton",
            iconSize = iconSize,
            onClick = { onAction(ToolAction.OpenEffectPicker) }
        )

        GradientSelector(
            current = scoreCard.background.shaderType,
            iconSize = iconSize,
            onChange = { onAction(ToolAction.ChangeShader(it)) }
        )

        ColorButtons(iconSize = iconSize) { idx ->
            onAction(ToolAction.PickColor(idx))
        }
    }
}

sealed interface ToolAction {
    data object PickTextColor : ToolAction
    data object PickOverlayImage : ToolAction
    data object PickBackgroundImage : ToolAction
    data object OpenEffectPicker : ToolAction
    data class PickColor(val index: Int) : ToolAction
    data class ChangeShader(val type: ShaderType) : ToolAction
}

@Composable
private fun ToolIconButton(
    icon: ImageVector,
    label: String,
    tag: String,
    iconSize: Dp,
    onClick: () -> Unit
) {
    IconButtonWithText(
        imageVector = icon,
        text = label,
        onClick = onClick,
        modifier = Modifier.testTag(tag),
        iconSize = iconSize
    )
}

@Composable
private fun GradientSelector(
    current: ShaderType,
    iconSize: Dp,
    onChange: (ShaderType) -> Unit
) {
    FastCreateDropDownWithIcon(
        showDropdownMenu = false,
        labelText        = stringResource(R.string.gradient),
        inputItems       = ShaderType.entries.map { it.shaderName },
        currentSelection = current.index,
        onItemSelected   = { idx -> onChange(ShaderType.entries[idx]) },
        onChangeDropDown = { /* handled internally */ },
        imageVector      = Icons.Filled.Gradient,
        modifier         = Modifier.width(iconSize * 1.7f),
        testTag          = "DesignScoreCardShaderButton",
        iconSize         = iconSize
    )
}

@Composable
private fun ColorButtons(
    iconSize: Dp,
    onPick: (index: Int) -> Unit
) {
    colorNames.forEachIndexed { index, _ ->
        ToolIconButton(
            icon  = Icons.Default.ColorLens,
            label = stringResource(colorNames[index]),
            tag   = "DesignScoreCardSetColorButton$index",
            iconSize = iconSize,
            onClick = { onPick(index) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DesignScoreCardToolsPreview() {
    val scoreCard = sampleScoreCard
    DesignScoreCardTools(
        updateQuizCoordinate = {},
        removeBackground = false,
        scoreCard = scoreCard,
        screenHeight = 600.dp,
        screenWidth = 80.dp
    )
}

