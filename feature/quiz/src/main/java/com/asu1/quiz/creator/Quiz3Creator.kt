package com.asu1.quiz.creator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.asu1.customComposable.textField.TextFieldWithDelete
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz3
import com.asu1.quiz.ui.QuestionTextField
import com.asu1.quiz.viewmodel.quiz.Quiz3ViewModel
import com.asu1.resources.R

@Composable
fun Quiz3Creator(
    quiz: Quiz3ViewModel = viewModel(),
    onSave: (Quiz<Quiz3>) -> Unit
){
    val quiz3State by quiz.quizState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                QuestionTextField(
                    question = quiz3State.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    focusManager = focusManager
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quiz3State.bodyType,
                    updateBody = { quiz.updateBodyState(it) },
                )
            }
            item{
                OutlinedTextField(
                    value = quiz3State.answers[0],
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
            }
            items(quiz3State.answers.size-1) { index ->
                val newIndex = index +1
                val isLast = newIndex == quiz3State.answers.size - 1
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Remove answer"
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextFieldWithDelete(
                    label = stringResource(R.string.answer_label),
                    value = quiz3State.answers[newIndex],
                    modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
                        .testTag("QuizAnswerTextField${newIndex}"),
                    onValueChange = { quiz.updateAnswerAt(newIndex, it) },
                    onNext = {
                        if(!isLast){
                            focusManager.moveFocus(FocusDirection.Down)
                        }else{
                            focusManager.clearFocus()
                        }
                    },
                )
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
                AddAnswer(
                    onClick = {
                        quiz.addAnswer()
                    }
                )
            }
        }
        SaveButton {
            onSave(quiz3State)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Quiz3CreatorPreview() {
    Quiz3Creator(
        onSave = {}
    )
}
