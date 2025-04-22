package com.asu1.quiz.checker

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.sampleReorderQuiz
import com.asu1.quiz.ui.ArrowDownwardWithPaddings
import com.asu1.quiz.ui.SurfaceWithAnswerComposable
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody

@Composable
fun ReorderQuizChecker(
    quiz: ReorderQuiz,
) {
    val result = remember{quiz.gradeQuiz()}
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            AnswerShower(
                isCorrect = result,
                contentAlignment = Alignment.CenterStart
            ){
                QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(16.dp))
            BuildBody(
                quizBody = quiz.bodyValue,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnswerTextStyle.GetTextComposable(quiz.shuffledAnswers[0], modifier = Modifier.fillMaxWidth().padding(8.dp))
        }
        items(quiz.answers.size-1, key = {it}){it ->
            val newIndex = it + 1
            val item = quiz.shuffledAnswers[newIndex].replace(Regex("Q!Z2\\d+$"), "")
            ArrowDownwardWithPaddings()
            AnswerShower(
                isCorrect = quiz.answers[newIndex] == item,
            ) {
                SurfaceWithAnswerComposable(
                    item = item,
                    shadowElevation = 1.dp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReorderQuizChecker(){
    ReorderQuizChecker(
        quiz = sampleReorderQuiz,
    )
}