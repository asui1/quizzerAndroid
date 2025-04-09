package com.asu1.customComposable.textField

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.resources.R

@Composable
fun TextFieldWithDelete(
    modifier: Modifier = Modifier,
    value: String,
    label: String = stringResource(R.string.answer_label),
    onValueChange: (String) -> Unit = {},
    isLast: Boolean = false,
    onNext: () -> Unit = {},
    deleteAnswer: () -> Unit = {}
){
    OutlinedTextField(
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