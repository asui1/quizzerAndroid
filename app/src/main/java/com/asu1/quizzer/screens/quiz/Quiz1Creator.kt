package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.ImageGetter
import com.asu1.quizzer.composables.SaveButton
import com.asu1.quizzer.composables.YoutubeLinkInput
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.quizModels.Quiz1ViewModel
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.core.text.isDigitsOnly
import com.asu1.quizzer.composables.QuestionTextFieldWithPoints
import com.asu1.quizzer.composables.QuizBodyBuilder


@Composable
fun Quiz1Creator(
    quiz: Quiz1ViewModel = viewModel(),
    onSave: (Quiz) -> Unit
) {
    val quiz1State by quiz.quiz1State.collectAsState()
    var focusRequesters by remember { mutableStateOf(List(quiz1State.answers.size + 2) { FocusRequester() }) }

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
                    focusRequesters[2].requestFocus()
                },
                focusRequesters = focusRequesters
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quiz1State.bodyType,
                    updateBody = { quiz.updateBodyState(it) },
                    bodyText = quiz1State.bodyText,
                    onBodyTextChange = { quiz.updateBodyText(it) },
                    imageBytes = quiz1State.bodyImage,
                    onImageSelected = { quiz.updateBodyImage(it) },
                    youtubeId = quiz1State.youtubeId,
                    youtubeStartTime = quiz1State.youtubeStartTime,
                    onYoutubeUpdate = { id, time ->
                        quiz.updateBodyYoutube(id, time)
                    },
                    onRemoveBody = {
                        quiz.updateBodyState(BodyType.NONE)
                    }
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
                        focusRequesters = focusRequesters.toMutableList().also {
                            it.removeAt(index+1)
                        }
                        quiz.removeAnswerAt(index)
                    },
                    focusRequester = focusRequesters[index+2],
                    onNext = {
                        if(index == quiz1State.answers.size-1){
                            focusRequesters[0].requestFocus()
                        } else {
                            focusRequesters[index+3].requestFocus()
                        }
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
                        focusRequesters = focusRequesters.toMutableList().also {
                            it.add(FocusRequester())
                        }
                        quiz.addAnswer()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Quiz1AnswerSelection(
                    shuffleValue = quiz1State.shuffleAnswers,
                    onShuffleToggle = { quiz.toggleShuffleAnswers() },
                    maxAnswerSelectionValue = if(quiz1State.maxAnswerSelection == -1) "" else quiz1State.maxAnswerSelection.toString(),
                    maxAnswerSelectionValueChange = { it ->
                        quiz.updateMaxAnswerSelection(it)
                    },
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
        Text("Points")
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Points") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PointSetterPreview() {
    QuizzerAndroidTheme {
        PointSetter()
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    imeAction : ImeAction = ImeAction.Next,
    focusRequester: FocusRequester,
    onNext: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(),
    label: String = "Question",
    key: String = "",
){
    TextField(
        modifier = modifier
            .testTag(key)
            .focusRequester(focusRequester),
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
    focusRequester: FocusRequester,
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
        TextField(
            modifier = Modifier.focusRequester(focusRequester)
                .testTag(key+"TextField"),
            value = value,
            onValueChange = onValueChange,
            label = { Text("Answer") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = if(isLast) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext()
                },
            ),
        )
        IconButton(
            onClick = deleteAnswer,
            modifier = Modifier.align(Alignment.CenterVertically)
                .padding(start = 8.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Answer"
            )
        }
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
            modifier = Modifier.width(200.dp)
                .testTag("QuizCreatorAddAnswerButton")
        ) {
            Text("Add Answer")
        }
    }
}

@Composable
fun Quiz1AnswerSelection(
    shuffleValue: Boolean = false,
    onShuffleToggle: () -> Unit,
    maxAnswerSelectionValue: String,
    maxAnswerSelectionValueChange: (String) -> Unit
){
    var textFieldValue by remember { mutableStateOf(maxAnswerSelectionValue) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(maxAnswerSelectionValue) {
        textFieldValue = maxAnswerSelectionValue
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text("Max. Answer\nSelectable")
        TextField(
            modifier = Modifier.width(60.dp)
                .padding(start = 8.dp)
                .testTag("QuizMaxAnswerSelectionTextField"),
            value = textFieldValue,
            onValueChange = {it ->
                if(it.isEmpty()){
                    textFieldValue = it
                }
                else if(it.isDigitsOnly()){
                    textFieldValue = it
                    maxAnswerSelectionValueChange(it)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    maxAnswerSelectionValueChange(textFieldValue)
                }
            ),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Shuffle\nAnswers?")
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
    QuizzerAndroidTheme {
        Quiz1Creator(
            onSave = {}
        )
    }
}
