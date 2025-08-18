package com.asu1.quiz.content.quizCommonBuilder

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.serializers.QuizType
import com.asu1.quiz.content.connectItemQuiz.ConnectItemsQuizCreator
import com.asu1.quiz.content.dateSelectionQuiz.DateSelectionQuizCreator
import com.asu1.quiz.content.fillInBlankQuiz.FillInBlankQuizCreator
import com.asu1.quiz.content.multipleChoiceQuiz.MultipleChoiceQuizCreator
import com.asu1.quiz.content.reorderQuiz.ReorderQuizCreator
import com.asu1.quiz.content.shortAnswerQuiz.ShortAnswerQuizCreator
import com.asu1.quiz.viewmodel.quiz.ConnectItemsQuizViewModel
import com.asu1.quiz.viewmodel.quiz.DateSelectionQuizViewModel
import com.asu1.quiz.viewmodel.quiz.FillInBlankViewModel
import com.asu1.quiz.viewmodel.quiz.MultipleChoiceQuizViewModel
import com.asu1.quiz.viewmodel.quiz.ReorderQuizViewModel
import com.asu1.quiz.viewmodel.quiz.ShortAnswerQuizViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel


@Composable
fun QuizCaller(
    navController: NavController,
    loadIndex: Int,
    quizType: QuizType,
    insertIndex: Int
) {
    // 1) Shared viewModel & state
    val coordinatorVm: QuizCoordinatorViewModel = hiltViewModel()
    val uiState by coordinatorVm.quizUIState.collectAsStateWithLifecycle()
    val theme   = uiState.quizTheme
    val quizzes = uiState.quizContentState.quizzes

    // 2) Determine existing quiz or null for new
    val existingQuiz: Quiz? = quizzes.getOrNull(loadIndex)

    // 3) onSave handler for both update & add
    val onSave: (Quiz) -> Unit = { newQuiz ->
        newQuiz.initViewState()
        if (existingQuiz != null) {
            coordinatorVm.updateQuizCoordinator(
                QuizCoordinatorActions.UpdateQuizAt(newQuiz, loadIndex)
            )
        } else {
            coordinatorVm.updateQuizCoordinator(
                QuizCoordinatorActions.AddQuizAt(newQuiz, insertIndex)
            )
        }
        navController.popBackStack()
    }

    MaterialTheme(colorScheme = theme.colorScheme) {
        QuizCreatorDispatcher(
            quizType      = quizType,
            existingQuiz  = existingQuiz,
            onSave        = onSave
        )
    }
}

@Composable
private fun QuizCreatorDispatcher(
    quizType: QuizType,
    existingQuiz: Quiz?,
    onSave: (Quiz) -> Unit
) {
    when (quizType) {
        QuizType.QUIZ1 -> {
            val vm: MultipleChoiceQuizViewModel = viewModel(key = "Quiz1ViewModel")
            existingQuiz?.let { vm.loadQuiz(it as MultipleChoiceQuiz) }
            MultipleChoiceQuizCreator(
                quiz = vm,
                onSave = { onSave(it) }
            )
        }

        QuizType.QUIZ2 -> {
            val vm: DateSelectionQuizViewModel = viewModel(key = "Quiz2ViewModel")
            existingQuiz?.let { vm.loadQuiz(it as DateSelectionQuiz) }
            DateSelectionQuizCreator(
                quizVm = vm,
                onSave = { onSave(it) }
            )
        }

        QuizType.QUIZ3 -> {
            val vm: ReorderQuizViewModel = viewModel(key = "Quiz3ViewModel")
            existingQuiz?.let { vm.loadQuiz(it as ReorderQuiz) }
            ReorderQuizCreator(
                quiz = vm,
                onSave = { onSave(it) }
            )
        }

        QuizType.QUIZ4 -> {
            val vm: ConnectItemsQuizViewModel = viewModel(key = "Quiz4ViewModel")
            existingQuiz?.let { vm.loadQuiz(it as ConnectItemsQuiz) }
            ConnectItemsQuizCreator(
                quizVm = vm,
                onSave = { onSave(it) }
            )
        }

        QuizType.QUIZ5 -> {
            val vm: ShortAnswerQuizViewModel = viewModel(key = "Quiz5ViewModel")
            existingQuiz?.let { vm.loadQuiz(it as ShortAnswerQuiz) }
            ShortAnswerQuizCreator(
                quiz = vm,
                onSave = { onSave(it) }
            )
        }

        QuizType.QUIZ6 -> {
            val vm: FillInBlankViewModel = viewModel(key = "Quiz6ViewModel")
            existingQuiz?.let { vm.loadQuiz(it as FillInBlankQuiz) }
            FillInBlankQuizCreator(
                quizVm = vm,
                onSave = { onSave(it) }
            )
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
