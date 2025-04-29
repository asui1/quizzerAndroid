package com.asu1.quiz.content.quizCommonBuilder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp

@Composable
fun AnswerShower(
    modifier: Modifier = Modifier,
    isCorrect: Boolean = false,
    showChecker: Boolean = true,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit = {}
) {
    var contentSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        contentAlignment = contentAlignment,
        modifier = Modifier.wrapContentSize()
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentSize().onGloballyPositioned { coordinates ->
                contentSize = coordinates.size
            }
        ){
            content()
        }

        if(showChecker){
            val fontSize = (contentSize.height * 0.33).sp // Adjust the multiplier as needed
            BasicText(
                text = if(isCorrect) "O" else "X",
                style = TextStyle(
                    fontSize = fontSize,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Light
                ),
                modifier = modifier.scale(1.5f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnswerShowerPreview() {
    Column(){
        AnswerShower(
            isCorrect = false,
            content = {
                Checkbox(
                    checked = true,
                    onCheckedChange = {}
                )
            }
        )
        AnswerShower(
            isCorrect = true,
            content = {
                Checkbox(
                    checked = true,
                    onCheckedChange = {}
                )
            }
        )

    }
}