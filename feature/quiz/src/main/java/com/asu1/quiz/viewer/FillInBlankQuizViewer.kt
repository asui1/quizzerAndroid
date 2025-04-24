package com.asu1.quiz.viewer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.sampleFillInBlankQuiz
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun FillInBlankQuizViewer(
    quiz: FillInBlankQuiz,
    updateUserAnswer: (List<String>) -> Unit,
){
    val userAnswers = remember { mutableStateListOf(*quiz.userAnswers.toTypedArray()) }
    val focusManager = LocalFocusManager.current

    DisposableEffect(Unit) {
        onDispose {
            updateUserAnswer(userAnswers)
        }
    }

    LazyColumn(
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
        }
        item{
            AnswerTextStyle.GetTextComposable(
                quiz.rawText, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        itemsIndexed(userAnswers) { index, item ->
            val isLast = userAnswers.size-1 == index
            OutlinedTextField(
                modifier = AnswerTextStyle.borderModifier.fillMaxWidth(),
                value = item,
                onValueChange = { userAnswers[index] = item },
                label = { Text("${stringResource(R.string.answer_label)} ${index+1}", fontSize = 12.sp) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = if(isLast) ImeAction.Next else ImeAction.Done
                ),
                textStyle = AnswerTextStyle.textStyle,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
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
fun PreviewFillInBlankQuizViewer(){
    QuizzerAndroidTheme {
        FillInBlankQuizViewer(
            sampleFillInBlankQuiz
        ) { }
    }
}