package com.asu1.quiz.layoutBuilder.quizTextInputs


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.customComposable.textField.TagSetter
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun QuizLayoutSetTags(
    modifier: Modifier = Modifier,
    quizTags: Set<String> = emptySet(),
    onTagUpdate: (String) -> Unit = {},
    enabled: Boolean = true,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    TagSetter(
        tags = quizTags,
        onClick = {
            onTagUpdate(it)
        },
        focusRequester = focusRequester,
        modifier = modifier
    )

    LaunchedEffect(enabled) {
        if(enabled){
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutTagPreview() {
    QuizzerAndroidTheme {
        QuizLayoutSetTags(
            quizTags = setOf("Test", "ADMIN", "tag3"),
            onTagUpdate = {}
        )
    }
}
