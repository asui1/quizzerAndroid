package com.asu1.quiz.creator

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.serializers.QuizType
import com.asu1.quiz.viewmodel.quiz.ConnectItemsQuizViewModel
import com.asu1.quiz.viewmodel.quiz.MultipleChoiceQuizViewModel
import com.asu1.quiz.viewmodel.quiz.DateSelectionQuizViewModel
import com.asu1.quiz.viewmodel.quiz.FillInBlankViewModel
import com.asu1.quiz.viewmodel.quiz.ReorderQuizViewModel
import com.asu1.quiz.viewmodel.quiz.ShortAnswerQuizViewModel
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

    val quiz: Quiz? = if (loadIndex != -1 && quizzes.size > loadIndex
    ) {
        quizzes[loadIndex]
    } else {
        null
    }

    fun onSave(newQuiz: Quiz){
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
                val multipleChoiceQuizViewModel: MultipleChoiceQuizViewModel = viewModel(key="Quiz1ViewModel")
                if(quiz != null){
                    multipleChoiceQuizViewModel.loadQuiz(quiz as MultipleChoiceQuiz)
                }
                MultipleChoiceQuizCreator(
                    quiz = multipleChoiceQuizViewModel,
                    onSave = {
                        onSave(it)
                    },
                )
            }
            QuizType.QUIZ2 -> {
                val dateSelectionQuizViewModel: DateSelectionQuizViewModel = viewModel(key="Quiz2ViewModel")
                if(quiz != null){
                    dateSelectionQuizViewModel.loadQuiz(quiz as DateSelectionQuiz)
                }
                DateSelectionQuizCreator(
                    quiz = dateSelectionQuizViewModel,
                    onSave = {
                        onSave(it)
                    },
                )
            }
            QuizType.QUIZ3 -> {
                val quiz3ViewModel: ReorderQuizViewModel = viewModel(key="Quiz3ViewModel")
                if(quiz != null){
                    quiz3ViewModel.loadQuiz(quiz as ReorderQuiz)
                }
                ReorderQuizCreator(
                    quiz = quiz3ViewModel,
                    onSave = {
                        onSave(it)
                    },
                )
            }
            QuizType.QUIZ4 -> {
                val quiz4ViewModel: ConnectItemsQuizViewModel = viewModel(key="Quiz4ViewModel")
                if(quiz != null){
                    quiz4ViewModel.loadQuiz(quiz as ConnectItemsQuiz)
                }
                ConnectItemsQuizCreator(
                    quiz = quiz4ViewModel,
                    onSave = {
                        onSave(it)
                    },
                )
            }
            QuizType.QUIZ5 -> {
                val shortAnswerQuizViewModel: ShortAnswerQuizViewModel = viewModel(key = "Quiz6ViewModel")
                if(quiz != null){
                    shortAnswerQuizViewModel.loadQuiz(quiz as ShortAnswerQuiz)
                }
                ShortAnswerQuizCreator(
                    quiz = shortAnswerQuizViewModel,
                    onSave = {
                        onSave(it)
                    },
                )
            }
            QuizType.QUIZ6 -> {
                val fillInBlankViewModel: FillInBlankViewModel = viewModel(key = "Quiz5ViewModel")
                if(quiz != null){
                    fillInBlankViewModel.loadQuiz(quiz as FillInBlankQuiz)
                }
                FillInBlankQuizCreator(
                    quiz = fillInBlankViewModel,
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