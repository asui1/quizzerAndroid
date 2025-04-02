package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.asu1.colormodel.ContrastLevel
import com.asu1.colormodel.PaletteLevel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.resources.GenerateWith
import com.asu1.resources.R

@Composable
fun GenerateNewColorScheme(
    isTitleImageSet: Boolean = false,
    generateColorScheme: (QuizCoordinatorActions) -> Unit = { _ -> },
){
    val isDark = isSystemInDarkTheme()
    var openColorSettings by remember {mutableStateOf(false)}
    var paletteLevel by remember { mutableStateOf(PaletteLevel.Fidelity)}
    var contrastLevel by remember { mutableStateOf(ContrastLevel.Default)}

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                "Make New Colors",
                fontWeight = FontWeight.Bold,
            )
            TextButton(
                enabled = isTitleImageSet,
                onClick = {
                    generateColorScheme(QuizCoordinatorActions.GenerateColorScheme(
                        generateWith = GenerateWith.TITLE_IMAGE,
                        palette = paletteLevel,
                        contrast = contrastLevel,
                        isDark = isDark,
                    ))
                },
                colors = ButtonDefaults.buttonColors(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            ) {
                Text(
                    "Image"
                )
            }
            TextButton(
                onClick = {
                    generateColorScheme(QuizCoordinatorActions.GenerateColorScheme(
                        generateWith = GenerateWith.COLOR,
                        palette = paletteLevel,
                        contrast = contrastLevel,
                        isDark = isDark,
                    ))
                },
                colors = ButtonDefaults.buttonColors(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            ) {
                Text(
                    "Color"
                )
            }
            IconButton(
                onClick = {
                    openColorSettings = openColorSettings.not()
                },
                colors = IconButtonDefaults.iconButtonColors(),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Open Edit Row for Color Scheme Generation"
                )
            }
        }
        AnimatedVisibility(
            visible = openColorSettings
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                LevelSelector(
                    prefix = stringResource(R.string.palette),
                    items = PaletteLevel.entries.map { stringResource(it.stringResource) },
                    onUpdateLevel = { level ->
                        paletteLevel = PaletteLevel.entries[level]
                    },
                    selectedLevel = paletteLevel.ordinal,
                )
                LevelSelector(
                    prefix = stringResource(R.string.contrast),
                    items = ContrastLevel.entries.map { stringResource(it.stringResource) },
                    onUpdateLevel = { level ->
                        contrastLevel = ContrastLevel.entries[level]
                    },
                    selectedLevel = contrastLevel.ordinal,
                )
            }
        }
    }
}
