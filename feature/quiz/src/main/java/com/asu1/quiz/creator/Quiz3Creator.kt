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
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz3
import com.asu1.quiz.ui.QuestionTextFieldWithPoints
import com.asu1.quiz.viewmodel.quiz.Quiz3ViewModel

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
                QuestionTextFieldWithPoints(
                    question = quiz3State.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    point = quiz3State.point,
                    onPointChange = { quiz.setPoint(it) },
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
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
                TextField(
                    value = quiz3State.answers[0],
                    modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
                        .testTag("QuizAnswerTextField0"),
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
                TextField(
                    value = quiz3State.answers[newIndex],
                    modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
                        .testTag("QuizAnswerTextField${newIndex}"),
                    onValueChange = { quiz.updateAnswerAt(newIndex, it) },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                quiz.removeAnswerAt(newIndex)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Remove answer"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = if(isLast) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if(!isLast){
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        },
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                )
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    modifier = Modifier.testTag("QuizCreatorAddAnswerButton"),
                    onClick = {
                        quiz.addAnswer()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "Add answer"
                    )
                }
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
