package com.asu1.quiz.content.reorderQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.sampleReorderQuiz
import com.asu1.quiz.content.quizCommonBuilder.AnswerShower
import com.asu1.quiz.content.quizCommonBuilder.QuizViewerBase
import com.asu1.quiz.content.QuizMode
import kotlin.text.replace

@Composable
fun ReorderQuizChecker(
    quiz: ReorderQuiz,
) {
    QuizViewerBase(
        quiz = quiz,
        mode = QuizMode.Checker
    ) {
        quiz.shuffledAnswers.forEachIndexed { index, item ->
            val item = quiz.shuffledAnswers[index].replace(Regex("Q!Z2\\d+$"), "")
            AnswerShower(
                isCorrect = quiz.answers[index] == item,
            ) {
                SurfaceWithAnswerComposable(
                    item = item,
                )
            }
            if(index != quiz.shuffledAnswers.size -1){
                ArrowDownwardWithPaddings()
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