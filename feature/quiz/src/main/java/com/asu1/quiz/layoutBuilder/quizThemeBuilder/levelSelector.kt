package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerTypographyDefaults

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
            text = buildAnnotatedString {
                append(prefix)
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) { // Replace with your desired color
                    append(items[selectedLevel])
                }
            },
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

