package com.asu1.quiz.content.fillInBlankQuiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.customComposable.textField.TextFieldWithDelete
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.sampleFillInBlankQuiz
import com.asu1.quiz.content.quizCommonBuilder.AddAnswer
import com.asu1.quiz.content.quizCommonBuilder.QuizCreatorBase
import com.asu1.quiz.viewmodel.quiz.FillInBlankViewModel
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun FillInBlankQuizCreator(
    quiz: FillInBlankViewModel,
    onSave: (FillInBlankQuiz) -> Unit
) {
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(quizState.rawText) {
        if (quizState.rawText != textFieldValue.text) {
            if (textFieldValue.composition == null) {
                textFieldValue = textFieldValue.copy(
                    text      = quizState.rawText,
                    selection = TextRange(quizState.rawText.length)
                )
            }
        }
    }

    QuizCreatorBase(
        quiz = quizState,
        testTag = "MultipleChoiceQuizCreatorLazyColumn",
        onSave = { onSave(quizState) },
        focusManager = focusManager,
        updateQuestion = { it -> quiz.updateQuestion(it) },
        updateBodyState = { it -> quiz.updateBodyState(it) },
    ) {
        item{
            FillInBlankField(
                textFieldValue = textFieldValue,
                onValueChange = { newTfv: TextFieldValue ->
                    textFieldValue = newTfv
                    quiz.updateRawText(newTfv.text)
                },
                focusRequester = focusRequester,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        itemsIndexed(quizState.correctAnswers) { index, item ->
            TextFieldWithDelete(
                modifier = Modifier
                    .testTag("FillInBlankQuizTextFieldAnswer$index")
                    .fillMaxWidth(0.8f),
                value = item,
                onValueChange = {it ->
                    quiz.updateCorrectAnswer(index, it)
                },
                label = "${stringResource(R. string. answer_label)} ${index+1}",
                isLast = index == quizState.correctAnswers.size-1,
                onNext = {
                    if(index == quizState.correctAnswers.size-1){
                        focusManager.clearFocus()
                    }else{
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                },
                deleteAnswer = {
                    quiz.deleteCorrectAnswer(index)
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
            AddAnswer {
                quiz.addAnswer()
            }
        }
    }
}

@Composable
fun FillInBlankField(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
) {
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = onValueChange,
        modifier = Modifier
            .testTag("FillInBlankQuizTextFieldBody")
            .fillMaxWidth()
            .focusRequester(focusRequester),
        label = { Text("Question") }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFillInBlankQuizCreator() {
    val fillInBlankViewModel: FillInBlankViewModel = viewModel()
    fillInBlankViewModel.loadQuiz(sampleFillInBlankQuiz)
    QuizzerAndroidTheme {
        FillInBlankQuizCreator(
            quiz = fillInBlankViewModel,
            onSave = {}
        )
    }
}
