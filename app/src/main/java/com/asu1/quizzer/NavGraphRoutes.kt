package com.asu1.quizzer

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
import com.asu1.quiz.content.quizCommonBuilder.QuizBuilderScreen
import com.asu1.quiz.content.quizCommonBuilder.QuizCaller
import com.asu1.quiz.content.quizCommonBuilder.QuizChecker
import com.asu1.quiz.content.quizCommonBuilder.QuizSolver
import com.asu1.quiz.layoutBuilder.QuizLayoutBuilderScreen
import com.asu1.quiz.scorecard.DesignScoreCardScreen
import com.asu1.quiz.scorecard.ScoringScreen
import com.asu1.quizcard.quizLoad.LoadLocalQuizScreen
import com.asu1.quizcard.quizLoad.LoadMyQuizScreen
import com.asu1.search.SearchScreen
import com.asu1.splashpage.InitializationScreen

@Composable
fun QuizNavGraph(
    navController: NavHostController,
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
        quizBuilderRoute(navController)
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
) {
    composable<Route.QuizBuilder>(
        enterTransition = enterFadeInTransition(),
        exitTransition = exitFadeOutTransition(),
        popEnterTransition = enterFadeInTransition(),
        popExitTransition = exitFadeOutTransition(),
    ) {
        QuizBuilderScreen(
            navController = navController
        )
    }
}

fun NavGraphBuilder.quizCallerRoute(
    navController: NavController
) {
    composable<Route.QuizCaller> { backStackEntry ->
        val args = backStackEntry.toRoute<Route.QuizCaller>()
        QuizCaller(
            navController = navController,
            loadIndex = args.loadIndex,
            quizType = args.quizType,
            insertIndex = args.insertIndex
        )
    }
}

fun NavGraphBuilder.quizSolverRoute(
    navController: NavController,
) {
    composable<Route.QuizSolver> {
        val currentEntry by navController.currentBackStackEntryAsState()
        val hasVisited = remember(currentEntry) {
            hasVisitedRoute(navController, Route.QuizBuilder)
        }
        QuizSolver(
            navController = navController,
            hasVisitedRoute = hasVisited,
        )
    }
}

fun NavGraphBuilder.designScoreCardRoute(
    navController: NavController,
) {
    composable<Route.DesignScoreCard> {
        DesignScoreCardScreen(
            navController = navController
        )
    }
}

fun NavGraphBuilder.loadLocalQuizRoute(
    navController: NavController,
) {
    composable<Route.LoadLocalQuiz> {
        LoadLocalQuizScreen(
            navController = navController,
        )
    }
}

fun NavGraphBuilder.loadUserQuizRoute(
    navController: NavController,
) {
    composable<Route.LoadUserQuiz> {
        LoadMyQuizScreen(
            navController = navController,
        )
    }
}

fun NavGraphBuilder.myActivitiesRoute(
    navController: NavController,
) {
    composable<Route.MyActivities> {
        MyActivitiesScreen(
            navController = navController,
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
) {
    composable<Route.QuizChecker> {
        QuizChecker()
    }
}

fun NavGraphBuilder.scoringScreenRoute(
    navController: NavController,
    loadQuiz: (String) -> Unit
) {
    composable<Route.ScoringScreen> {
        ScoringScreen(
            navController = navController,
            loadQuiz = { quizId -> loadQuiz(quizId) }
        )
    }
}

