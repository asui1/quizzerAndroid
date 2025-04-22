package com.asu1.quiz.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.asu1.quiz.ui.QuestionTextField
import com.asu1.quiz.viewmodel.quiz.MultipleChoiceQuizViewModel
import com.asu1.resources.R


@Composable
fun MultipleChoiceQuizCreator(
    quiz: MultipleChoiceQuizViewModel,
    onSave: (MultipleChoiceQuiz) -> Unit
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
                .testTag("Quiz1CreatorLazyColumn")
        ) {
            item {
                QuestionTextField(
                    question = quiz1State.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    focusManager = focusManager,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quiz1State.bodyValue,
                    updateBody = { quiz.updateBodyState(it) },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(quiz1State.options.size) { index ->
                AnswerTextField(
                    value = quiz1State.options[index],
                    onValueChange = {it ->  quiz.updateAnswerAt(index, it) },
                    answerCheck = quiz1State.correctFlags[index],
                    toggleAnswer = {
                        quiz.toggleAnsAt(index)
                    },
                    deleteAnswer = {
                        quiz.removeAnswerAt(index)
                    },
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    isLast = index == quiz1State.options.size-1,
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
                Quiz1AnswerShuffle(
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
            label = stringResource(R.string.answer_label),
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
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .testTag("QuizCreatorAddAnswerButton")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.add_answer),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun Quiz1AnswerShuffle(
    shuffleValue: Boolean = false,
    onShuffleToggle: () -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
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
    com.asu1.resources.QuizzerAndroidTheme {
        MultipleChoiceQuizCreator(
            quiz = viewModel(),
            onSave = {}
        )
    }
}
