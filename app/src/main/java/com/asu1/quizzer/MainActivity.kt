package com.asu1.quizzer

import SnackBarManager
import ToastType
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.asu1.activityNavigation.Route
import com.asu1.activityNavigation.enterFadeInTransition
import com.asu1.activityNavigation.enterFromRightTransition
import com.asu1.activityNavigation.exitFadeOutTransition
import com.asu1.activityNavigation.exitToRightTransition
import com.asu1.activityNavigation.snackbar.CustomSnackbarHost
import com.asu1.mainpage.screens.LoginScreen
import com.asu1.mainpage.screens.MainScreen
import com.asu1.mainpage.screens.MyActivitiesScreen
import com.asu1.mainpage.screens.NotificationScreen
import com.asu1.mainpage.screens.PrivacyPolicy
import com.asu1.mainpage.screens.RegisterScreen
import com.asu1.mainpage.viewModels.QuizCardMainViewModel
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.quiz.content.quizCommonBuilder.QuizChecker
import com.asu1.quiz.content.quizCommonBuilder.QuizBuilderScreen
import com.asu1.quiz.content.quizCommonBuilder.QuizCaller
import com.asu1.quiz.layoutBuilder.QuizLayoutBuilderScreen
import com.asu1.quiz.scorecard.DesignScoreCardScreen
import com.asu1.quiz.scorecard.ScoringScreen
import com.asu1.quiz.content.quizCommonBuilder.QuizSolver
import com.asu1.quiz.viewmodel.quizLayout.QuizContentViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizGeneralViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizResultViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeViewModel
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModel
import com.asu1.quizcard.quizLoad.LoadLocalQuizScreen
import com.asu1.quizcard.quizLoad.LoadLocalQuizViewModel
import com.asu1.quizcard.quizLoad.LoadMyQuizScreen
import com.asu1.quizcard.quizLoad.LoadMyQuizViewModel
import com.asu1.resources.R
import com.asu1.search.SearchScreen
import com.asu1.search.SearchViewModel
import com.asu1.splashpage.InitializationScreen
import com.asu1.splashpage.InitializationViewModel
import com.asu1.utils.Logger
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // NEED INITIALIZATION WITH HIGH PRIORITY
    private val quizCardMainViewModel: QuizCardMainViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    // CAN BE INITIALIZED LATER
    private val searchViewModel: SearchViewModel by viewModels()
    private val quizCoordinatorViewModel: QuizCoordinatorViewModel by viewModels()
    private val quizGeneralViewModel: QuizGeneralViewModel by viewModels()
    private val quizContentViewModel: QuizContentViewModel by viewModels()
    private val quizThemeViewModel: QuizThemeViewModel by viewModels()
    private val quizResultViewModel: QuizResultViewModel by viewModels()
    private val scoreCardViewModel: ScoreCardViewModel by viewModels()
    private val loadMyQuizViewModel: LoadMyQuizViewModel by viewModels()
    private val loadLocalQuizViewModel: LoadLocalQuizViewModel by viewModels()
    private val initializationViewModel: InitializationViewModel by viewModels()

    private lateinit var navController: NavHostController
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var updateLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        updateLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode != RESULT_OK) {
                Logger.debug("Update flow failed! Result code: " + result.resultCode)
            } else{
                initializationViewModel.noUpdateAvailable()
            }
        }

        lifecycleScope.launch {
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                    // Request the update.
                }else{
                    initializationViewModel.noUpdateAvailable()
                }
            }
            if(BuildConfig.isDebug){
                delay(1000)
                initializationViewModel.noUpdateAvailable()
            }
        }
        handleIntent(intent)


        quizCoordinatorViewModel.setViewModels(
            quizGeneral = quizGeneralViewModel,
            quizTheme = quizThemeViewModel,
            quizContent = quizContentViewModel,
            quizResult = quizResultViewModel,
            scoreCard = scoreCardViewModel,
        )
        ProcessLifecycleOwner.get().lifecycle.addObserver(appStateObserver)

        setContent {
            val context = LocalContext.current
            val snackbarHostState = remember { SnackbarHostState() }

            // Observe ToastManager for Toast messages
            LaunchedEffect(Unit) {
                SnackBarManager.snackBarMessage.collectLatest { message ->
                    message?.let {
                        val prefix = it.second.prefix
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = "$prefix${context.getString(it.first)}",
                            duration = SnackbarDuration.Short
                        )
                        SnackBarManager.snackBarShown()
                    }
                }
            }

            navController = rememberNavController()
            com.asu1.resources.QuizzerAndroidTheme {
                Scaffold(
                    snackbarHost = {
                        CustomSnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                        )
                    }
                ) { it ->
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.padding(it).fillMaxSize()
                    ) {
                        fun hasVisitedRoute(
                            navController: NavController,
                            route: Route
                        ): Boolean {
                            val previousEntry = navController.previousBackStackEntry
                            return previousEntry?.destination?.route == route::class.qualifiedName
                        }

                        fun getQuizResult(resultId: String = "") {
                            if (resultId.isEmpty()) return
                            quizCoordinatorViewModel.loadQuizResult(resultId)
                            navController.navigate(
                                Route.ScoringScreen
                            ) {
                                popUpTo(Route.Home) { inclusive = false }
                                launchSingleTop = true
                                quizCardMainViewModel.setLoadResultId(null)
                            }
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
                            navController.navigate(
                                Route.Home
                            ) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        fun navigateToCreateQuizLayout(){
                            if (userViewModel.userData.value?.email == null) {
                                SnackBarManager.showSnackBar(
                                    R.string.please_login_first,
                                    ToastType.INFO
                                )
                                navController.navigate(
                                    Route.Login
                                ) {
                                    launchSingleTop = true
                                }
                            } else {
                                quizCoordinatorViewModel.resetQuizData(userViewModel.userData.value?.email)
                                loadLocalQuizViewModel.reset()
                                navController.navigate(
                                    Route.CreateQuizLayout
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        }

                        fun navigateToLoadUserQuiz(){
                            loadMyQuizViewModel.loadUserQuiz(
                                userViewModel.userData.value?.email ?: ""
                            )
                            navController.navigate(
                                Route.LoadUserQuiz
                            ) {
                                launchSingleTop = true
                            }
                        }

                        NavHost(
                            navController = navController,
                            startDestination = Route.Init,
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None },
                        ) {
                            composable<Route.Init> {
                                InitializationScreen(
                                    initViewModel = initializationViewModel,
                                    userViewModel = userViewModel,
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
                                    navigateTo = {route ->
                                        if(route is Route.CreateQuizLayout){
                                            navigateToCreateQuizLayout()
                                        }
                                        else if(route is Route.LoadUserQuiz){
                                            navigateToLoadUserQuiz()
                                        }
                                        else if(route is Route.MyActivities){
                                            userViewModel.getUserActivities()
                                            navController.navigate(route) {
                                                launchSingleTop = true
                                            }
                                        }
                                        else{
                                            navController.navigate(route) {
                                                launchSingleTop = true
                                            }
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
                                        userViewModel.login(email, profileUri)
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
                                    navController = navController,
                                    quizCoordinatorViewModel = quizCoordinatorViewModel,
                                    navigateToQuizLoad = {
                                        loadLocalQuizViewModel.loadLocalQuiz(
                                            context = context,
                                            email = userViewModel.userData.value?.email
                                                ?: "GUEST"
                                        )
                                        navController.navigate(
                                            Route.LoadLocalQuiz
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                )
                            }
                            composable<Route.QuizBuilder>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                QuizBuilderScreen(navController,
                                    quizCoordinatorViewModel = quizCoordinatorViewModel,
                                    onMoveToScoringScreen = {
                                        scoreCardViewModel.updateScoreCard(
                                            quizGeneralViewModel.quizGeneralUiState.value.quizData,
                                            quizThemeViewModel.quizTheme.value.colorScheme
                                        )
                                        navController.navigate(
                                            Route.DesignScoreCard
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                    navigateToQuizLoad = {
                                        loadLocalQuizViewModel.loadLocalQuiz(
                                            context = context,
                                            email = userViewModel.userData.value?.email
                                                ?: "GUEST"
                                        )
                                        navController.navigate(
                                            Route.LoadLocalQuiz
                                        ) {
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
                                    navController = navController,
                                    quizCoordinatorViewModel = quizCoordinatorViewModel,
                                    loadIndex = loadIndex,
                                    quizType = quizType,
                                    insertIndex = insertIndex,
                                )
                            }
                            composable<Route.QuizSolver>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
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
                                    navController = navController,
                                    quizCoordinatorViewModel = quizCoordinatorViewModel,
                                    onUpload = {
                                        navController.popBackStack(
                                            Route.Home,
                                            inclusive = false
                                        )
                                        loadMyQuizViewModel.reset()
                                        scoreCardViewModel.resetScoreCard()
                                    })
                            }
                            composable<Route.LoadLocalQuiz>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                LoadLocalQuizScreen(
                                    navController = navController,
                                    loadLocalQuizViewModel = loadLocalQuizViewModel,
                                ) { quizData, quizTheme, scoreCard ->
                                    quizCoordinatorViewModel.loadQuiz(
                                        quizData = quizData,
                                        quizTheme = quizTheme,
                                        scoreCard = scoreCard,
                                    )
                                    loadLocalQuizViewModel.loadComplete()
                                }
                            }
                            composable<Route.LoadUserQuiz>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                LoadMyQuizScreen(
                                    navController = navController,
                                    loadMyQuizViewModel = loadMyQuizViewModel,
                                    email = userViewModel.userData.value?.email ?: ""
                                )
                            }
                            composable<Route.MyActivities>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                MyActivitiesScreen(
                                    navController = navController,
                                    userViewModel = userViewModel,

                                    )
                            }
                            composable<Route.Notifications>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                NotificationScreen(
                                    navController = navController,
                                )
                            }
                            composable<Route.QuizChecker>(
                                enterTransition = enterFadeInTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFadeInTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                QuizChecker(
                                    quizCoordinatorViewModel = quizCoordinatorViewModel,
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
                                    quizCoordinatorViewModel = quizCoordinatorViewModel,
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

    private val appStateObserver = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_START) {
            Logger.debug("QUIZZER ON START")
        }
    }
    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build())
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

