package com.asu1.quiz.creator

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.Quiz3
import com.asu1.models.quiz.Quiz4
import com.asu1.models.quiz.Quiz5
import com.asu1.models.serializers.QuizType
import com.asu1.quiz.viewmodel.quiz.Quiz1ViewModel
import com.asu1.quiz.viewmodel.quiz.Quiz2ViewModel
import com.asu1.quiz.viewmodel.quiz.Quiz3ViewModel
import com.asu1.quiz.viewmodel.quiz.Quiz4ViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel


@Composable
fun QuizCaller(
    navController: NavHostController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel(),
    loadIndex:Int,
    quizType: QuizType,
    insertIndex: Int,
) {
    val quizState by quizCoordinatorViewModel.quizUIState.collectAsStateWithLifecycle()
    val quizTheme = quizState.quizTheme
    val quizzes = quizState.quizContentState.quizzes

    val quiz: Quiz<*>? = if (loadIndex != -1 && quizzes.size > loadIndex
    ) {
        quizzes[loadIndex]
    } else {
        null
    }

    fun onSave(newQuiz: Quiz<*>){
        newQuiz.initViewState()
        if(quiz != null){
            quizCoordinatorViewModel.updateQuizCoordinator(
                QuizCoordinatorActions.UpdateQuizAt(newQuiz, loadIndex)
            )
        }
        else{
            quizCoordinatorViewModel.updateQuizCoordinator(
                QuizCoordinatorActions.AddQuizAt(newQuiz, insertIndex)
            )
        }
        navController.popBackStack()
    }

    MaterialTheme(
        colorScheme = quizTheme.colorScheme
    ) {
        when(quizType){
            QuizType.QUIZ1 -> {
                val quiz1ViewModel: Quiz1ViewModel = viewModel(key="Quiz1ViewModel")
                if(quiz != null){
                    quiz1ViewModel.loadQuiz(quiz as Quiz1)
                }
                Quiz1Creator(
                    quiz = quiz1ViewModel,
                    onSave = {
                        onSave(it)
                    },
                )
            }
            QuizType.QUIZ2 -> {
                val quiz2ViewModel: Quiz2ViewModel = viewModel(key="Quiz2ViewModel")
                if(quiz != null){
                    quiz2ViewModel.loadQuiz(quiz as Quiz2)
                }
                Quiz2Creator(
                    quiz = quiz2ViewModel,
                    onSave = {
                        onSave(it)
                    },
                )
            }
            QuizType.QUIZ3 -> {
                val quiz3ViewModel: Quiz3ViewModel = viewModel(key="Quiz3ViewModel")
                if(quiz != null){
                    quiz3ViewModel.loadQuiz(quiz as Quiz3)
                }
                Quiz3Creator(
                    quiz = quiz3ViewModel,
                    onSave = {
                        onSave(it)
                    },
                )
            }
            QuizType.QUIZ4 -> {
                val quiz4ViewModel: Quiz4ViewModel = viewModel(key="Quiz4ViewModel")
                if(quiz != null){
                    quiz4ViewModel.loadQuiz(quiz as Quiz4)
                }
                Quiz4Creator(
                    quiz = quiz4ViewModel,
                    onSave = {
                        onSave(it)
                    },
                )
            }
            QuizType.Quiz5 -> {
                val quiz5ViewModel: Quiz5ViewModel = viewModel(key = "Quiz5ViewModel")
                if(quiz != null){
                    quiz5ViewModel.loadQuiz(quiz as Quiz5)
                }
                Quiz4Creator(
                    quiz = quiz5ViewModel,
                    onSave = {
                        onSave(it)
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
        loadIndex = -1,
        quizType = QuizType.QUIZ1,
        insertIndex = 0,
        navController = rememberNavController()
    )
}