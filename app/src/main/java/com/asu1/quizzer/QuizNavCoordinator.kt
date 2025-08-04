package com.asu1.quizzer

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.asu1.activityNavigation.Route
import com.asu1.mainpage.viewModels.QuizCardMainViewModel
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quizcard.quizLoad.LoadLocalQuizViewModel
import com.asu1.quizcard.quizLoad.LoadMyQuizViewModel
import com.asu1.resources.R

@Suppress("LongParameterList")
class QuizNavCoordinator(
    private val navController: NavHostController,
    private val quizCoordinatorViewModel: QuizCoordinatorViewModel,
    private val quizCardMainViewModel: QuizCardMainViewModel,
    private val userViewModel: UserViewModel,
    private val loadLocalQuizViewModel: LoadLocalQuizViewModel,
    private val loadMyQuizViewModel: LoadMyQuizViewModel,
) {
    fun getQuizResult(resultId: String) {
        if (resultId.isBlank()) return
        quizCoordinatorViewModel.loadQuizResult(resultId)
        navController.navigate(Route.ScoringScreen) {
            popUpTo(Route.Home) { inclusive = false }
            launchSingleTop = true
        }
        quizCardMainViewModel.setLoadResultId(null)
    }

    fun loadQuiz(quizId: String, doPop: Boolean = false) {
        quizCoordinatorViewModel.loadQuiz(quizId)
        navController.navigate(Route.QuizSolver()) {
            if (doPop) popUpTo(Route.Home) { inclusive = false }
            launchSingleTop = true
        }
        quizCardMainViewModel.setLoadQuizId(null)
    }

    fun getHome(fetchData: Boolean = true) {
        quizCardMainViewModel.resetQuizTrends()
        quizCardMainViewModel.resetUserRanks()
        if (fetchData) quizCardMainViewModel.fetchQuizCards()
        navController.navigate(Route.Home) {
            // clearer than popUpTo(0): use start destination or home directly
            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateToCreateQuizLayout() {
        val email = userViewModel.userData.value?.email
        if (email.isNullOrEmpty()) {
            SnackBarManager.showSnackBar(R.string.please_login_first, ToastType.INFO)
            navController.navigate(Route.Login) {
                launchSingleTop = true
            }
            return
        }
        quizCoordinatorViewModel.resetQuizData(email)
        loadLocalQuizViewModel.reset()
        navController.navigate(Route.CreateQuizLayout) {
            launchSingleTop = true
        }
    }

    fun navigateToLoadUserQuiz() {
        val email = userViewModel.userData.value?.email ?: return
        loadMyQuizViewModel.loadUserQuiz(email)
        navController.navigate(Route.LoadUserQuiz) {
            launchSingleTop = true
        }
    }
}
