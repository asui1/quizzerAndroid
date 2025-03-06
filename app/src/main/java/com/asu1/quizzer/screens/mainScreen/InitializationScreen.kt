package com.asu1.quizzer.screens.mainScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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

    LoadingAnimation(
        modifier = Modifier.fillMaxSize(),
        withText = { initializationState?.stringResource ?: R.string.empty_string }
    )
}
