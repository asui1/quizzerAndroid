package com.asu1.quiz.content.multipleChoiceQuiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.customComposable.textField.TextFieldWithDelete
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.quiz.content.quizCommonBuilder.AddAnswer
import com.asu1.quiz.content.quizCommonBuilder.QuizCreatorBase
import com.asu1.quiz.viewmodel.quiz.MultipleChoiceQuizViewModel
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R


@Composable
fun MultipleChoiceQuizCreator(
    quiz: MultipleChoiceQuizViewModel,
    onSave: (MultipleChoiceQuiz) -> Unit
) {
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    QuizCreatorBase(
        quiz = quizState,
        testTag = "MultipleChoiceQuizCreatorLazyColumn",
        onSave = { onSave(quizState) },
        focusManager = focusManager,
        updateQuestion = { it -> quiz.updateQuestion(it) },
        updateBodyState = { it -> quiz.updateBodyState(it) },
    ) {
        items(quizState.options.size) { index ->
            AnswerTextField(
                value = quizState.options[index],
                onValueChange = {it ->  quiz.updateAnswerAt(index, it) },
                answerCheck = quizState.correctFlags[index],
                toggleAnswer = {
                    quiz.toggleAnsAt(index)
                },
                deleteAnswer = {
                    quiz.removeAnswerAt(index)
                },
                onNext = {
                    if(index == quizState.options.size-1) focusManager.clearFocus()
                    else focusManager.moveFocus(FocusDirection.Down)
                },
                isLast = index == quizState.options.size-1,
                key = "QuizAnswerTextField$index"
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            AddAnswer(
                onClick = {
                    quiz.addAnswer()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            MultipleChoiceAnswerShuffle(
                shuffleValue = quizState.shuffleAnswers,
                onShuffleToggle = { quiz.toggleShuffleAnswers() },
            )
        }
    }
}

@Composable
private fun AnswerTextField(
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
            label = stringResource(R.string.answer_label),
            isLast = isLast,
            onNext = onNext,
            deleteAnswer = deleteAnswer
        )
    }
}

@Composable
private fun MultipleChoiceAnswerShuffle(
    shuffleValue: Boolean = false,
    onShuffleToggle: () -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Shuffle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.shuffle_answers),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(end = 8.dp)
        )
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
        MultipleChoiceQuizCreator(
            quiz = viewModel(),
            onSave = {}
        )
    }
}
