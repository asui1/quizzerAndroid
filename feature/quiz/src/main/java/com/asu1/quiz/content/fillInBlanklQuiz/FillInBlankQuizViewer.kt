package com.asu1.quiz.content.fillInBlanklQuiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.asu1.quiz.content.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun FillInBlankQuizViewer(
    quiz: FillInBlankQuiz,
){
    val focusManager = LocalFocusManager.current

    QuizBase(
        quiz = quiz,
        mode = QuizMode.Viewer
    ){
        AnswerTextStyle.GetTextComposable(
            quiz.rawText, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        quiz.userAnswers.forEachIndexed { index, item ->
            val isLast = quiz.userAnswers.size-1 == index
            OutlinedTextField(
                modifier = AnswerTextStyle.borderModifier.fillMaxWidth(),
                value = item,
                onValueChange = { quiz.userAnswers[index] = item },
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
        )
    }
}