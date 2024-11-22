package com.asu1.quizzer

import ToastManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.asu1.quizzer.composables.CustomSnackbar
import com.asu1.quizzer.composables.CustomSnackbarHost
import com.asu1.quizzer.screens.quizlayout.DesignScoreCardScreen
import com.asu1.quizzer.screens.mainScreen.InitializationScreen
import com.asu1.quizzer.screens.mainScreen.LoginScreen
import com.asu1.quizzer.screens.mainScreen.MainScreen
import com.asu1.quizzer.screens.mainScreen.NicknameInput
import com.asu1.quizzer.screens.mainScreen.PrivacyPolicy
import com.asu1.quizzer.screens.quiz.QuizBuilderScreen
import com.asu1.quizzer.screens.quizlayout.QuizLayoutBuilderScreen
import com.asu1.quizzer.screens.quiz.QuizSolver
import com.asu1.quizzer.screens.quiz.ScoringScreen
import com.asu1.quizzer.screens.mainScreen.SearchScreen
import com.asu1.quizzer.screens.mainScreen.TagSetting
import com.asu1.quizzer.screens.mainScreen.UsageAgreement
import com.asu1.quizzer.screens.quiz.QuizCaller
import com.asu1.quizzer.screens.quizlayout.LoadItems
import com.asu1.quizzer.screens.quizlayout.LoadMyQuiz
import com.asu1.quizzer.states.rememberLoginActivityState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.enterFromRightTransition
import com.asu1.quizzer.util.exitFadeOutTransition
import com.asu1.quizzer.util.exitToRightTransition
import com.asu1.quizzer.viewModels.InquiryViewModel
import com.asu1.quizzer.viewModels.MainViewModel
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.QuizLoadViewModel
import com.asu1.quizzer.viewModels.RegisterViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.SearchViewModel
import com.asu1.quizzer.viewModels.UserViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {

    // NEED INITIALIZATION WITH HIGH PRIORITY
    private val mainViewModel: MainViewModel by viewModels()
    private val quizCardMainViewModel: QuizCardMainViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    // CAN BE INITIALIZED LATER
    private val registerViewModel: RegisterViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val quizLayoutViewModel: QuizLayoutViewModel by viewModels()
    private val scoreCardViewModel: ScoreCardViewModel by viewModels()
    private val quizLoadViewModel: QuizLoadViewModel by viewModels()
    private val inquiryViewModel: InquiryViewModel by viewModels()
    private lateinit var navController: NavHostController
    private var loadResultId: String? = null
    private var loadQuizId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)

        lifecycleScope.launch {
            quizLayoutViewModel.initialize()
        }

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            navController = rememberNavController()
            Box(Modifier.safeDrawingPadding()) {
                QuizzerAndroidTheme {
                    Surface(
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val context = LocalContext.current
                        val colorScheme = MaterialTheme.colorScheme
                        val loginActivityState = rememberLoginActivityState(
                            userViewModel = userViewModel,
                        )
                        fun hasVisitedRoute(navController: NavController, route: Route): Boolean {
                            val previousEntry = navController.previousBackStackEntry
                            return previousEntry?.destination?.route == route::class.qualifiedName
                        }

                        fun getQuizResult(resultId: String = "") {
                            if(resultId.isEmpty()) return
                            quizLayoutViewModel.loadQuizResult(resultId, scoreCardViewModel)
                            NavMultiClickPreventer.navigate(
                                navController,
                                Route.ScoringScreen
                            ) {
                                popUpTo(Route.Home) { inclusive = false }
                                launchSingleTop = true
                                loadResultId = null
                            }
                        }
                        fun loadQuiz(quizId: String, popUpToHome: Boolean = false){
                            quizLayoutViewModel.loadQuiz(quizId, scoreCardViewModel)
                            NavMultiClickPreventer.navigate(
                                navController,
                                Route.QuizSolver(0)
                            ){
                                if(popUpToHome) popUpTo(Route.Home) { inclusive = false }
                                launchSingleTop = true
                            }
                            loadQuizId = null
                        }
                        fun getHome(fetchData: Boolean = true){
                            NavMultiClickPreventer.navigate(
                                navController,
                                Route.Home
                            ){
                                val lang = if(Locale.getDefault().language == "ko") "ko" else "en"
                                quizCardMainViewModel.resetQuizTrends()
                                quizCardMainViewModel.resetUserRanks()
                                if(fetchData) quizCardMainViewModel.fetchQuizCards(lang)
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                        CustomSnackbarHost(
                            hostState = snackbarHostState
                        )
                        NavHost(
                            navController = navController,
                            startDestination = Route.Init
                        ) {
                            composable<Route.Init> {
                                InitializationScreen(
                                    navController,
                                    initViewModel = mainViewModel,
                                    navigateToHome = {
                                        getHome(
                                            fetchData = false  // FOR DEBUG MODES
//                                            fetchData = loadResultId == null // ONLY IN DEBUG MODES
                                        )
                                    }
                                )
                            }
                            composable<Route.Home> {
                                if(loadResultId != null){
                                    getQuizResult(loadResultId!!)
                                }
                                if(loadQuizId != null){
                                    loadQuiz(loadQuizId!!)
                                }
                                MainScreen(
                                    navController,
                                    quizCardMainViewModel = quizCardMainViewModel,
                                    inquiryViewModel = inquiryViewModel,
                                    loginActivityState = loginActivityState,
                                    navigateToQuizLayoutBuilder = {
                                        if (userViewModel.userData.value?.email == null) {
                                            ToastManager.showToast(R.string.please_login_first, ToastType.INFO)
                                            NavMultiClickPreventer.navigate(
                                                navController,
                                                Route.Login
                                            )
                                        } else {
                                            quizLayoutViewModel.resetQuizLayout()
                                            quizLayoutViewModel.initQuizLayout(
                                                userViewModel.userData.value?.email,
                                                colorScheme
                                            )
                                            scoreCardViewModel.resetScoreCard()
                                            quizLoadViewModel.reset()
                                            NavMultiClickPreventer.navigate(
                                                navController,
                                                Route.CreateQuizLayout
                                            )
                                        }
                                    },
                                    navigateToMyQuizzes = {
                                        quizLoadViewModel.loadUserQuiz(
                                            userViewModel.userData.value?.email ?: ""
                                        )
                                        NavMultiClickPreventer.navigate(
                                            navController,
                                            Route.LoadUserQuiz
                                        )
                                    },
                                    loadQuiz ={loadQuiz(it)},
                                    moveHome = {getHome()}
                                )
                            }
                            composable<Route.Search>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                SearchScreen(navController, searchViewModel,
                                    onQuizClick = {quizId ->
                                        loadQuiz(quizId)
                                    },
                                    searchText = it.toRoute<Route.Search>().searchText
                                )
                            }
                            composable<Route.Login>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                LoginScreen(navController, loginActivityState)
                            }
                            composable<Route.PrivacyPolicy>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                PrivacyPolicy(navController)
                            }
                            composable<Route.RegisterPolicyAgreement>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) { backStackEntry ->
                                val email =
                                    backStackEntry.toRoute<Route.RegisterPolicyAgreement>().email
                                val profileUri =
                                    backStackEntry.toRoute<Route.RegisterPolicyAgreement>().profileUri
                                if (email == "" || profileUri == null) {
                                    return@composable
                                }
                                registerViewModel.setEmail(email)
                                registerViewModel.setPhotoUri(profileUri)
                                UsageAgreement(navController, registerViewModel)
                            }
                            composable<Route.RegisterNickname> {
                                NicknameInput(navController, registerViewModel)
                            }
                            composable<Route.RegisterTags> {
                                TagSetting(navController, registerViewModel)
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
                                        quizLoadViewModel.loadLocalQuiz(context = context, email = userViewModel.userData.value?.email ?: "GUEST")
                                        NavMultiClickPreventer.navigate(
                                            navController,
                                            Route.LoadLocalQuiz
                                        )
                                    },
                                    scoreCardViewModel = scoreCardViewModel,
                                )
                            }
                            composable<Route.QuizBuilder>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                QuizBuilderScreen(navController, quizLayoutViewModel,
                                    onMoveToScoringScreen = {
                                        scoreCardViewModel.updateScoreCard(
                                            quizLayoutViewModel.quizData.value,
                                            quizLayoutViewModel.quizTheme.value.colorScheme
                                        )
                                        NavMultiClickPreventer.navigate(
                                            navController,
                                            Route.DesignScoreCard
                                        )
                                    },
                                    scoreCardViewModel = scoreCardViewModel,
                                    navigateToQuizLoad = {
                                        quizLoadViewModel.loadLocalQuiz(context = context, email = userViewModel.userData.value?.email ?: "GUEST")
                                        NavMultiClickPreventer.navigate(
                                            navController,
                                            Route.LoadLocalQuiz
                                        )
                                    }
                                )
                            }
                            composable<Route.QuizCaller>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) { backStackEntry ->
                                val loadIndex = backStackEntry.toRoute<Route.QuizCaller>().loadIndex
                                val quizType = backStackEntry.toRoute<Route.QuizCaller>().quizType
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
                                val loadIndex = backStackEntry.toRoute<Route.QuizSolver>().initIndex
                                QuizSolver(navController, quizLayoutViewModel, loadIndex,
                                    navigateToScoreCard = {
                                        val creatingQuiz =
                                            hasVisitedRoute(navController, Route.QuizBuilder)
                                        if (creatingQuiz) {
                                            ToastManager.showToast(R.string.can_not_proceed_when_creating_quiz, ToastType.INFO)
                                            return@QuizSolver
                                        }
                                        quizLayoutViewModel.gradeQuiz(
                                            userViewModel.userData.value?.email ?: "GUEST"
                                        ){
                                            NavMultiClickPreventer.navigate(
                                                navController,
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
                                        navController.popBackStack(Route.Home, inclusive = false)
                                        quizLoadViewModel.reset()
                                        scoreCardViewModel.resetScoreCard()
                                    })
                            }
                            composable<Route.LoadLocalQuiz>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFromRightTransition(),
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
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                LoadMyQuiz(
                                    navController = navController,
                                    quizLoadViewModel = quizLoadViewModel,
                                    email = userViewModel.userData.value?.email ?: ""
                                )
                            }
                            composable<Route.ScoringScreen>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                ScoringScreen(
                                    navController = navController,
                                    quizLayoutViewModel = quizLayoutViewModel,
                                    scoreCardViewModel = scoreCardViewModel,
                                    email = userViewModel.userData.value?.email ?: "GUEST",
                                    loadQuiz = {quizId ->
                                        loadQuiz(quizId, popUpToHome = true)
                                    }
                                )
                            }
                        }
                        // Observe ToastManager for Toast messages
                        ToastManager.toastMessage.observe(this@MainActivity) { message ->
                            message?.let {
                                lifecycleScope.launch {
                                    try{
                                        snackbarHostState.showSnackbar(context.getString(message.first))
                                    }catch(_: Exception){
                                    }
                                }
                                ToastManager.toastShown()
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
        if (intent.action == Intent.ACTION_VIEW) {
            val data: Uri? = intent.data
            data?.let {
                val resultId = it.getQueryParameter("resultId") ?: ""
                val quizId = it.getQueryParameter("quizId") ?: ""
                if(resultId.isNotEmpty()){
                    loadResultId = resultId
                }
                if(quizId.isNotEmpty()){
                    loadQuizId = quizId
                }
            }
        }
    }
}
