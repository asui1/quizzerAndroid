package com.asu1.quizzer.screens.quiz

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import com.asu1.quizzer.model.QuizType
import com.asu1.quizzer.screens.getQuizLayoutState
import com.asu1.quizzer.states.QuizLayoutState


@Composable
fun QuizCaller(quizLayoutState: QuizLayoutState, loadIndex:Int, quizType: QuizType) {
    val colorScheme by quizLayoutState.colorScheme
    val quiz: Quiz = if (loadIndex != -1 && quizLayoutState.quizzes.value.size > loadIndex) {
        quizLayoutState.quizzes.value[loadIndex]
    } else {
        when(quizType){
            QuizType.QUIZ1 -> Quiz1()
            QuizType.QUIZ2 -> Quiz2()
            QuizType.QUIZ3 -> Quiz3()
            QuizType.QUIZ4 -> Quiz4()
        }
    }
    MaterialTheme(
        colorScheme = colorScheme
    ) {
        when(quizType){
            QuizType.QUIZ1 -> Quiz1Creator(
                quiz = quiz as Quiz1,
                onSave = {},
            )
            QuizType.QUIZ2 -> Quiz2Creator(
                quiz = quiz as Quiz2,
                onSave = {},
            )
            QuizType.QUIZ3 -> Quiz3Creator(
                quiz = quiz as Quiz3,
                onSave = {},
            )
            QuizType.QUIZ4 -> Quiz4Creator(
                quiz = quiz as Quiz4,
                onSave = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCallerPreview() {
    QuizCaller(
        quizLayoutState = getQuizLayoutState(),
        loadIndex = -1,
        quizType = QuizType.QUIZ1
    )
}