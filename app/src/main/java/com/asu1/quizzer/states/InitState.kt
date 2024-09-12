package com.asu1.quizzer.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.viewModels.MainViewModel


data class InitState(
    val isInternetAvailable: State<Boolean?>,
    val isUpdateAvailable: State<Boolean?>,
    val checkUpdate: () -> Unit,
    val onRetryInternet: () -> Unit,
    val finishApp: () -> Unit,
)

@Composable
fun rememberInitState(
    mainViewModel: MainViewModel = viewModel(),
): InitState {
    val isInternetAvailable by mainViewModel.isInternetAvailable.observeAsState(initial = null)
    val isUpdateAvailable by mainViewModel.isUpdateAvailable.observeAsState(initial = null)

    return InitState(
        isInternetAvailable = rememberUpdatedState(isInternetAvailable),
        isUpdateAvailable = rememberUpdatedState(isUpdateAvailable),
        checkUpdate = { mainViewModel.updateIsUpdateAvailable() },
        onRetryInternet = { mainViewModel.updateInternetConnection() },
        finishApp = { mainViewModel.finishApp() },
    )
}