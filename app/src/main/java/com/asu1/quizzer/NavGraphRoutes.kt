package com.asu1.quizzer

import SnackBarManager
import ToastType
import android.content.Context
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.asu1.activityNavigation.Route
import com.asu1.activityNavigation.enterFadeInTransition
import com.asu1.activityNavigation.enterFromRightTransition
import com.asu1.activityNavigation.exitFadeOutTransition
import com.asu1.activityNavigation.exitToRightTransition
import com.asu1.mainpage.screens.LoginScreen
import com.asu1.mainpage.screens.MainScreen
import com.asu1.mainpage.screens.MyActivitiesScreen
import com.asu1.mainpage.screens.NotificationScreen
import com.asu1.mainpage.screens.PrivacyPolicy
import com.asu1.mainpage.screens.RegisterScreen
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.quiz.content.quizCommonBuilder.QuizBuilderScreen
import com.asu1.quiz.content.quizCommonBuilder.QuizCaller
import com.asu1.quiz.content.quizCommonBuilder.QuizChecker
import com.asu1.quiz.content.quizCommonBuilder.QuizSolver
import com.asu1.quiz.layoutBuilder.QuizLayoutBuilderScreen
import com.asu1.quiz.scorecard.DesignScoreCardScreen
import com.asu1.quiz.scorecard.ScoringScreen
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizGeneralViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeViewModel
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModel
import com.asu1.quizcard.quizLoad.LoadLocalQuizScreen
import com.asu1.quizcard.quizLoad.LoadLocalQuizViewModel
import com.asu1.quizcard.quizLoad.LoadMyQuizScreen
import com.asu1.quizcard.quizLoad.LoadMyQuizViewModel
import com.asu1.resources.R
import com.asu1.search.SearchScreen
import com.asu1.splashpage.InitializationScreen

@Composable
fun QuizNavGraph(
    navController: NavHostController,
    context: Context,
    getHome: (Boolean) -> Unit,
    loadQuiz: (String, Boolean) -> Unit,
    getQuizResult: (String) -> Unit,
    navigateToCreateQuizLayout: () -> Unit,
    navigateToLoadUserQuiz: () -> Unit
) {
    NavHost(
        navController    = navController,
        startDestination = Route.Init,
        enterTransition  = { EnterTransition.None },
        exitTransition   = { ExitTransition.None }
    ) {
        initializationRoute(
            getHome = getHome
        )
        homeRoute(
            navController = navController,
            navigateToCreateQuizLayout = navigateToCreateQuizLayout,
            navigateToLoadUserQuiz = navigateToLoadUserQuiz,
            getHome = {getHome(true)},
            loadQuiz = { it -> loadQuiz(it, false)},
            getQuizResult = getQuizResult,
        )
        searchRoute(
            navController = navController,
            loadQuiz = { it -> loadQuiz(it, false) },
        )
        loginRoute(navController)
        privacyPolicyRoute(navController)
        registerRoute(navController)
        createQuizLayoutRoute(navController)
        quizBuilderRoute(navController, context = context)
        quizCallerRoute(navController)
        quizSolverRoute(navController)
        designScoreCardRoute(navController)
        loadLocalQuizRoute(navController)
        loadUserQuizRoute(navController)
        myActivitiesRoute(navController)
        notificationsRoute(navController)
        quizCheckerRoute()
        scoringScreenRoute(navController,
            loadQuiz = {it -> loadQuiz(it, true)}
        )
    }
}

fun NavGraphBuilder.initializationRoute(
    getHome: (fetchData: Boolean) -> Unit
) {
    composable<Route.Init> {
        InitializationScreen(
            navigateToHome = {
                getHome(
                    !BuildConfig.isDebug
                )
            }
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navController: NavController,
    navigateToCreateQuizLayout: () -> Unit,
    navigateToLoadUserQuiz: () -> Unit,
    getHome: () -> Unit,
    loadQuiz: (String) -> Unit,
    getQuizResult: (String) -> Unit
) {
    composable<Route.Home> {
        MainScreen(
            navController = navController,
            navigateTo = { route ->
                when (route) {
                    is Route.CreateQuizLayout -> navigateToCreateQuizLayout()
                    is Route.LoadUserQuiz -> navigateToLoadUserQuiz()
                    is Route.MyActivities -> {
                        navController.navigate(route) { launchSingleTop = true }
                    }
                    else -> navController.navigate(route) { launchSingleTop = true }
                }
            },
            loadQuiz = loadQuiz,
            loadQuizResult = getQuizResult,
            moveHome = getHome
        )
    }
}

fun NavGraphBuilder.searchRoute(
    navController: NavController,
    loadQuiz: (String) -> Unit,
) {
    composable<Route.Search>(
        enterTransition = enterFromRightTransition(),
        exitTransition = exitToRightTransition(),
        popEnterTransition = enterFromRightTransition(),
        popExitTransition = exitToRightTransition(),
    ) {
        SearchScreen(
            navController = navController,
            onQuizClick = loadQuiz
        )
    }
}

fun NavGraphBuilder.loginRoute(
    navController: NavController,
) {
    composable<Route.Login> {
        LoginScreen(
            navController = navController,
        )
    }
}

fun NavGraphBuilder.privacyPolicyRoute(
    navController: NavController
) {
    composable<Route.PrivacyPolicy> {
        PrivacyPolicy(navController)
    }
}

fun NavGraphBuilder.registerRoute(
    navController: NavHostController,
) {
    composable<Route.Register> { backStackEntry ->
        val email = backStackEntry.toRoute<Route.Register>().email
        val profileUri = backStackEntry.toRoute<Route.Register>().profileUri
        if (email.isEmpty() || profileUri == null) return@composable
        RegisterScreen(
            navController = navController,
            email = email,
            profileUri = profileUri
        )
    }
}

fun NavGraphBuilder.createQuizLayoutRoute(
    navController: NavController,
) {
    composable<Route.CreateQuizLayout> {
        QuizLayoutBuilderScreen(
            navController = navController,
            navigateToQuizLoad = {
                navController.navigate(Route.LoadLocalQuiz) { launchSingleTop = true }
            }
        )
    }
}

fun NavGraphBuilder.quizBuilderRoute(
    navController: NavController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel,
    quizGeneralViewModel: QuizGeneralViewModel,
    quizThemeViewModel: QuizThemeViewModel,
    scoreCardViewModel: ScoreCardViewModel,
    loadLocalQuizViewModel: LoadLocalQuizViewModel,
    context: Context,
    userViewModel: UserViewModel,
) {
    composable<Route.QuizBuilder>(
        enterTransition = enterFadeInTransition(),
        exitTransition = exitFadeOutTransition(),
        popEnterTransition = enterFadeInTransition(),
        popExitTransition = exitFadeOutTransition(),
    ) {
        QuizBuilderScreen(
            navController = navController,
            quizCoordinatorViewModel = quizCoordinatorViewModel,
            onMoveToScoringScreen = {
                scoreCardViewModel.updateScoreCard(
                    quizGeneralViewModel.quizGeneralUiState.value.quizData,
                    quizThemeViewModel.quizTheme.value.colorScheme
                )
                navController.navigate(Route.DesignScoreCard) { launchSingleTop = true }
            },
            navigateToQuizLoad = {
                loadLocalQuizViewModel.loadLocalQuiz(
                    context = context,
                    email = userViewModel.userData.value?.email ?: "GUEST"
                )
                navController.navigate(Route.LoadLocalQuiz) { launchSingleTop = true }
            }
        )
    }
}

fun NavGraphBuilder.quizCallerRoute(
    navController: NavController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel
) {
    composable<Route.QuizCaller> { backStackEntry ->
        val args = backStackEntry.toRoute<Route.QuizCaller>()
        QuizCaller(
            navController = navController,
            quizCoordinatorViewModel = quizCoordinatorViewModel,
            loadIndex = args.loadIndex,
            quizType = args.quizType,
            insertIndex = args.insertIndex
        )
    }
}

fun NavGraphBuilder.quizSolverRoute(
    navController: NavController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel,
    userViewModel: UserViewModel
) {
    composable<Route.QuizSolver> {
        QuizSolver(
            navController = navController,
            quizCoordinatorViewModel = quizCoordinatorViewModel,
            navigateToScoreCard = {
                if (hasVisitedRoute(navController, Route.QuizBuilder)) {
                    SnackBarManager.showSnackBar(
                        R.string.can_not_proceed_when_creating_quiz,
                        ToastType.INFO
                    )
                    return@QuizSolver
                }
                quizCoordinatorViewModel.gradeQuiz(
                    userViewModel.userData.value?.email ?: "GUEST"
                ) {
                    navController.navigate(Route.ScoringScreen) {
                        popUpTo(Route.Home) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}

fun NavGraphBuilder.designScoreCardRoute(
    navController: NavController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel,
    loadMyQuizViewModel: LoadMyQuizViewModel,
    scoreCardViewModel: ScoreCardViewModel
) {
    composable<Route.DesignScoreCard> {
        DesignScoreCardScreen(
            navController = navController,
            quizCoordinatorViewModel = quizCoordinatorViewModel,
            onUpload = {
                navController.popBackStack(Route.Home, inclusive = false)
                loadMyQuizViewModel.reset()
                scoreCardViewModel.resetScoreCard()
            }
        )
    }
}

fun NavGraphBuilder.loadLocalQuizRoute(
    navController: NavController,
    loadLocalQuizViewModel: LoadLocalQuizViewModel,
    quizCoordinatorViewModel: QuizCoordinatorViewModel
) {
    composable<Route.LoadLocalQuiz> {
        LoadLocalQuizScreen(
            navController = navController,
            loadLocalQuizViewModel = loadLocalQuizViewModel
        ) { quizData, quizTheme, scoreCard ->
            quizCoordinatorViewModel.loadQuiz(
                quizData = quizData,
                quizTheme = quizTheme,
                scoreCard = scoreCard
            )
            loadLocalQuizViewModel.loadComplete()
        }
    }
}

fun NavGraphBuilder.loadUserQuizRoute(
    navController: NavController,
    loadMyQuizViewModel: LoadMyQuizViewModel,
    userViewModel: UserViewModel
) {
    composable<Route.LoadUserQuiz> {
        LoadMyQuizScreen(
            navController = navController,
            loadMyQuizViewModel = loadMyQuizViewModel,
            email = userViewModel.userData.value?.email ?: ""
        )
    }
}

fun NavGraphBuilder.myActivitiesRoute(
    navController: NavController,
    userViewModel: UserViewModel
) {
    composable<Route.MyActivities> {
        MyActivitiesScreen(
            navController = navController,
            userViewModel = userViewModel
        )
    }
}

fun NavGraphBuilder.notificationsRoute(
    navController: NavController
) {
    composable<Route.Notifications> {
        NotificationScreen(navController)
    }
}

fun NavGraphBuilder.quizCheckerRoute(
    quizCoordinatorViewModel: QuizCoordinatorViewModel
) {
    composable<Route.QuizChecker> {
        QuizChecker(quizCoordinatorViewModel = quizCoordinatorViewModel)
    }
}

fun NavGraphBuilder.scoringScreenRoute(
    navController: NavController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel,
    userViewModel: UserViewModel,
    loadQuiz: (String) -> Unit
) {
    composable<Route.ScoringScreen> {
        ScoringScreen(
            navController = navController,
            quizCoordinatorViewModel = quizCoordinatorViewModel,
            email = userViewModel.userData.value?.email ?: "GUEST",
            loadQuiz = { quizId -> loadQuiz(quizId) }
        )
    }
}

