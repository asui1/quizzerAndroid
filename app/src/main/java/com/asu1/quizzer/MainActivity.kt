package com.asu1.quizzer

import ToastManager
import ToastType
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.tracing.Trace
import com.asu1.quizzer.composables.CustomSnackbarHost
import com.asu1.quizzer.screens.mainScreen.InitializationScreen
import com.asu1.quizzer.screens.mainScreen.LoginScreen
import com.asu1.quizzer.screens.mainScreen.MainScreen
import com.asu1.quizzer.screens.mainScreen.PrivacyPolicy
import com.asu1.quizzer.screens.mainScreen.RegisterScreen
import com.asu1.quizzer.screens.mainScreen.SearchScreen
import com.asu1.quizzer.screens.quiz.QuizBuilderScreen
import com.asu1.quizzer.screens.quiz.QuizCaller
import com.asu1.quizzer.screens.quiz.QuizSolver
import com.asu1.quizzer.screens.quiz.ScoringScreen
import com.asu1.quizzer.screens.quizlayout.DesignScoreCardScreen
import com.asu1.quizzer.screens.quizlayout.LoadItems
import com.asu1.quizzer.screens.quizlayout.LoadMyQuiz
import com.asu1.quizzer.screens.quizlayout.QuizLayoutBuilderScreen
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.enterFadeInTransition
import com.asu1.quizzer.util.enterFromRightTransition
import com.asu1.quizzer.util.exitFadeOutTransition
import com.asu1.quizzer.util.exitToRightTransition
import com.asu1.quizzer.viewModels.MusicListViewModel
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.QuizLoadViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.SearchViewModel
import com.asu1.quizzer.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // NEED INITIALIZATION WITH HIGH PRIORITY
    private val quizCardMainViewModel: QuizCardMainViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val musicListViewModel: MusicListViewModel by viewModels()

    // CAN BE INITIALIZED LATER
    private val searchViewModel: SearchViewModel by viewModels()
    private val quizLayoutViewModel: QuizLayoutViewModel by viewModels()
    private val scoreCardViewModel: ScoreCardViewModel by viewModels()
    private val quizLoadViewModel: QuizLoadViewModel by viewModels()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Trace.beginSection("MainActivity_onCreate")
        handleIntent(intent)

        lifecycleScope.launch {
            quizLayoutViewModel.initialize()
        }

        setContent {
            val context = LocalContext.current
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            // Observe ToastManager for Toast messages
            LaunchedEffect(Unit) {
                ToastManager.toastMessage.observe(this@MainActivity) { message ->
                    message?.let {
                        val prefix = when (it.second) {
                            ToastType.SUCCESS -> "S"
                            ToastType.ERROR -> "E"
                            ToastType.INFO -> "I"
                        }
                        snackbarHostState.currentSnackbarData?.dismiss()
                        lifecycleScope.launch {
                            snackbarHostState.showSnackbar(buildString {
                                append(prefix)
                                append(context.getString(it.first))
                            },)
                            ToastManager.toastShown()
                        }
                    }
                }
            }

            navController = rememberNavController()
            QuizzerAndroidTheme {
                Scaffold(
                    snackbarHost = {
                        CustomSnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                        )
                    }
                ) {it ->
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.padding(it).fillMaxSize()
                    ) {
                        val colorScheme = MaterialTheme.colorScheme
                        fun hasVisitedRoute(
                            navController: NavController,
                            route: Route
                        ): Boolean {
                            val previousEntry = navController.previousBackStackEntry
                            return previousEntry?.destination?.route == route::class.qualifiedName
                        }

                        fun getQuizResult(resultId: String = "") {
                            if (resultId.isEmpty()) return
                            quizLayoutViewModel.loadQuizResult(resultId, scoreCardViewModel)
                            navController.navigate(
                                Route.ScoringScreen
                            ){
                                popUpTo(Route.Home) { inclusive = false }
                                launchSingleTop = true
                                quizCardMainViewModel.setLoadResultId(null)
                            }
                        }
                        fun loadQuiz(quizId: String, doPop: Boolean = false) {
                            quizLayoutViewModel.loadQuiz(quizId, scoreCardViewModel)
                            navController.navigate(Route.QuizSolver()){
                                if(doPop) popUpTo(Route.Home) { inclusive = false }
                                launchSingleTop = true
                            }
                            quizCardMainViewModel.setLoadQuizId(null)
                        }

                        fun getHome(fetchData: Boolean = true) {
                            val lang =
                                if (Locale.getDefault().language == "ko") "ko" else "en"
                            val email = userViewModel.userData.value?.email ?: "GUEST"
                            quizCardMainViewModel.resetQuizTrends()
                            quizCardMainViewModel.resetUserRanks()
                            if(fetchData) quizCardMainViewModel.fetchQuizCards(lang, email)
                            navController.navigate(
                                Route.Home
                            ) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                        Trace.endSection()
                        Trace.beginSection("MainActivity_NavHost")
                        NavHost(
                            navController = navController,
                            startDestination = Route.Init,
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None },
                        ) {
                            composable<Route.Init> {
                                InitializationScreen(
                                    navigateToHome = {
                                        getHome(
                                            fetchData = !BuildConfig.isDebug
                                        )
                                    }
                                )
                            }
                            composable<Route.Home> {
                                MainScreen(
                                    navController,
                                    quizCardMainViewModel = quizCardMainViewModel,
                                    userViewModel = userViewModel,
                                    navigateToQuizLayoutBuilder = {
                                        if (userViewModel.userData.value?.email == null) {
                                            ToastManager.showToast(
                                                R.string.please_login_first,
                                                ToastType.INFO
                                            )
                                            navController.navigate(
                                                Route.Login
                                            ){
                                                launchSingleTop = true
                                            }
                                        } else {
                                            quizLayoutViewModel.resetQuizLayout()
                                            quizLayoutViewModel.initQuizLayout(
                                                userViewModel.userData.value?.email,
                                                colorScheme
                                            )
                                            scope.launch {
                                                scoreCardViewModel.resetScoreCard()
                                                quizLoadViewModel.reset()
                                            }
                                            navController.navigate(
                                                Route.CreateQuizLayout
                                            ){
                                                launchSingleTop = true
                                            }
                                        }
                                    },
                                    navigateToMyQuizzes = {
                                        quizLoadViewModel.loadUserQuiz(
                                            userViewModel.userData.value?.email ?: ""
                                        )
                                        navController.navigate(
                                            Route.LoadUserQuiz
                                        ){
                                            launchSingleTop = true
                                        }
                                    },
                                    loadQuiz = { loadQuiz(it) },
                                    loadQuizResult = { getQuizResult(it) },
                                    moveHome = { getHome() }
                                )
                            }
                            composable<Route.Search>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                SearchScreen(
                                    navController, searchViewModel,
                                    onQuizClick = { quizId ->
                                        loadQuiz(quizId)
                                    },
                                )
                            }
                            composable<Route.Login>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                LoginScreen(navController, userViewModel)
                            }
                            composable<Route.PrivacyPolicy>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                PrivacyPolicy(navController)
                            }
                            composable<Route.Register>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) { backStackEntry ->
                                val email =
                                    backStackEntry.toRoute<Route.Register>().email
                                val profileUri =
                                    backStackEntry.toRoute<Route.Register>().profileUri
                                if (email == "" || profileUri == null) {
                                    return@composable
                                }
                                RegisterScreen(
                                    navController = navController,
                                    email = email,
                                    profileUri = profileUri,
                                    login = {
                                        userViewModel.logIn(email, profileUri)
                                    }
                                )
                            }
                            composable<Route.CreateQuizLayout>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                QuizLayoutBuilderScreen(
                                    navController, quizLayoutViewModel,
                                    navigateToQuizLoad = {
                                        quizLoadViewModel.loadLocalQuiz(
                                            context = context,
                                            email = userViewModel.userData.value?.email
                                                ?: "GUEST"
                                        )
                                        navController.navigate(
                                            Route.LoadLocalQuiz
                                        ){
                                            launchSingleTop = true
                                        }
                                    },
                                    scoreCardViewModel = scoreCardViewModel,
                                )
                            }
                            composable<Route.QuizBuilder>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                QuizBuilderScreen(navController, quizLayoutViewModel,
                                    onMoveToScoringScreen = {
                                        scoreCardViewModel.updateScoreCard(
                                            quizLayoutViewModel.quizData.value,
                                            quizLayoutViewModel.quizTheme.value.colorScheme
                                        )
                                        navController.navigate(
                                            Route.DesignScoreCard
                                        ){
                                            launchSingleTop = true
                                        }
                                    },
                                    scoreCardViewModel = scoreCardViewModel,
                                    navigateToQuizLoad = {
                                        quizLoadViewModel.loadLocalQuiz(
                                            context = context,
                                            email = userViewModel.userData.value?.email
                                                ?: "GUEST"
                                        )
                                        navController.navigate(
                                            Route.LoadLocalQuiz
                                        ){
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable<Route.QuizCaller>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) { backStackEntry ->
                                val loadIndex =
                                    backStackEntry.toRoute<Route.QuizCaller>().loadIndex
                                val quizType =
                                    backStackEntry.toRoute<Route.QuizCaller>().quizType
                                val insertIndex =
                                    backStackEntry.toRoute<Route.QuizCaller>().insertIndex
                                QuizCaller(
                                    quizLayoutViewModel,
                                    loadIndex,
                                    quizType,
                                    insertIndex,
                                    navController
                                )
                            }
                            composable<Route.QuizSolver>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) { backStackEntry ->
                                QuizSolver(navController, quizLayoutViewModel,
                                    navigateToScoreCard = {
                                        val creatingQuiz =
                                            hasVisitedRoute(navController, Route.QuizBuilder)
                                        if (creatingQuiz) {
                                            ToastManager.showToast(
                                                R.string.can_not_proceed_when_creating_quiz,
                                                ToastType.INFO
                                            )
                                            return@QuizSolver
                                        }
                                        quizLayoutViewModel.gradeQuiz(
                                            userViewModel.userData.value?.email ?: "GUEST"
                                        ) {
                                            navController.navigate(
                                                Route.ScoringScreen
                                            ) {
                                                popUpTo(Route.Home) { inclusive = false }
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                )
                            }
                            composable<Route.DesignScoreCard>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                DesignScoreCardScreen(
                                    navController,
                                    quizLayoutViewModel,
                                    scoreCardViewModel,
                                    onUpload = {
                                        navController.popBackStack(
                                            Route.Home,
                                            inclusive = false
                                        )
                                        quizLoadViewModel.reset()
                                        scoreCardViewModel.resetScoreCard()
                                    })
                            }
                            composable<Route.LoadLocalQuiz>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                LoadItems(
                                    navController = navController,
                                    quizLoadViewModel = quizLoadViewModel
                                ) { quizData, quizTheme, scoreCard ->
                                    quizLayoutViewModel.loadQuizData(
                                        quizData, quizTheme
                                    )
                                    scoreCardViewModel.loadScoreCard(scoreCard)
                                    quizLoadViewModel.loadComplete()
                                }
                            }
                            composable<Route.LoadUserQuiz>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                LoadMyQuiz(
                                    navController = navController,
                                    quizLoadViewModel = quizLoadViewModel,
                                    email = userViewModel.userData.value?.email ?: ""
                                )
                            }
                            composable<Route.ScoringScreen>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                ScoringScreen(
                                    navController = navController,
                                    quizLayoutViewModel = quizLayoutViewModel,
                                    scoreCardViewModel = scoreCardViewModel,
                                    email = userViewModel.userData.value?.email ?: "GUEST",
                                    loadQuiz = { quizId ->
                                        loadQuiz(quizId, doPop = true)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW || intent.action == "com.asu1.quizzer.ACTION_GET") {
            val data: Uri? = intent.data
            data?.let {
                val resultId = it.getQueryParameter("resultId") ?: ""
                val quizId = it.getQueryParameter("quizId") ?: ""
                if(resultId.isNotEmpty()){
                    quizCardMainViewModel.setLoadResultId(resultId)
                }
                if(quizId.isNotEmpty()){
                    quizCardMainViewModel.setLoadQuizId(quizId)
                }
            }
        }
    }
}

