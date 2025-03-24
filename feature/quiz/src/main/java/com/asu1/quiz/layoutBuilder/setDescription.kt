package com.asu1.quiz.layoutBuilder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.utils.Logger

@Composable
fun QuizLayoutSetDescription(
    modifier: Modifier = Modifier,
    quizDescription: String = "",
    onDescriptionUpdate: (String) -> Unit = {},
    proceed: () -> Unit = {},
    enabled: Boolean = true,
    ) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = quizDescription)) }
    val sizeLimit = 200

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.enter_quiz_description),
            style = QuizzerTypographyDefaults.quizzerTitleMediumMedium,
        )
        TextField(
            value = textFieldValue,
            onValueChange = {
                if (it.text.length <= sizeLimit) {
                    textFieldValue = it
                    onDescriptionUpdate(it.text)
                }
            },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("QuizLayoutBuilderDescriptionTextField")
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    proceed()
                }
            ),
            minLines = 3,
            maxLines = 7,
            supportingText = { Text(text = buildString {
                append(stringResource(R.string.length))
                append("${textFieldValue.text.length}/$sizeLimit")
            }) }
        )
    }

    LaunchedEffect(enabled) {
        if(enabled){
            Logger.debug("Requesting focus for description")
            focusRequester.requestFocus()
            textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
            keyboardController?.show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutDescriptionPreview() {
    QuizzerAndroidTheme {
        QuizLayoutSetDescription(
        )
    }
}