package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.colormodel.ContrastLevel
import com.asu1.colormodel.PaletteLevel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.resources.GenerateWith
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun GenerateNewColorScheme(
    isTitleImageSet: Boolean = false,
    generateColorScheme: (QuizCoordinatorActions) -> Unit = {},
    scrollTo: () -> Unit = {}
) {
    // 1️⃣ local UI state
    var openSettings by remember { mutableStateOf(false) }
    var paletteLevel by remember { mutableStateOf(PaletteLevel.Fidelity) }
    var contrastLevel by remember { mutableStateOf(ContrastLevel.Default) }
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SchemeHeader()
        ButtonsRow(
            isTitleImageSet = isTitleImageSet,
            onGenerateWithImage = {
                generateColorScheme(
                    QuizCoordinatorActions.GenerateColorScheme(
                        generateWith = GenerateWith.TITLE_IMAGE,
                        palette     = paletteLevel,
                        contrast    = contrastLevel,
                        isDark      = isDark
                    )
                )
            },
            onGenerateWithColor = {
                generateColorScheme(
                    QuizCoordinatorActions.GenerateColorScheme(
                        generateWith = GenerateWith.COLOR,
                        palette     = paletteLevel,
                        contrast    = contrastLevel,
                        isDark      = isDark
                    )
                )
            },
            onToggleSettings = {
                openSettings = !openSettings
                if (openSettings) scrollTo()
            }
        )
        if (openSettings) {
            ColorSettingsPanel(
                paletteLevel    = paletteLevel,
                onPaletteChange = { paletteLevel = it },
                contrastLevel   = contrastLevel,
                onContrastChange= { contrastLevel = it }
            )
        }
    }
}

@Composable
private fun SchemeHeader() {
    Text(
        text       = stringResource(R.string.refresh_color),
        fontWeight = FontWeight.Bold,
        modifier   = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun ButtonsRow(
    isTitleImageSet: Boolean,
    onGenerateWithImage: () -> Unit,
    onGenerateWithColor: () -> Unit,
    onToggleSettings: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(
            enabled = isTitleImageSet,
            onClick = onGenerateWithImage,
            shape = RoundedCornerShape(10),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 10.dp)
        ) {
            Text(stringResource(R.string.gen_with_title_image))
        }
        TextButton(
            onClick = onGenerateWithColor,
            shape = RoundedCornerShape(10),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 10.dp),
            modifier = Modifier.testTag("QuizLayoutBuilderColorSchemeGenWithPrimaryColor")
        ) {
            Text(stringResource(R.string.gen_with_primary_color))
        }
        IconButton(onClick = onToggleSettings) {
            Icon(
                imageVector     = Icons.Default.Edit,
                contentDescription = "Open Edit Row for Color Scheme Generation"
            )
        }
    }
}

@Composable
private fun ColorSettingsPanel(
    paletteLevel: PaletteLevel,
    onPaletteChange: (PaletteLevel) -> Unit,
    contrastLevel: ContrastLevel,
    onContrastChange: (ContrastLevel) -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        LevelSelector(
            prefix        = stringResource(R.string.palette),
            items         = PaletteLevel.entries.map { stringResource(it.stringResource) },
            selectedLevel = paletteLevel.ordinal,
            onUpdateLevel = { onPaletteChange(PaletteLevel.entries[it]) }
        )
        Spacer(Modifier.height(8.dp))
        LevelSelector(
            prefix        = stringResource(R.string.contrast),
            items         = ContrastLevel.entries.map { stringResource(it.stringResource) },
            selectedLevel = contrastLevel.ordinal,
            onUpdateLevel = { onContrastChange(ContrastLevel.entries[it]) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGenerateNewColorSchemeRow(){
    QuizzerAndroidTheme {
        GenerateNewColorScheme()
    }
}