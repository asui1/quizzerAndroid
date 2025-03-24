package com.asu1.quiz.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly


@Composable
fun QuestionTextFieldWithPoints(
    question: String = "",
    onQuestionChange: (String) -> Unit = {},
    point: Int = 5,
    onPointChange: (Int) -> Unit = {},
    onNext: () -> Unit = {},
    focusManager: FocusManager,
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(point.toString())) }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        MyTextField(
            value = question,
            onValueChange = {onQuestionChange(it)},
            onNext = {
                focusManager.moveFocus(FocusDirection.Right)
            },
            modifier = Modifier.weight(5f),
            key = "QuizQuestionTextField",
        )
        TextField(
            label = { Text("Points") },
            modifier = Modifier.width(80.dp)
                .align(Alignment.CenterVertically)
                .testTag("QuizPointTextField")
                .padding(start = 8.dp),
            value = textFieldValue,
            maxLines = 1,
            onValueChange = {it ->
                if(it.text.isEmpty()){
                    textFieldValue = it
                }
                else if(it.text.isDigitsOnly()){
                    textFieldValue = it
                    onPointChange(it.text.toInt())
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext()
                }
            ),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun QuestionTextFieldWithPointsPreview() {
    val focusManager = LocalFocusManager.current
    QuestionTextFieldWithPoints(
        focusManager = focusManager
    )
}