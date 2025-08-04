package com.asu1.quizzer

import SnackBarManager
import ToastType
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.asu1.activityNavigation.Route
import com.asu1.activityNavigation.snackbar.CustomSnackbarHost
import com.asu1.mainpage.viewModels.QuizCardMainViewModel
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizContentViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizGeneralViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizResultViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeViewModel
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModel
import com.asu1.quizcard.quizLoad.LoadLocalQuizViewModel
import com.asu1.quizcard.quizLoad.LoadMyQuizViewModel
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
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

        initInAppUpdate()
        initViewModelsAndObservers()
        handleIntent(intent)
        setContent{
            QuizzerAndroidTheme {
                navController = rememberNavController()
                AppScaffold()
            }
        }
    }

    fun getQuizResult(
        resultId: String = ""
    ) {
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

    private fun initInAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        updateLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode != RESULT_OK) {
                Logger.debug("Update flow failed! Result code: ${result.resultCode}")
            } else {
                initializationViewModel.noUpdateAvailable()
            }
        }

        lifecycleScope.launch {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                } else {
                    initializationViewModel.noUpdateAvailable()
                }
            }
            if (BuildConfig.isDebug) {
                delay(1_000)
                initializationViewModel.noUpdateAvailable()
            }
        }
    }

    // 2️⃣ ViewModel wiring & app‑state observing
    private fun initViewModelsAndObservers() {
        quizCoordinatorViewModel.setViewModels(
            quizGeneral     = quizGeneralViewModel,
            quizTheme       = quizThemeViewModel,
            quizContent     = quizContentViewModel,
            quizResult      = quizResultViewModel,
            scoreCard       = scoreCardViewModel
        )
        ProcessLifecycleOwner.get().lifecycle.addObserver(appStateObserver)
    }

    @Composable
    private fun AppScaffold() {
        val snackBarHostState = remember { SnackbarHostState() }
        val context = LocalContext.current
        SnackBarListener(
            hostState = snackBarHostState,
            context = context,
        )

        Scaffold(
            snackbarHost = { CustomSnackbarHost(snackBarHostState) }
        ) { padding ->
            Surface(
                Modifier.padding(padding).fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                QuizNavGraph(
                    navController = navController,
                    getHome = ::getHome,
                    loadQuiz = ::loadQuiz,
                    getQuizResult = ::getQuizResult,
                    navigateToCreateQuizLayout = ::navigateToCreateQuizLayout,
                    navigateToLoadUserQuiz = ::navigateToLoadUserQuiz,
                )
            }
        }
    }

    // 3‑c: compose‑side SnackBar listener
    @Composable
    private fun SnackBarListener(hostState: SnackbarHostState, context: Context) {
        LaunchedEffect(Unit) {
            SnackBarManager.snackBarMessage.collectLatest { message ->
                message?.let {
                    val prefix = it.second.prefix
                    hostState.currentSnackbarData?.dismiss()
                    hostState.showSnackbar(
                        message = "$prefix${context.getString(it.first)}",
                        duration = SnackbarDuration.Short
                    )
                    SnackBarManager.snackBarShown()
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

