package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.quizzer.composables.QuestionTextFieldWithPoints
import com.asu1.quizzer.composables.QuizBodyBuilder
import com.asu1.quizzer.composables.SaveButton
import com.asu1.quizzer.composables.TextFieldWithDelete
import com.asu1.quizzer.viewModels.quizModels.Quiz1ViewModel
import com.asu1.resources.R


@Composable
fun Quiz1Creator(
    quiz: Quiz1ViewModel,
    onSave: (Quiz<Quiz1>) -> Unit
) {
    val quiz1State by quiz.quizState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                QuestionTextFieldWithPoints(
                    question = quiz1State.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    point = quiz1State.point,
                    onPointChange = { quiz.setPoint(it) },
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    focusManager = focusManager,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quiz1State.bodyType,
                    updateBody = { quiz.updateBodyState(it) },
                    onBodyTextChange = { quiz.updateBodyText(it) },
                    onImageSelected = { quiz.updateBodyImage(it) },
                    onYoutubeUpdate = { id, time ->
                        quiz.updateBodyYoutube(id, time)
                    },
                )
            }
            items(quiz1State.answers.size) { index ->
                AnswerTextField(
                    value = quiz1State.answers[index],
                    onValueChange = {it ->  quiz.updateAnswerAt(index, it) },
                    answerCheck = quiz1State.ans[index],
                    toggleAnswer = {
                        quiz.toggleAnsAt(index)
                    },
                    deleteAnswer = {
                        quiz.removeAnswerAt(index)
                    },
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    isLast = index == quiz1State.answers.size-1,
                    key = "QuizAnswerTextField$index"
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AddAnswer(
                    onClick = {
                        quiz.addAnswer()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Quiz1AnswerSelection(
                    shuffleValue = quiz1State.shuffleAnswers,
                    onShuffleToggle = { quiz.toggleShuffleAnswers() },
                )
            }
        }
        SaveButton(
            onSave = {
                onSave(
                    quiz1State
                )
            }
        )
    }

}

@Composable
fun PointSetter(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(stringResource(R.string.points))
        TextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.points)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PointSetterPreview() {
    com.asu1.resources.QuizzerAndroidTheme {
        PointSetter()
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    imeAction : ImeAction = ImeAction.Next,
    onNext: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(),
    label: String = "Question",
    key: String = "",
){
    TextField(
        modifier = modifier
            .testTag(key),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() },
            onDone = { onNext() },
            onGo = { onNext() },
            onSearch = { onNext() },
            onSend = { onNext() }
        ),
    )
}




@Composable
fun AnswerTextField(
    value: String,
    onValueChange: (String) -> Unit,
    answerCheck: Boolean,
    toggleAnswer: () -> Unit,
    deleteAnswer: () -> Unit,
    onNext: () -> Unit = {},
    isLast: Boolean = false,
    key: String = "",
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = Modifier.testTag(key+"Checkbox"),
            checked = answerCheck,
            onCheckedChange = {
                toggleAnswer()
            },
        )
        TextFieldWithDelete(
            modifier = Modifier.weight(1f)
                .testTag(key+"TextField"),
            value = value,
            onValueChange = onValueChange,
            label = "Answer",
            isLast = isLast,
            onNext = onNext,
            deleteAnswer = deleteAnswer
        )
    }
}

@Composable
fun AddAnswer(
    onClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ){
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .width(200.dp)
                .testTag("QuizCreatorAddAnswerButton")
        ) {
            Text(stringResource(R.string.add_answer))
        }
    }
}

@Composable
fun Quiz1AnswerSelection(
    shuffleValue: Boolean = false,
    onShuffleToggle: () -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(stringResource(R.string.shuffle_answers))
        Checkbox(
            checked = shuffleValue,
            onCheckedChange = {
                onShuffleToggle()
            }
        )
    }
}



@Preview(showBackground = true)
@Composable
fun Quiz1Preview() {
    com.asu1.resources.QuizzerAndroidTheme {
        Quiz1Creator(
            quiz = viewModel(),
            onSave = {}
        )
    }
}
