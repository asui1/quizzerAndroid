package com.asu1.quiz.content.fillInBlankQuiz

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
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
    quizVm: FillInBlankViewModel = viewModel(),
    onSave: (FillInBlankQuiz) -> Unit
) {
    val quizState       by quizVm.quizState.collectAsStateWithLifecycle()
    val focusManager    = LocalFocusManager.current
    val focusRequester  = remember { FocusRequester() }

    // keep textFieldValue in sync with viewModel.rawText
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }
    LaunchedEffect(quizState.rawText) {
        if (quizState.rawText != textFieldValue.text && textFieldValue.composition == null) {
            textFieldValue = textFieldValue.copy(
                text      = quizState.rawText,
                selection = TextRange(quizState.rawText.length)
            )
        }
    }

    QuizCreatorBase(
        quiz            = quizState,
        testTag         = "FillInBlankQuizCreatorLazyColumn",
        onSave          = { onSave(quizState) },
        updateQuiz = { action -> quizVm.onAction(action) },
    ) {
        fillInBlankEditor(
            textFieldValue   = textFieldValue,
            onTextChange     = { tfv ->
                textFieldValue = tfv
                quizVm.updateRawText(tfv.text)
            },
            focusRequester   = focusRequester,
            answers          = quizState.correctAnswers,
            onAnswerChange   = { idx, text -> quizVm.updateCorrectAnswer(idx, text) },
            onAnswerDelete   = quizVm::deleteCorrectAnswer,
            onAddAnswer      = quizVm::addAnswer,
            focusManager     = focusManager
        )
    }
}

private fun LazyListScope.fillInBlankEditor(
    textFieldValue: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    answers: List<String>,
    onAnswerChange: (Int, String) -> Unit,
    onAnswerDelete: (Int) -> Unit,
    onAddAnswer: () -> Unit,
    focusManager: FocusManager
) {
    item {
        FillInBlankField(
            textFieldValue = textFieldValue,
            onValueChange = onTextChange,
            focusRequester = focusRequester
        )
        Spacer(Modifier.height(16.dp))
    }
    itemsIndexed(answers) { index, answer ->
        CorrectAnswerRow(
            index         = index,
            answer        = answer,
            onValueChange = { onAnswerChange(index, it) },
            onDelete      = { onAnswerDelete(index) },
            focusManager  = focusManager
        )
        Spacer(Modifier.height(8.dp))
    }
    item {
        AddAnswer(onClick = onAddAnswer)
    }
}

@Composable
private fun CorrectAnswerRow(
    index: Int,
    answer: String,
    onValueChange: (String) -> Unit,
    onDelete: () -> Unit,
    focusManager: FocusManager
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        TextFieldWithDelete(
            value = answer,
            onValueChange = onValueChange,
            label = "${stringResource(R.string.answer_label)} ${index + 1}",
            isLast = false,
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            deleteAnswer = onDelete,
            modifier = Modifier.weight(1f)
        )
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
            quizVm = fillInBlankViewModel,
            onSave = {}
        )
    }
}
