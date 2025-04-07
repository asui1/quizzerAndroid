package com.asu1.quiz.preview

import androidx.compose.runtime.Composable
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.Quiz3
import com.asu1.models.quiz.Quiz4

@Composable
fun QuizPreview(
    quiz: Quiz<*>,
) {
    when(quiz){
        is Quiz1 -> {
            Quiz1Preview(quiz)
        }
        is Quiz2 -> {
            Quiz2Preview(quiz)
        }
        is Quiz3 -> {
            Quiz3Preview(quiz)
        }
        is Quiz4 -> {
            Quiz4Preview(quiz)
        }
    }
}