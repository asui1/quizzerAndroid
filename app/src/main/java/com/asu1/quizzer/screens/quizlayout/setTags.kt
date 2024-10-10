package com.asu1.quizzer.screens.quizlayout


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.quizzer.composables.TagSetter
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme

@Composable
fun QuizLayoutSetTags(quizTags: Set<String> = emptySet(), onTagChange: (String) -> Unit = {},proceed: () -> Unit = {}) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    TagSetter(
        tags = quizTags,
        onClick = {
            onTagChange(it)
        },
        focusRequester = focusRequester
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutTagPreview() {
    QuizzerAndroidTheme {
        QuizLayoutSetTags(
            quizTags = setOf("Test", "ADMIN", "tag3"),
            onTagChange = {},
            proceed = {}
        )
    }
}