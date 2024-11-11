package com.asu1.quizzer.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.asu1.quizzer.composables.LoadComposable
import com.asu1.quizzer.composables.NoInternetDialog
import com.asu1.quizzer.composables.UpdateDialog
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.MainViewModel

@Composable
fun InitializationScreen(navHostController: NavHostController, initViewModel: MainViewModel = viewModel()) {
    val isUpdateAvailable by initViewModel.isUpdateAvailable.observeAsState()
    val hasCheckedUpdate = remember { mutableStateOf(false) }

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
            NavMultiClickPreventer.navigate(navHostController,Route.Home) {
                popUpTo(navHostController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }
}

private fun redirectToPlayStore(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://play.google.com/store/apps/details?id=com.asu1.quizzer")
        setPackage("com.android.vending")
    }
    ContextCompat.startActivity(context, intent, null)
}