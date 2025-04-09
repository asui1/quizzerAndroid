package com.asu1.quiz.viewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.Quiz1
import com.asu1.models.sampleQuiz1
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle

@Composable
fun Quiz1Viewer(
    quiz: Quiz1,
    toggleUserAnswer: (Int) -> Unit = {},
)
{
    val userAnswers = remember { mutableStateListOf(*quiz.userAns.toTypedArray()) }
    fun toggleLocalUserAnswers(index: Int){
        userAnswers[index ] = !userAnswers[index]
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
                quizBody = quiz.bodyType,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(userAnswers.size, key = {it}){ index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable {
                    toggleLocalUserAnswers(index)
                    toggleUserAnswer(index)
                }
            ){
                Checkbox(
                    checked = userAnswers[index],
                    onCheckedChange = {
                        toggleLocalUserAnswers(index)
                        toggleUserAnswer(index)
                    },
                    modifier = Modifier
                        .semantics {
                            contentDescription = "Checkbox at $index, and is ${if(userAnswers[index]) "checked" else "unchecked"}"
                        }
                        .scale(1.5f)
                )
                AnswerTextStyle.GetTextComposable(quiz.shuffledAnswers[index])
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



@Preview(showBackground = true)
@Composable
fun Quiz1ViewerPreview()
{
    Quiz1Viewer(
        quiz = sampleQuiz1,
    )
}