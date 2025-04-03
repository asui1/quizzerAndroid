package com.asu1.quiz.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun QuestionTextField(
    question: String = "",
    onQuestionChange: (String) -> Unit = {},
    focusManager: FocusManager,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        MyTextField(
            value = question,
            onValueChange = {onQuestionChange(it)},
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            modifier = Modifier.weight(5f),
            key = "QuizQuestionTextField",
        )
    }
}


@Preview(showBackground = true)
@Composable
fun QuestionTextFieldWithPointsPreview() {
    val focusManager = LocalFocusManager.current
    QuestionTextField(
        focusManager = focusManager
    )
}