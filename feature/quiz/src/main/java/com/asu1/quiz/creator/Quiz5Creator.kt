package com.asu1.quiz.creator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz5
import com.asu1.quiz.ui.QuestionTextField
import com.asu1.quiz.viewmodel.quiz.FillInBlankViewModel
import com.asu1.resources.R

@Composable
fun Quiz5Creator(
    quiz: FillInBlankViewModel,
    onSave: (Quiz<Quiz5>) -> Unit
) {
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("Quiz5CreatorLazyColumn")
        ) {
            item {
                QuestionTextField(
                    question = quizState.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    focusManager = focusManager,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quizState.bodyType,
                    updateBody = { quiz.updateBodyState(it) },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item { ->
                OutlinedTextField(
                    value = quizState.answer,
                    modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
                        .testTag("QuizAnswerTextField0"),
                    label = { Text(stringResource(R.string.answer_label), fontSize = 12.sp) },
                    onValueChange = { quiz.updateAnswerAt(0, it) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        SaveButton(
            onSave = {
                onSave(
                    quizState
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz5Preview() {
    com.asu1.resources.QuizzerAndroidTheme {
        Quiz5Creator(
            quiz = viewModel(),
            onSave = {}
        )
    }
}
