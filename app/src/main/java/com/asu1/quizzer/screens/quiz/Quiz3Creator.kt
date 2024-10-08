package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.SaveButton
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.viewModels.quizModels.Quiz3ViewModel

@Composable
fun Quiz3Creator(
    quiz: Quiz3ViewModel = viewModel(),
    onSave: (Quiz) -> Unit
){
    val quiz3State by quiz.quiz3State.collectAsState()


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
                QuestionTextField(
                    value = quiz3State.question,
                    onValueChange = { quiz.updateQuestion(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item{
                TextField(
                    value = quiz3State.answers[0],
                    onValueChange = { quiz.updateAnswerAt(0, it) },
                )
            }
            items(quiz3State.answers.size-1) { index ->
                val newIndex = index +1
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Remove answer"
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = quiz3State.answers[newIndex],
                    onValueChange = { quiz.updateAnswerAt(newIndex, it) },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                quiz.removeAnswerAt(newIndex)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Remove answer"
                            )
                        }
                    }
                )
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = {
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
