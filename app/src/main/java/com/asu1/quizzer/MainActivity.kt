package com.asu1.quizzer

import SnackBarManager
import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.asu1.activityNavigation.snackbar.CustomSnackbarHost
import com.asu1.resources.QuizzerAndroidTheme
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

private const val DEFAULT_DELAY_MS = 1_000L

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val initializationViewModel: InitializationViewModel by viewModels()



    private lateinit var navController: NavHostController
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var updateLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInAppUpdate()
        setContent{
            QuizzerAndroidTheme {
                navController = rememberNavController()
                AppScaffold()
            }
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
                Logger.debug("Release Debug Updater in Debug")
                delay(DEFAULT_DELAY_MS)
                initializationViewModel.noUpdateAvailable()
            }
        }
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
                QuizNavGraphManager(
                    navController = navController,
                    initializationViewModel = initializationViewModel,
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
        navController.handleDeepLink(intent)
    }
}
