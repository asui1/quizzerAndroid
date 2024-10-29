package com.asu1.quizzer.composables

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.asu1.quizzer.screens.quiz.MyTextField


@Composable
fun QuestionTextFieldWithPoints(
    question: String = "",
    onQuestionChange: (String) -> Unit = {},
    point: Int = 5,
    onPointChange: (Int) -> Unit = {},
    onNext: () -> Unit = {},
    focusRequesters: List<FocusRequester> = listOf(FocusRequester(), FocusRequester())
) {
    var textFieldValue by remember { mutableStateOf(point.toString()) }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        MyTextField(
            value = question,
            onValueChange = {onQuestionChange(it)},
            focusRequester = focusRequesters[0],
            onNext = {
                focusRequesters[1].requestFocus()
            },
            modifier = Modifier.weight(5f)
        )
        TextField(
            label = { Text("Points") },
            modifier = Modifier.width(80.dp)
                .align(Alignment.CenterVertically)
                .focusRequester(focusRequesters[1])
                .padding(start = 8.dp),
            value = textFieldValue,
            maxLines = 1,
            onValueChange = {it ->
                if(it.isEmpty()){
                    textFieldValue = it
                }
                else if(it.isDigitsOnly()){
                    textFieldValue = it
                    onPointChange(it.toInt())
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
    QuestionTextFieldWithPoints(

    )
}