package com.asu1.quizzer

import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.asu1.activityNavigation.Route
import com.asu1.activityNavigation.enterFadeInTransition
import com.asu1.activityNavigation.enterFromRightTransition
import com.asu1.activityNavigation.exitFadeOutTransition
import com.asu1.activityNavigation.exitToRightTransition
import com.asu1.mainpage.screens.LoginScreen
import com.asu1.mainpage.screens.MyActivitiesScreen
import com.asu1.mainpage.screens.NotificationScreen
import com.asu1.mainpage.screens.PrivacyPolicy
import com.asu1.mainpage.screens.RegisterScreen
import com.asu1.mainpage.screens.mainScreen.MainScreen
import com.asu1.mainpage.viewModels.QuizCardViewModel
import com.asu1.quiz.content.quizCommonBuilder.QuizBuilderScreen
import com.asu1.quiz.content.quizCommonBuilder.QuizCaller
import com.asu1.quiz.content.quizCommonBuilder.QuizChecker
import com.asu1.quiz.content.quizCommonBuilder.QuizSolver
import com.asu1.quiz.layoutBuilder.QuizLayoutBuilderScreen
import com.asu1.quiz.scorecard.DesignScoreCardScreen
import com.asu1.quiz.scorecard.ScoringScreen
import com.asu1.quiz.viewmodel.LoadLocalQuizViewModel
import com.asu1.quiz.viewmodel.LoadMyQuizViewModel
import com.asu1.quiz.viewmodel.UserViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizContentViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizGeneralViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizResultViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeViewModel
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModel
import com.asu1.quizcard.quizLoad.LoadLocalQuizScreen
import com.asu1.quizcard.quizLoad.LoadMyQuizScreen
import com.asu1.search.SearchScreen
import com.asu1.splashpage.InitializationScreen
import com.asu1.splashpage.InitializationViewModel

@Composable
private fun activityOwner(): ViewModelStoreOwner {
    val context = LocalContext.current
    return remember(context) {
        generateSequence(context) { (it as? ContextWrapper)?.baseContext }
            .filterIsInstance<ComponentActivity>()
            .first()
    }
}

@Composable
fun QuizNavGraphManager(
    navController: NavHostController,
    initializationViewModel: InitializationViewModel,
) {
    val owner = activityOwner()

    val quizCardViewModel: QuizCardViewModel = hiltViewModel(owner)
    val userViewModel: UserViewModel = hiltViewModel(owner)
    val quizCoordinatorViewModel: QuizCoordinatorViewModel = hiltViewModel(owner)
    val quizGeneralViewModel: QuizGeneralViewModel = viewModel(owner)
    val quizContentViewModel: QuizContentViewModel = viewModel(owner)
    val quizThemeViewModel: QuizThemeViewModel = viewModel(owner)
    val quizResultViewModel: QuizResultViewModel = viewModel(owner)
    val scoreCardViewModel: ScoreCardViewModel = viewModel(owner)
    val loadMyQuizViewModel: LoadMyQuizViewModel = hiltViewModel(owner)
    val loadLocalQuizViewModel: LoadLocalQuizViewModel = viewModel(owner)
    val quizNavCoordinator = QuizNavCoordinator(
        navController = navController, quizCoordinatorViewModel = quizCoordinatorViewModel,
        quizCardViewModel = quizCardViewModel, userViewModel = userViewModel,
        loadLocalQuizViewModel = loadLocalQuizViewModel, loadMyQuizViewModel = loadMyQuizViewModel
    )

    quizCoordinatorViewModel.setViewModels(
        quizGeneral     = quizGeneralViewModel, quizTheme       = quizThemeViewModel,
        quizContent     = quizContentViewModel, quizResult      = quizResultViewModel,
        scoreCard       = scoreCardViewModel
    )

    NavHost(
        navController    = navController, startDestination = Route.Init,
        enterTransition  = { EnterTransition.None }, exitTransition   = { ExitTransition.None }
    ) {
        initializationRoute(
            initializationViewModel = initializationViewModel, parentOwner = owner,
            getHome = { fetchData -> quizNavCoordinator.getHome(fetchData) },
        )
        homeRoute(
            navController = navController, quizNavCoordinator = quizNavCoordinator, parentOwner = owner,
        )
        searchRoute(
            navController = navController, loadQuiz = { id -> quizNavCoordinator.loadQuiz(id, false)},
        )
        loginRoute(navController)
        privacyPolicyRoute(navController)
        registerRoute(navController)
        createQuizLayoutRoute(navController, parentOwner = owner,
        )
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
            loadQuiz = { id -> quizNavCoordinator.loadQuiz(id, false)},
        )
    }
}

fun NavGraphBuilder.initializationRoute(
    initializationViewModel: InitializationViewModel,
    getHome: (fetchData: Boolean) -> Unit,
    parentOwner: ViewModelStoreOwner,
) {
    composable<Route.Init> {
        InitializationScreen(
            initializationViewModel = initializationViewModel,
            navigateToHome = {
                getHome(
                    !BuildConfig.isDebug
                )
            },
            parentOwner = parentOwner
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navController: NavController,
    quizNavCoordinator: QuizNavCoordinator,
    parentOwner: ViewModelStoreOwner,
) {
    composable<Route.Home>(
    ) {
        MainScreen(
            navController = navController,
            navigateTo = { route ->
                when (route) {
                    is Route.CreateQuizLayout -> { quizNavCoordinator.navigateToCreateQuizLayout() }
                    is Route.LoadUserQuiz -> { quizNavCoordinator.navigateToLoadUserQuiz() }
                    is Route.MyActivities -> {
                        navController.navigate(route) { launchSingleTop = true }
                    }
                    else -> navController.navigate(route) { launchSingleTop = true }
                }
            },
            loadQuiz = { id -> quizNavCoordinator.loadQuiz(id, false)},
            moveHome = { quizNavCoordinator.getHome(true)},
            parentOwner = parentOwner,
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
    parentOwner: ViewModelStoreOwner,
) {
    composable<Route.CreateQuizLayout> {
        QuizLayoutBuilderScreen(
            navController = navController,
            navigateToQuizLoad = {
                navController.navigate(Route.LoadLocalQuiz) { launchSingleTop = true }
            },
            parentOwner = parentOwner,
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
    composable<Route.QuizSolver>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "https://quizzer.co.kr/?quizId={quizId}"
            }
        )
    ) { backStackEntry ->

    val currentEntry by navController.currentBackStackEntryAsState()

        // Read quizId from deep-link
        val quizId = backStackEntry.arguments?.getString("quizId") ?: ""

        // Screen-scoped ViewModel
        val quizCoordinatorViewModel: QuizCoordinatorViewModel = hiltViewModel()

        // Trigger logic from loadQuiz()
        LaunchedEffect(quizId) {
            if (quizId.isNotBlank()) {
                quizCoordinatorViewModel.loadQuiz(quizId)
            }
        }

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
    composable<Route.ScoringScreen>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "https://quizzer.co.kr/?resultId={resultId}"
            }
        )
    ) { backStackEntry ->

        // Read resultId from deep link
        val resultId = backStackEntry.arguments?.getString("resultId") ?: ""

        // Access screen-scoped ViewModels
        val quizCoordinatorViewModel: QuizCoordinatorViewModel = hiltViewModel()

        LaunchedEffect(resultId) {
            if (resultId.isNotBlank()) {
                quizCoordinatorViewModel.loadQuizResult(resultId)
            }
        }

        ScoringScreen(
            navController = navController,
            loadQuiz = { quizId -> loadQuiz(quizId) }
        )
    }
}
