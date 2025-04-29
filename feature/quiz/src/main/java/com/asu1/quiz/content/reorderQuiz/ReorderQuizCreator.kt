package com.asu1.quiz.content.reorderQuiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.customComposable.textField.TextFieldWithDelete
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.quiz.content.multipleChoiceQuiz.AddAnswer
import com.asu1.quiz.content.quizBodyBuilder.QuizBodyBuilder
import com.asu1.quiz.content.quizCommonBuilder.SaveButton
import com.asu1.quiz.ui.QuestionTextField
import com.asu1.quiz.viewmodel.quiz.ReorderQuizViewModel
import com.asu1.resources.R

@Composable
fun ReorderQuizCreator(
    quiz: ReorderQuizViewModel = viewModel(),
    onSave: (ReorderQuiz) -> Unit
){
    val quiz3State by quiz.quizState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                QuestionTextField(
                    question = quiz3State.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    focusManager = focusManager
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quiz3State.bodyValue,
                    updateBody = { quiz.updateBodyState(it) },
                )
            }
            itemsIndexed(quiz3State.answers) { index, item ->
                val isLast = index == quiz3State.answers.size - 1
                TextFieldWithDelete(
                    label = stringResource(R.string.answer_label),
                    value = quiz3State.answers[index],
                    modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
                        .testTag("QuizAnswerTextField${index}"),
                    onValueChange = { quiz.updateAnswerAt(index, it) },
                    isLast = isLast,
                    onNext = {
                        if(!isLast){
                            focusManager.moveFocus(FocusDirection.Down)
                        }else{
                            focusManager.clearFocus()
                        }
                    },
                    deleteAnswer = {
                        quiz.removeAnswerAt(index)
                    }
                )
                if(!isLast){
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Remove answer"
                    )
                }
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
                AddAnswer(
                    onClick = {
                        quiz.addAnswer()
                    }
                )
            }
        }
        SaveButton {
            onSave(quiz3State)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewReorderQuizCreator() {
    ReorderQuizCreator(
        onSave = {}
    )
}
