package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.quizzer.R
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun QuizLayoutTitle(
    title: String = "",
    onTitleUpdate: (String) -> Unit = {},
    proceed: () -> Unit = {},
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember{ FocusRequester() }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = title)) }
    val titleSizeLimit = 50
    val keyboardController = LocalSoftwareKeyboardController.current
    var localError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.enter_quiz_title),
            style = MaterialTheme.typography.titleMedium,
        )
        TextField(
            value = textFieldValue,
            onValueChange = {
                localError = false
                if (it.text.length <= titleSizeLimit) {
                    textFieldValue = it
                    onTitleUpdate(it.text)
                }
            },
            enabled = enabled,
            isError = localError,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .testTag("QuizLayoutTitleTextField"),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    if(textFieldValue.text.isBlank()){
                        localError = true
                    } else if(textFieldValue.text.isNotBlank()) {
                        proceed()
                    }
                }
            ),
            supportingText = {
                if(localError){
                    Text(text = stringResource(R.string.quiz_title_cannot_be_empty))
                }else{
                    Text(text = buildString {
                        append(stringResource(R.string.length))
                        append("${textFieldValue.text.length}/$titleSizeLimit")
                    })
                }
            }
        )
    }

    LaunchedEffect(enabled) {
        if(enabled){
            focusRequester.requestFocus()
            textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
            keyboardController?.show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutTitlePreview() {
    com.asu1.resources.QuizzerAndroidTheme {
        QuizLayoutTitle(
        )
    }
}