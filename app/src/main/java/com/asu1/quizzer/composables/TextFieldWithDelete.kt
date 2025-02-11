package com.asu1.quizzer.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldWithDelete(
    modifier: Modifier = Modifier,
    value: String,
    label: String = "Answer",
    onValueChange: (String) -> Unit = {},
    isLast: Boolean = false,
    onNext: () -> Unit = {},
    deleteAnswer: () -> Unit = {}
){
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = if(isLast) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onNext()
            },
            onDone = {
                onNext()
            }
        ),
        trailingIcon = {
            IconButton(
                onClick = deleteAnswer,
                modifier = Modifier
                    .padding(start = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Answer"
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewTextFieldWithDelete(){
    TextFieldWithDelete(
        value = "Answer 1",
        onValueChange = {},
        deleteAnswer = {}
    )
}