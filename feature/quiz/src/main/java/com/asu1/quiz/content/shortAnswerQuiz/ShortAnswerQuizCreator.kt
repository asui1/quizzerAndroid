package com.asu1.quiz.content.shortAnswerQuiz

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.quiz.content.quizCommonBuilder.QuizCreatorBase
import com.asu1.quiz.viewmodel.quiz.ShortAnswerQuizViewModel
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun ShortAnswerQuizCreator(
    quiz: ShortAnswerQuizViewModel,
    onSave: (ShortAnswerQuiz) -> Unit
) {
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    QuizCreatorBase(
        quiz = quizState,
        testTag = "ShortAnswerQuizCreatorLazyColumn",
        onSave = { onSave(quizState) },
        updateQuiz = { action -> quiz.onAction(action) },
    ) {
        item{
            OutlinedTextField(
                modifier = Modifier
                    .testTag("ShortAnswerQuizAnswerTextField")
                    .fillMaxWidth(0.8f),
                value = quizState.answer,
                onValueChange = {quiz.updateAnswer(it)},
                label = { Text(stringResource(R.string.answer_label), fontSize = 12.sp) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShortAnswerQuizCreator() {
    QuizzerAndroidTheme {
        ShortAnswerQuizCreator(
            quiz = viewModel(),
            onSave = {}
        )
    }
}
