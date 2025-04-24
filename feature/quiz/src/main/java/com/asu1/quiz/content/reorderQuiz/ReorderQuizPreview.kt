package com.asu1.quiz.content.reorderQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.sampleReorderQuiz
import com.asu1.quiz.content.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun ReorderQuizPreview(
    quiz: ReorderQuiz,
) {
    QuizBase(
        quiz = quiz,
        mode = QuizMode.Preview,
    ){
        quiz.shuffledAnswers.forEachIndexed { index, answer ->
            val item = answer.replace(Regex("Q!Z2\\d+$"), "")
            SurfaceWithAnswerComposable(
                item = item,
                shadowElevation = 1.dp,
            )
            if(index != quiz.shuffledAnswers.size -1){
                ArrowDownwardWithPaddings()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReorderQuizPreview(){
    QuizzerAndroidTheme {
        ReorderQuizPreview(sampleReorderQuiz)
    }
}