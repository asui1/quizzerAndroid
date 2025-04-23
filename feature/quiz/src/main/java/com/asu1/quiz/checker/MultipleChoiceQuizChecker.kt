package com.asu1.quiz.checker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody

@Composable
fun MultipleChoiceQuizChecker(
    quiz: MultipleChoiceQuiz,
){
    val result = remember{quiz.gradeQuiz()}
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            AnswerShower(
                isCorrect = result,
                contentAlignment = Alignment.CenterStart
            ){
                QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            BuildBody(
                quizBody = quiz.bodyValue,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(quiz.options.size, key = {it}){ index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                AnswerShower(
                    isCorrect = quiz.correctFlags[index] == quiz.userSelections[index],
                    showChecker = quiz.correctFlags[index] || quiz.userSelections[index]
                ){
                    Checkbox(
                        checked = quiz.userSelections[index],
                        onCheckedChange = {
                        },
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Checkbox at $index, and is ${if(quiz.userSelections[index]) "checked" else "unchecked"}"
                            }
                            .scale(1.5f)
                    )
                }
                AnswerTextStyle.GetTextComposable(quiz.displayedOptions[index])
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMultipleChoiceQuizChecker(){
    MultipleChoiceQuizChecker(
        quiz = sampleMultipleChoiceQuiz,
    )
}