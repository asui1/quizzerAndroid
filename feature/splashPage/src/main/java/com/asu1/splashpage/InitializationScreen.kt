package com.asu1.splashpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.resources.InitializationState
import com.asu1.resources.QuizzerAndroidTheme
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

    InitializationScreenBody(
        loadingText = { initializationState?.stringResource ?: R.string.empty_string }
    )
}

@Composable
fun InitializationScreenBody(loadingText: () -> Int){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your app icon resource
            contentDescription = "App Icon",
            modifier = Modifier.fillMaxWidth(0.8f)
                .widthIn(max = 600.dp),
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        LoadingAnimation(
            modifier = Modifier,
            withText = loadingText,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InitializationScreenPreview() {
    QuizzerAndroidTheme {
        InitializationScreenBody(
            loadingText = { R.string.loading_with_app_logo }
        )
    }
}