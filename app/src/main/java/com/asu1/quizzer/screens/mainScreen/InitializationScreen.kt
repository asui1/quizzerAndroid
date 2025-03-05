package com.asu1.quizzer.screens.mainScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.customdialogs.UpdateDialog
import com.asu1.quizzer.composables.animations.LoadingAnimation
import com.asu1.quizzer.viewModels.InitializationViewModel
import com.asu1.quizzer.viewModels.UserViewModel
import com.asu1.resources.InitializationState
import com.asu1.resources.R

@Composable
fun InitializationScreen(initViewModel: InitializationViewModel = viewModel(),
                         userViewModel: UserViewModel = viewModel(),
                         navigateToHome: () -> Unit = {},
                         ) {
    val initializationState by initViewModel.initializationState.observeAsState()
    val isUpdateAvailable by initViewModel.isUpdateAvailable.observeAsState(null)

    LaunchedEffect(initializationState){
        when(initializationState){
            InitializationState.CHECKING_FOR_UPDATES -> {
            }
            InitializationState.GETTING_USER_DATA -> {
                userViewModel.initLogin(
                    onDone = {
                        initViewModel.updateInitializationState(InitializationState.DONE)
                    }
                )
            }
            InitializationState.DONE -> {
                navigateToHome()
            }
            null -> {}
        }
    }
    if(isUpdateAvailable == true){
        UpdateDialog(
            onUpdate = { redirectToPlayStore(it) },
            onCancel = {  },
        )
    }

    LoadingAnimation(
        modifier = Modifier.fillMaxSize(),
        withText = { initializationState?.stringResource ?: R.string.empty_string }
    )
}

private fun redirectToPlayStore(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://play.google.com/store/apps/details?id=com.asu1.quizzer")
        setPackage("com.android.vending")
    }
    context.startActivity(intent)
}