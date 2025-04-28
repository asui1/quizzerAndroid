package com.asu1.quiz.content.shortAnswerQuiz

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.sampleShortAnswerQuiz
import com.asu1.quiz.content.AnswerShower
import com.asu1.quiz.content.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun ShortAnswerQuizChecker(
    quiz: ShortAnswerQuiz
) {
    QuizBase(
        quiz = quiz,
        mode = QuizMode.Viewer,
    ) {
        AnswerShower(
            isCorrect = quiz.gradeQuiz()
        ) {
            OutlinedTextField(
                modifier = AnswerTextStyle.borderModifier.fillMaxWidth(0.8f),
                value = quiz.userAnswer,
                enabled = false,
                onValueChange = { },
                label = { Text(stringResource(R.string.answer_label), fontSize = 12.sp) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                textStyle = AnswerTextStyle.textStyle,
                keyboardActions = KeyboardActions(
                    onDone = {
                    }
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShortAnswerQuizChecker(){
    QuizzerAndroidTheme {
        ShortAnswerQuizChecker(
            quiz = sampleShortAnswerQuiz,
        )
    }
}