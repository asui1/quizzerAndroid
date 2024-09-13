package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.draggable
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
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.asu1.quizzer.screens.getQuizLayoutState
import com.asu1.quizzer.states.QuizLayoutState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme

//TODO: 버튼 눌렀을 때 열면서 버튼이 body 상단에 위치하도록 올리기.
@Composable
fun QuizLayoutSetColorScheme(quizLayoutState: QuizLayoutState, proceed: () -> Unit) {
    val colorStrings = listOf("Primary Color", "Secondary Color", "Territory Color", "onPrimary Color", "onSecondary Color", "onTerritory Color", "Error Color", "onError Color")
    val colorScheme by remember { mutableStateOf(quizLayoutState.colorScheme.value) }
    val background by remember { mutableStateOf(quizLayoutState.backgroundImage.value) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        GenerateColorScheme(quizLayoutState)
        // BackgroundPickerRowOpener()
        for(i in colorStrings){
            ColorPickerRowOpener(
                text = i,
                imageColor = getColorbyName(colorScheme, i),
                onColorSelected = {
                    quizLayoutState.setColorScheme(i, it)
                },
            )
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
fun GenerateColorScheme(quizLayoutState: QuizLayoutState){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Autorenew, contentDescription = "Previous")
        }
        Text(
            text = "Gen with\nBackground Image",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Autorenew, contentDescription = "Previous")
        }
        Text(
            text = "Gen with\nPrimary Color",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ColorPickerRowOpener(
    text: String,
    imageColor: Color,
    onColorSelected: (Color) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(imageColor) }
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
                        }
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
            quizLayoutState = getQuizLayoutState(),
            proceed = {},
        )
    }
}