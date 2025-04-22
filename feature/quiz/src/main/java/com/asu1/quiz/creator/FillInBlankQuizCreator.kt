package com.asu1.quiz.creator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.quiz.ui.QuestionTextField
import com.asu1.quiz.viewmodel.quiz.FillInBlankViewModel

@Composable
fun FillInBlankQuizCreator(
    quiz: FillInBlankViewModel,
    onSave: (FillInBlankQuiz) -> Unit
) {
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("Quiz5CreatorLazyColumn")
        ) {
            item {
                QuestionTextField(
                    question = quizState.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    focusManager = focusManager,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quizState.bodyValue,
                    updateBody = { quiz.updateBodyState(it) },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        SaveButton(
            onSave = {
                onSave(
                    quizState
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFillInBlankQuizCreator() {
    com.asu1.resources.QuizzerAndroidTheme {
        FillInBlankQuizCreator(
            quiz = viewModel(),
            onSave = {}
        )
    }
}
