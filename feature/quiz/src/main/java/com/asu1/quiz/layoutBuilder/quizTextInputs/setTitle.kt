package com.asu1.quiz.layoutBuilder.quizTextInputs

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
private fun TitleLabel() {
    Text(
        text = stringResource(R.string.enter_quiz_title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun TitleInputField(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    proceed: () -> Unit,
    enabled: Boolean
) {
    var localError by remember { mutableStateOf(false) }
    val titleSizeLimit = 50

    TextField(
        value = textFieldValue,
        onValueChange = {
            localError = false
            if (it.text.length <= titleSizeLimit) {
                onValueChange(it)
            }
        },
        enabled = enabled,
        isError = localError,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .testTag("QuizLayoutTitleTextField"),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                if (textFieldValue.text.isBlank()) {
                    localError = true
                } else {
                    proceed()
                }
            }
        ),
        supportingText = {
            if (localError) {
                Text(text = stringResource(R.string.quiz_title_cannot_be_empty))
            } else {
                Text(text = "${textFieldValue.text.length}/$titleSizeLimit")
            }
        }
    )
}

@Composable
private fun TitleFocusEffect(
    enabled: Boolean,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {
    LaunchedEffect(enabled) {
        if (enabled) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
}

@Composable
fun QuizLayoutTitle(
    modifier: Modifier = Modifier,
    title: String = "",
    onTitleUpdate: (String) -> Unit = {},
    proceed: () -> Unit = {},
    enabled: Boolean = true,
) {
    val focusRequester = remember { FocusRequester() }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = title)) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TitleLabel()
        TitleInputField(
            textFieldValue    = textFieldValue,
            onValueChange     = { tfv ->
                textFieldValue = tfv
                onTitleUpdate(tfv.text)
            },
            focusRequester    = focusRequester,
            proceed           = proceed,
            enabled           = enabled
        )
    }

    TitleFocusEffect(
        enabled            = enabled,
        focusRequester     = focusRequester,
        keyboardController = keyboardController
    )
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutTitlePreview() {
    QuizzerAndroidTheme {
        QuizLayoutTitle(
        )
    }
}