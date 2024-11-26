package com.asu1.quizzer.screens.mainScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.asu1.quizzer.composables.LoadComposable
import com.asu1.quizzer.composables.UpdateDialog
import com.asu1.quizzer.viewModels.MainViewModel

@Composable
fun InitializationScreen(navHostController: NavHostController, initViewModel: MainViewModel = viewModel(),
                         navigateToHome: () -> Unit = {},
                         ) {
    val isUpdateAvailable by initViewModel.isUpdateAvailable.observeAsState(null)

    when (isUpdateAvailable) {
        null -> {
            LoadComposable()
        }
        true -> UpdateDialog(
            onUpdate = { redirectToPlayStore(it) },
            onCancel = { initViewModel.finishApp() },
        )
        else -> {
            navigateToHome()
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