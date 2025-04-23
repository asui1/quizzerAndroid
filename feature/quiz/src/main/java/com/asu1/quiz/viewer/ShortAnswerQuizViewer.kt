package com.asu1.quiz.viewer

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.sampleShortAnswerQuiz
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun ShortAnswerQuizViewer(
    quiz: ShortAnswerQuiz,
    updateUserAnswer: (String) -> Unit = {},
)
{
    val focusManager = LocalFocusManager.current
    var userAnswer = remember {quiz.userAnswer}

    DisposableEffect(Unit) {
        onDispose {
            updateUserAnswer(userAnswer)
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            QuestionTextStyle.GetTextComposable(
                quiz.question, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            BuildBody(
                quizBody = quiz.bodyValue,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            OutlinedTextField(
                modifier = AnswerTextStyle.borderModifier.fillMaxWidth(0.8f),
                value = userAnswer,
                onValueChange = { userAnswer = it },
                label = { Text(stringResource(R.string.answer_label), fontSize = 12.sp) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                textStyle = AnswerTextStyle.textStyle,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShortAnswerQuizViewer(){
    QuizzerAndroidTheme {
        ShortAnswerQuizViewer(
            quiz = sampleShortAnswerQuiz,
            updateUserAnswer = {},
        )
    }
}