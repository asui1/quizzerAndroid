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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asu1.quizzer.screens.InitializationScreen
import com.asu1.quizzer.screens.LoginScreen
import com.asu1.quizzer.screens.MainScreen
import com.asu1.quizzer.screens.NicknameInput
import com.asu1.quizzer.screens.PrivacyPolicy
import com.asu1.quizzer.screens.QuizLayoutBuilderScreen
import com.asu1.quizzer.screens.SearchScreen
import com.asu1.quizzer.screens.TagSetting
import com.asu1.quizzer.screens.UsageAgreement
import com.asu1.quizzer.states.rememberInitState
import com.asu1.quizzer.states.rememberLoginActivityState
import com.asu1.quizzer.states.rememberMainActivityState
import com.asu1.quizzer.states.rememberQuizLayoutState
import com.asu1.quizzer.states.rememberRegisterActivityState
import com.asu1.quizzer.states.rememberSearchActivityState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.enterFadeInTransition
import com.asu1.quizzer.util.enterFromRightTransition
import com.asu1.quizzer.util.exitFadeOutTransition
import com.asu1.quizzer.util.exitToRightTransition
import com.asu1.quizzer.viewModels.InquiryViewModel
import com.asu1.quizzer.viewModels.MainViewModel
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.RegisterViewModel
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
                        val initState = rememberInitState(mainViewModel = mainViewModel)
                        val mainActivityState = rememberMainActivityState(
                            quizCardMainViewModel = quizCardMainViewModel,
                            userViewModel = userViewModel,
                            signOutViewModel = signOutViewModel,
                            inquiryViewModel = inquiryViewModel,
                        )
                        val loginActivityState = rememberLoginActivityState(
                            userViewModel = userViewModel,
                        )
                        val registerActivityState = rememberRegisterActivityState(
                            registerViewmodel =registerViewModel
                        )
                        val searchScreenActivityState = rememberSearchActivityState(
                            searchViewModel = searchViewModel
                        )
                        val quizLayoutState = rememberQuizLayoutState(
                            quizLayoutViewModel = quizLayoutViewModel
                        )


                        NavHost(
                            navController = navController,
                            startDestination = Route.Init.route
                        ) {
                            composable(Route.Init.route) {
                                mainViewModel.updateInternetConnection()
                                InitializationScreen(
                                    navController,
                                    initActivityState = initState
                                )
                            }
                            composable(Route.Home.route) {
                                MainScreen(
                                    navController,
                                    mainActivityState,
                                )
                            }
                            composable(Route.Trends.route) {
                                //TODO
                            }
                            composable(Route.Statistics.route) {
                                //TODO
                            }
                            composable(Route.Setting.route) {
                                //TODO
                            }
                            composable(Route.Search.route,
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                                ) {
                                SearchScreen(navController, searchScreenActivityState)
                            }
                            composable(Route.Login.route,
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                LoginScreen(navController, loginActivityState)
                            }
                            composable(Route.PrivacyPolicy.route,
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                PrivacyPolicy(navController)
                            }
                            composable(Route.RegisterPolicyAgreement.route + "?email={email}?profileUri={profileUri}",
                                arguments = listOf(
                                    navArgument("email") {
                                        type = NavType.StringType
                                        nullable = true
                                    },
                                    navArgument("profileUri") { type = NavType.StringType
                                        nullable = true
                                    }
                                ),
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                                ) {backStackEntry ->
                                val email = backStackEntry.arguments?.getString("email")
                                val profileUri = backStackEntry.arguments?.getString("profileUri")
                                if(email == null || profileUri == null) {
                                    return@composable
                                }
                                registerActivityState.email = email
                                registerActivityState.photoUri = profileUri
                                UsageAgreement(navController, registerActivityState)
                            }
                            composable(Route.RegisterNickname.route) {
                                NicknameInput (navController, registerActivityState)
                            }
                            composable(Route.RegisterTags.route) {
                                TagSetting(navController, registerActivityState)
                            }
                            composable(Route.CreateQuizLayout.route,
                                enterTransition = enterFromRightTransition(),
                                exitTransition = exitToRightTransition(),
                                popEnterTransition = enterFromRightTransition(),
                                popExitTransition = exitToRightTransition(),
                            ) {
                                val userData = userViewModel.userData.value
                                val email = userData?.email ?: "GUEST"
                                quizLayoutViewModel.initQuizLayout(
                                    email = email,
                                    colorScheme = MaterialTheme.colorScheme
                                )
                                QuizLayoutBuilderScreen(navController, quizLayoutState)
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

    }
}

