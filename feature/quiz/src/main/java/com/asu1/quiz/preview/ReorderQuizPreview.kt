package com.asu1.quiz.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.quiz.ui.ArrowDownwardWithPaddings
import com.asu1.quiz.ui.SurfaceWithAnswerComposable
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody

@Composable
fun ReorderQuizPreview(
    quiz: ReorderQuiz,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        BuildBody(
            quizBody = quiz.bodyValue,
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnswerTextStyle.GetTextComposable(quiz.shuffledAnswers[0], modifier = Modifier.fillMaxWidth().padding(8.dp))
        quiz.shuffledAnswers.drop(1).forEach { answer ->
            val item = answer.replace(Regex("Q!Z2\\d+$"), "")
            ArrowDownwardWithPaddings()
            SurfaceWithAnswerComposable(
                item = item,
                shadowElevation = 1.dp,
                )
        }
    }
}