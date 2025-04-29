package com.asu1.quiz.content.shortAnswerQuiz

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.resources.R

@Composable
fun ShortAnswerBody(
    userAnswer: String,
    onValueChange: (String) -> Unit,
    onDone: KeyboardActionScope.() -> Unit,
    enabled: Boolean = true,
){
    OutlinedTextField(
        modifier = AnswerTextStyle.borderModifier.fillMaxWidth(0.8f),
        value = userAnswer,
        onValueChange = onValueChange,
        enabled = enabled,
        label = { Text(stringResource(R.string.answer_label), fontSize = 12.sp) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        textStyle = AnswerTextStyle.textStyle,
        keyboardActions = KeyboardActions(
            onDone = onDone
        ),
    )
}