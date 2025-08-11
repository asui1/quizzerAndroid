package com.asu1.quiz.content.fillInBlankQuiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.quiz.content.quizCommonBuilder.AnswerShower
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.resources.R

@Composable
fun FillInBlankQuizBody(
    quiz: FillInBlankQuiz,
    focusManager: FocusManager,
    enabled: Boolean = true,
    markAnswers: Boolean = false,
){
    AnswerTextStyle.GetTextComposable(
        quiz.rawText, modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    quiz.userAnswers.forEachIndexed { index, item ->
        val isLast = quiz.userAnswers.size-1 == index
        if(markAnswers){
            AnswerShower(
                isCorrect = quiz.userAnswers[index] == quiz.correctAnswers[index],
                showChecker = true,
            ) {
                FillInBlankTextField(
                    item = item,
                    enabled = false,
                    onValueChange = { quiz.userAnswers[index] = it },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = if (isLast) ImeAction.Done else ImeAction.Next
                    ),
                    label = "${stringResource(R.string.answer_label)} ${index + 1}",
                )
            }
        }else{
            FillInBlankTextField(
                item = item,
                enabled = enabled,
                onValueChange = { quiz.userAnswers[index] = it },
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = if(isLast) ImeAction.Done else ImeAction.Next
                ),
                label = "${stringResource(R.string.answer_label)} ${index+1}",
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun FillInBlankTextField(
    item: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    label: String,
){
    OutlinedTextField(
        modifier = AnswerTextStyle.borderModifier.fillMaxWidth(0.8f),
        value = item,
        onValueChange = onValueChange,
        enabled = enabled,
        label = { Text(label, fontSize = 12.sp) },
        keyboardOptions = keyboardOptions,
        textStyle = AnswerTextStyle.textStyle,
        keyboardActions = keyboardActions,
    )
}
