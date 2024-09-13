package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.composables.ColorPicker
import com.asu1.quizzer.composables.Flipper
import com.asu1.quizzer.composables.GetTextStyle
import com.asu1.quizzer.screens.getQuizLayoutState
import com.asu1.quizzer.states.QuizLayoutState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme

val fonts = listOf("Gothic A1", "Noto Sans", "Maruburi", "Spoqahans", "EF-diary", "Ongle-Yunue", "Ongle-Eyeon")
val colors = listOf("Color1", "Color2", "Color3", "Color4", "Color5", "Color6", "Color7", "Color8", "Color9", "Color10")
val borders = listOf("No border", "Underline", "Box")

@Composable
fun QuizLayoutSetTextStyle(quizLayoutState: QuizLayoutState, proceed: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        TextStyleRowOpener(
            quizLayoutState = quizLayoutState,
            targetSelector = 0,
        )
        TextStyleRowOpener(
            quizLayoutState = quizLayoutState,
            targetSelector = 1,
        )
        TextStyleRowOpener(
            quizLayoutState = quizLayoutState,
            targetSelector = 2,
        )
    }
}

@Composable
fun TextStyleRowOpener(
    quizLayoutState: QuizLayoutState,
    targetSelector: Int,
) {
    var isOpen by remember { mutableStateOf(true) }
    val colorScheme by quizLayoutState.colorScheme
    val text = when(targetSelector){
        0 -> "Question"
        1 -> "Answer"
        2 -> "Body"
        else -> "Body"
    }
    val selectedStyle by when(targetSelector){
        0 -> quizLayoutState.questionTextStyle
        1 -> quizLayoutState.bodyTextStyle
        2 -> quizLayoutState.answerTextStyle
        else -> quizLayoutState.bodyTextStyle
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column {
            Button(
                onClick = { isOpen = !isOpen },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GetTextStyle(
                        text = text,
                        style = selectedStyle,
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
                        currentIndex = 0,
                        onNext = {},
                        onPrevious = {}
                    )
                    Flipper(
                        items = colors,
                        currentIndex = 0,
                        onNext = {},
                        onPrevious = {}
                    )
                    Flipper(
                        items = borders,
                        currentIndex = 0,
                        onNext = {},
                        onPrevious = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextStyleRowOpenerPreview(){
    QuizzerAndroidTheme {
        TextStyleRowOpener(
            quizLayoutState = getQuizLayoutState(),
            targetSelector = 0,
        )
    }
}
