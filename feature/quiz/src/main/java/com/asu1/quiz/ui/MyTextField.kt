package com.asu1.quiz.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.asu1.resources.R

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    imeAction : ImeAction = ImeAction.Next,
    onNext: () -> Unit = {},
    label: String = stringResource(R.string.question_label),
    key: String = "",
){
    TextField(
        modifier = modifier
            .testTag(key),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() },
            onDone = { onNext() },
            onGo = { onNext() },
            onSearch = { onNext() },
            onSend = { onNext() }
        ),
    )
}
