package com.asu1.quizzer

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
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.asu1.quizzer.screens.DesignScoreCardScreen
import com.asu1.quizzer.screens.InitializationScreen
import com.asu1.quizzer.screens.LoginScreen
import com.asu1.quizzer.screens.MainScreen
import com.asu1.quizzer.screens.NicknameInput
import com.asu1.quizzer.screens.PrivacyPolicy
import com.asu1.quizzer.screens.QuizBuilderScreen
import com.asu1.quizzer.screens.QuizLayoutBuilderScreen
import com.asu1.quizzer.screens.QuizSolver
import com.asu1.quizzer.screens.ScoringScreen
import com.asu1.quizzer.screens.SearchScreen
import com.asu1.quizzer.screens.TagSetting
import com.asu1.quizzer.screens.UsageAgreement
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
import com.asu1.quizzer.util.fromRouteName
import com.asu1.quizzer.viewModels.InquiryViewModel
import com.asu1.quizzer.viewModels.MainViewModel
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.QuizLoadViewModel
import com.asu1.quizzer.viewModels.RegisterViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.SearchViewModel
import com.asu1.quizzer.viewModels.UserViewModel


class MainActivity : ComponentActivity() {
    private val inquiryViewModel: InquiryViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val quizCardMainViewModel: QuizCardMainViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val quizLayoutViewModel: QuizLayoutViewModel by viewModels()
    private val scoreCardViewModel: ScoreCardViewModel by viewModels()
    private val quizLoadViewModel: QuizLoadViewModel by viewModels()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)

        setContent {
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

                        fun getQuizResult(resultId: String = "0c6d3a6f5f13602f46a2b37c11a33618b09bf1f11af2dc1a221e2aa191d9b773") {
                            quizLayoutViewModel.loadQuizResult(resultId, scoreCardViewModel)
                            NavMultiClickPreventer.navigate(
                                navController,
                                Route.ScoringScreen
                            ) {
                                popUpTo(Route.Home) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                        fun loadQuiz(quizId: String){
                            quizLayoutViewModel.loadQuiz(quizId, scoreCardViewModel,
                                onDone = {
                                    NavMultiClickPreventer.navigate(
                                        navController,
                                        Route.QuizSolver(0)
                                    ){
                                        popUpTo(Route.Home) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                })
                        }
                        NavHost(
                            navController = navController,
                            startDestination = Route.Init
                        ) {
                            composable<Route.Init> {
                                InitializationScreen(
                                    navController,
                                    initViewModel = mainViewModel
                                )
                            }
                            composable<Route.Home> {
                                MainScreen(
                                    navController,
                                    quizCardMainViewModel = quizCardMainViewModel,
                                    inquiryViewModel = inquiryViewModel,
                                    loginActivityState = loginActivityState,
                                    navigateToQuizLayoutBuilder = {
                                        if (userViewModel.userData.value?.email == null) {
                                            userViewModel.setToast("Please login first")
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
                                    testPress = {
                                        getQuizResult()
                                    },
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
                                            quizLayoutViewModel.sendToast("Can not Proceed when Creating Quiz")
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
                                    ) {
                                        quizLoadViewModel.loadComplete()
                                    }
                                    scoreCardViewModel.loadScoreCard(scoreCard)
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
                                        loadQuiz(quizId)
                                    }
                                )
                            }
                        }

                    }
                }
            }
        }
        inquiryViewModel.showToast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                inquiryViewModel.toastShown()
            }
        })
        userViewModel.showToast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                userViewModel.toastShown()
            }
        })
        registerViewModel.showToast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                registerViewModel.toastShown()
            }
        })
        searchViewModel.showToast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                searchViewModel.toastShown()
            }
        })
        quizLayoutViewModel.showToast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                quizLayoutViewModel.toastShown()
            }
        })
        quizLoadViewModel.showToast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                quizLoadViewModel.toastShown()
            }
        })

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val data: Uri? = intent.data
            data?.let {
                val query = it.getQueryParameter("query") ?: ""
                val route = it.getQueryParameter("route")
                // Use the query parameter as needed
                val targetRoute = fromRouteName(route, query)
                Logger().debug("Query: $query, Route: $route")
                if(targetRoute != null){
//                    navigateToRoute(targetRoute)
                }
            }
        }
    }
//    private fun navigateToRoute(route: Route) {
//        navController.navigate(Route.Init) {
//            popUpTo(Route.Init) { inclusive = true }
//        }
//        route.let {
//            quizLayoutViewModel.loadQuiz(quizId, scoreCardViewModel,
//                onDone = {
//                    NavMultiClickPreventer.navigate(
//                        navController,
//                        Route.QuizSolver(0)
//                    )
//                })
//        }
//    }
}
