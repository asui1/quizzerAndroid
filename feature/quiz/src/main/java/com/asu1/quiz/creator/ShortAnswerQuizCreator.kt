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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.quiz.ui.QuestionTextField
import com.asu1.quiz.viewmodel.quiz.ShortAnswerQuizViewModel
import com.asu1.resources.R

@Composable
fun ShortAnswerQuizCreator(
    quiz: ShortAnswerQuizViewModel,
    onSave: (ShortAnswerQuiz) -> Unit
) {
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    bodyState = quizState.bodyValue,
                    updateBody = { quiz.updateBodyState(it) },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item{
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
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
fun PreviewShortAnswerQuizCreator() {
    com.asu1.resources.QuizzerAndroidTheme {
        ShortAnswerQuizCreator(
            quiz = viewModel(),
            onSave = {}
        )
    }
}
