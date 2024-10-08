package com.asu1.quizzer.screens.quiz

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.asu1.quizzer.viewModels.quizModels.Quiz1ViewModel
import com.asu1.quizzer.viewModels.quizModels.Quiz2ViewModel
import com.asu1.quizzer.viewModels.quizModels.Quiz3ViewModel
import com.asu1.quizzer.viewModels.quizModels.Quiz4ViewModel


@Composable
fun QuizCaller(quizLayoutState: QuizLayoutState, loadIndex:Int, quizType: QuizType, insertIndex: Int, navController: NavHostController) {
    val colorScheme by quizLayoutState.colorScheme
    val quiz: Quiz? = if (loadIndex != -1 && quizLayoutState.quizzes.value.size > loadIndex) {
        quizLayoutState.quizzes.value[loadIndex]
    } else {
        null
    }
    MaterialTheme(
        colorScheme = colorScheme
    ) {
        when(quizType){
            QuizType.QUIZ1 -> {
                val quiz1ViewModel: Quiz1ViewModel = viewModel()
                if(quiz != null){
                    quiz1ViewModel.loadQuiz(quiz as Quiz1)
                }
                Quiz1Creator(
                    quiz = quiz1ViewModel,
                    onSave = {
                        navController.popBackStack()
                    },
                )
            }
            QuizType.QUIZ2 -> {
                val quiz2ViewModel: Quiz2ViewModel = viewModel()
                if(quiz != null){
                    quiz2ViewModel.loadQuiz(quiz as Quiz2)
                }
                Quiz2Creator(
                    quiz = quiz2ViewModel,
                    onSave = {
                        navController.popBackStack()
                    },
                )
            }
            QuizType.QUIZ3 -> {
                val quiz3ViewModel: Quiz3ViewModel = viewModel()
                if(quiz != null){
                    quiz3ViewModel.loadQuiz(quiz as Quiz3)
                }
                Quiz3Creator(
                    quiz = quiz3ViewModel,
                    onSave = {
                        navController.popBackStack()
                    },
                )
            }
            QuizType.QUIZ4 -> {
                val quiz4ViewModel: Quiz4ViewModel = viewModel()
                if(quiz != null){
                    quiz4ViewModel.loadQuiz(quiz as Quiz4)
                }
                Quiz4Creator(
                    quiz = quiz4ViewModel,
                    onSave = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCallerPreview() {
    QuizCaller(
        quizLayoutState = getQuizLayoutState(),
        loadIndex = -1,
        quizType = QuizType.QUIZ1,
        insertIndex = 0,
        navController = rememberNavController()
    )
}