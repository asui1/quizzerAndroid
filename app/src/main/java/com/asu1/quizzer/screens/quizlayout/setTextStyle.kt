package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.GetTextStyle
import com.asu1.quizzer.composables.base.Flipper
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.constants.borders
import com.asu1.quizzer.util.constants.colors
import com.asu1.quizzer.util.constants.fonts
import com.asu1.quizzer.util.constants.outlines

@Composable
fun QuizLayoutSetTextStyle(
    questionStyle: List<Int> = listOf(0, 0, 1, 0),
    bodyStyle: List<Int> = listOf(0, 0, 2, 1),
    answerStyle: List<Int> = listOf(0, 0, 0, 2),
    updateStyle: (Int, Int, Boolean) -> Unit = { _, _, _ -> },
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        TextStyleRowOpener(
            textStyle = questionStyle,
            updateTextStyle = updateStyle,
            targetSelector = 0,
            colorScheme = colorScheme,
            initOpen = true,
            key = "setTextStyleQuestion",
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextStyleRowOpener(
            textStyle = bodyStyle,
            updateTextStyle = updateStyle,
            targetSelector = 1,
            colorScheme = colorScheme,
            key = "setTextStyleBody",
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextStyleRowOpener(
            textStyle = answerStyle,
            updateTextStyle = updateStyle,
            targetSelector = 2,
            colorScheme = colorScheme,
            key = "setTextStyleAnswer",
        )
    }
}

@Composable
fun TextStyleRowOpener(
    textStyle: List<Int> = listOf(0, 0, 1, 0, 0),
    updateTextStyle: (Int, Int, Boolean) -> Unit = { _, _, _ -> },
    targetSelector: Int,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    key: String = "",
    initOpen: Boolean = false,
) {
    var isOpen by remember { mutableStateOf(initOpen) }
    val text = when(targetSelector){
        0 -> stringResource(R.string.question)
        1 -> stringResource(R.string.body)
        2 -> stringResource(R.string.answer)
        else -> stringResource(R.string.body)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Button(
                onClick = { isOpen = !isOpen },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .background(color = MaterialTheme.colorScheme.surface),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = ButtonDefaults.outlinedButtonBorder(
                    enabled = true,
                ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorScheme.background),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    GetTextStyle(
                        text = text,
                        style = textStyle,
                        colorScheme = colorScheme,
                    )
                    Icon(
                        imageVector = if(isOpen) Icons.Default.ArrowDropDown
                        else Icons.Default.ArrowDropUp,
                        contentDescription = "Icon to Open Color Picker",
                        tint = colorScheme.onSurface
                    )
                }
            }
            if (isOpen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Flipper(
                        items = fonts,
                        currentIndex = textStyle[0],
                        onNext = {
                            updateTextStyle(targetSelector, 0, true)
                        },
                        onPrevious = {
                            updateTextStyle(targetSelector, 0, false)
                        },
                        key = key+"Font"
                    )
                    Flipper(
                        items = colors,
                        currentIndex = textStyle[1],
                        onNext = {
                            updateTextStyle(targetSelector, 1, true)
                        },
                        onPrevious = {
                            updateTextStyle(targetSelector, 1, false)
                        },
                        key = key+"Color"
                    )
                    Flipper(
                        items = borders,
                        currentIndex = textStyle[2],
                        onNext = {
                            updateTextStyle(targetSelector, 2, true)
                        },
                        onPrevious = {
                            updateTextStyle(targetSelector, 2, false)
                        },
                        key = key+"Border"
                    )
                    Flipper(
                        items = outlines,
                        currentIndex = textStyle[4],
                        onNext = {
                            updateTextStyle(targetSelector, 4, true)
                        },
                        onPrevious = {
                            updateTextStyle(targetSelector, 4, false)
                        },
                        key = key+"Outline"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextStyleRowOpenerClosedPreview(){
    TextStyleRowOpener(
        targetSelector = 0,
    )
}
@Preview(showBackground = true)
@Composable
fun TextStyleRowOpenerOpenPreview(){
    TextStyleRowOpener(
        targetSelector = 0,
        initOpen = true,
    )
}
