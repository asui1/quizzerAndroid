package com.asu1.quizzer.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.composables.LoadComposable
import com.asu1.quizzer.composables.NoInternetDialog
import com.asu1.quizzer.composables.UpdateDialog
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.viewModels.MainViewModel

@Composable
fun InitializationScreen(navHostController: NavHostController, initViewModel: MainViewModel = viewModel()) {
    val isInternetAvailable by initViewModel.isInternetAvailable.observeAsState()
    val isUpdateAvailable by initViewModel.isUpdateAvailable.observeAsState()
    val hasCheckedUpdate = remember { mutableStateOf(false) }

    Logger().debug("InitializationScreen: isInternetAvailable: $isInternetAvailable")
    if (isInternetAvailable == true){
        Logger().debug("InitializationScreen: isUpdateAvailable: $isUpdateAvailable")
        when (isUpdateAvailable) {
            null -> {
                if (!hasCheckedUpdate.value) {
                    initViewModel.updateIsUpdateAvailable()
                    hasCheckedUpdate.value = true
                }
                LoadComposable()
            }
            true -> UpdateDialog(
                onUpdate = { redirectToPlayStore(it) },
                onCancel = { initViewModel.finishApp() },
            )
            else -> {
                Logger().debug("InitializationScreen: Navigating to Home")
                navHostController.navigate(Route.Home) {
                    popUpTo(navHostController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        }

    } else if(isInternetAvailable == false){
        NoInternetDialog(
            onRetry = { initViewModel.updateInternetConnection() },
            onExit = { initViewModel.finishApp() },
        )
    }
    else{
        LoadComposable()
    }

}

private fun redirectToPlayStore(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://play.google.com/store/apps/details?id=com.asu1.quizzer")
        setPackage("com.android.vending")
    }
    ContextCompat.startActivity(context, intent, null)
}