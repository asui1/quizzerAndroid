package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.QuestionTextFieldWithPoints
import com.asu1.quizzer.composables.QuizBodyBuilder
import com.asu1.quizzer.composables.SaveButton
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.viewModels.quizModels.Quiz3ViewModel

@Composable
fun Quiz3Creator(
    quiz: Quiz3ViewModel = viewModel(),
    onSave: (Quiz) -> Unit
){
    //TODO : 텍스트 필드들에 대한 키보드 타입 설정.

    val quiz3State by quiz.quiz3State.collectAsState()
    var focusRequesters by remember { mutableStateOf(List(quiz3State.answers.size + 2) { FocusRequester() }) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                QuestionTextFieldWithPoints(
                    question = quiz3State.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    point = quiz3State.point,
                    onPointChange = { quiz.setPoint(it) },
                    onNext = {
                        focusRequesters[2].requestFocus()
                    },
                    focusRequesters = focusRequesters
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                QuizBodyBuilder(
                    bodyState = quiz3State.bodyType,
                    updateBody = { quiz.updateBodyState(it) },
                    bodyText = quiz3State.bodyText,
                    onBodyTextChange = { quiz.updateBodyText(it) },
                    imageBytes = quiz3State.bodyImage,
                    onImageSelected = { quiz.updateBodyImage(it) },
                    youtubeId = quiz3State.youtubeId,
                    youtubeStartTime = quiz3State.youtubeStartTime,
                    onYoutubeUpdate = { id, time ->
                        quiz.updateBodyYoutube(id, time)
                    },
                    onRemoveBody = {
                        quiz.updateBodyState(BodyType.NONE)
                    }
                )
            }
            item{
                TextField(
                    value = quiz3State.answers[0],
                    modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
                        .focusRequester(focusRequesters[2]),
                    onValueChange = { quiz.updateAnswerAt(0, it) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequesters[3].requestFocus()
                        }
                    ),
                )
            }
            items(quiz3State.answers.size-1) { index ->
                val newIndex = index +1
                val isLast = newIndex == quiz3State.answers.size - 1
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Remove answer"
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = quiz3State.answers[newIndex],
                    modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
                        .focusRequester(focusRequesters[newIndex+2]),
                    onValueChange = { quiz.updateAnswerAt(newIndex, it) },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if(focusRequesters.size <= 5){
                                    return@IconButton
                                }
                                focusRequesters = focusRequesters.toMutableList().also {
                                    it.removeAt(newIndex+2)
                                }
                                quiz.removeAnswerAt(newIndex)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Remove answer"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = if(isLast) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if(!isLast){
                                focusRequesters[newIndex+3].requestFocus()
                            }
                        },
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                )
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = {
                        focusRequesters = focusRequesters.toMutableList().also {
                            it.add(FocusRequester())
                        }
                        quiz.addAnswer()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "Add answer"
                    )
                }
            }
        }
        SaveButton {
            onSave(quiz3State)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Quiz3CreatorPreview() {
    Quiz3Creator(
        onSave = {}
    )
}
