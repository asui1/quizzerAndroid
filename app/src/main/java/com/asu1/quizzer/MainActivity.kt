package com.asu1.quizzer

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
import androidx.lifecycle.Observer
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
import com.asu1.quizzer.screens.SearchScreen
import com.asu1.quizzer.screens.TagSetting
import com.asu1.quizzer.screens.UsageAgreement
import com.asu1.quizzer.screens.quiz.QuizCaller
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
import com.asu1.quizzer.viewModels.RegisterViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.SearchViewModel
import com.asu1.quizzer.viewModels.SignOutViewModel
import com.asu1.quizzer.viewModels.UserViewModel

class MainActivity : ComponentActivity() {
    private val inquiryViewModel: InquiryViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val quizCardMainViewModel: QuizCardMainViewModel by viewModels()
    private val signOutViewModel: SignOutViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val quizLayoutViewModel: QuizLayoutViewModel by viewModels()
    private val scoreCardViewModel: ScoreCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger().debug("MainActivity: onCreate")
        super.onCreate(savedInstanceState)

        setContent {
            Box(Modifier.safeDrawingPadding()) {

                QuizzerAndroidTheme {
                    Surface(
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        val colorScheme = MaterialTheme.colorScheme
                        val loginActivityState = rememberLoginActivityState(
                            userViewModel = userViewModel,
                        )
                        Logger().debug("MainActivity: NavHost")
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
                                    signOutViewModel = signOutViewModel,
                                    inquiryViewModel = inquiryViewModel,
                                    loginActivityState = loginActivityState,
                                    navigateToQuizLayoutBuilder = {
                                        quizLayoutViewModel.resetQuizLayout()
                                        quizLayoutViewModel.initQuizLayout(
                                            userViewModel.userData.value?.email,
                                            colorScheme
                                        )
                                        scoreCardViewModel.resetScoreCard()
                                        NavMultiClickPreventer.navigate(
                                            navController,
                                            Route.CreateQuizLayout
                                        )
                                    },
                                )
                            }
                            composable<Route.Trends> {
                                //TODO
                            }
                            composable<Route.Statistics> {
                                //TODO
                            }
                            composable<Route.Setting> {
                                //TODO
                            }
                            composable<Route.Search>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                SearchScreen(navController, searchViewModel)
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
                            composable<Route.RegisterPolicyAgreement> (
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {backStackEntry ->
                                val email = backStackEntry.toRoute<Route.RegisterPolicyAgreement>().email
                                val profileUri = backStackEntry.toRoute<Route.RegisterPolicyAgreement>().profileUri
                                if(email == "" || profileUri == null) {
                                    return@composable
                                }
                                registerViewModel.setEmail(email)
                                registerViewModel.setPhotoUri(profileUri)
                                UsageAgreement(navController, registerViewModel)
                            }
                            composable<Route.RegisterNickname> {
                                NicknameInput (navController, registerViewModel)
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
                                val userData = userViewModel.userData.value
                                QuizLayoutBuilderScreen(navController, quizLayoutViewModel, userData)
                            }
                            composable<Route.QuizBuilder>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                QuizBuilderScreen(navController, quizLayoutViewModel,
                                    onMoveToScoringScreen = {
                                        scoreCardViewModel.updateScoreCard(quizLayoutViewModel.quizData.value, quizLayoutViewModel.quizTheme.value.colorScheme)
                                        NavMultiClickPreventer.navigate(navController, Route.DesignScoreCard)
                                    }
                                )
                            }
                            composable<Route.QuizCaller> (
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {backStackEntry ->
                                Logger().debug("MainActivity: QuizCaller")
                                val loadIndex = backStackEntry.toRoute<Route.QuizCaller>().loadIndex
                                val quizType = backStackEntry.toRoute<Route.QuizCaller>().quizType
                                val insertIndex = backStackEntry.toRoute<Route.QuizCaller>().insertIndex
                                QuizCaller(quizLayoutViewModel, loadIndex, quizType, insertIndex, navController)
                            }
                            composable<Route.QuizSolver>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) { backStackEntry ->
                                val loadIndex = backStackEntry.toRoute<Route.QuizSolver>().initIndex
                                QuizSolver(navController, quizLayoutViewModel, loadIndex)
                            }
                            composable<Route.DesignScoreCard>(
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitFadeOutTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitFadeOutTransition(),
                            ) {
                                DesignScoreCardScreen(navController, quizLayoutViewModel, scoreCardViewModel, onUpload = {
                                    //TODO implement upload function at quizLayoutViewModel using ScoreCardViewModel contents
                                })
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

    }
}

