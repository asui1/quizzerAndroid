package com.asu1.quiz.content.quizCommonBuilder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.Quiz
import com.asu1.quiz.content.boxPadding
import com.asu1.quiz.content.quizBodyBuilder.QuizBodyBuilder
import com.asu1.quiz.ui.QuestionTextField
import com.asu1.quiz.viewmodel.quiz.QuizAction

@Composable
fun QuizCreatorBase(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    testTag: String = "QuizCreatorBase",
    onSave: () -> Unit,
    updateQuiz: (QuizAction) -> Unit,
    boxScopeBehind: @Composable () -> Unit = {},
    boxScopeOnTop: @Composable () -> Unit = {},
    content: LazyListScope.() -> Unit
){
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(boxPadding)
            .then(modifier)
    ) {
        boxScopeBehind()
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
        ) {
            item {
                QuestionTextField(
                    question = quiz.question,
                    onQuestionChange =  { updateQuiz(QuizAction.UpdateQuestion(it)) },
                    focusManager = focusManager,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quiz.bodyValue,
                    updateBody = { updateQuiz(QuizAction.UpdateBody(it)) },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            content()
        }
        SaveButton(
            onSave = {
                onSave()
            }
        )
        boxScopeOnTop()
    }
}
