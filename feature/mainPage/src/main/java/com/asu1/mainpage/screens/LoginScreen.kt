package com.asu1.mainpage.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.customComposable.text.TextDivider
import com.asu1.activityNavigation.Route
import com.asu1.mainpage.composables.GoogleCredentialManager
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.utils.getAppVersion


@Composable
fun LoginScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()
    val isUserLoggedIn by userViewModel.isUserLoggedIn.observeAsState(false)
    val credentialManager = remember{GoogleCredentialManager(context)}

    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.popBackStack()
        }
    }

    LoginBody(
        onClickSignIn = {
            credentialManager.requestLogin(
                login = {email, profileUri ->
                    userViewModel.login(email, profileUri)
                }
            )
        },
        onClickRegister = {
            credentialManager.requestRegister(
                register = {email, profileUri ->
                    navController.navigate(
                        Route.Register(
                            email,
                            profileUri
                        )
                    ){
                        launchSingleTop = true
                    }
                }
            )
        }
    )
}

@Composable
fun LoginBody(
    onClickSignIn: () -> Unit,
    onClickRegister: () -> Unit
) {
    val context = LocalContext.current
    val version = remember { context.getAppVersion() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppLogo()
        Spacer(Modifier.height(40.dp))

        SectionDivider(
            textRes = R.string.login,
            textStyle = QuizzerTypographyDefaults.quizzerHeadlineMediumBold
        )
        Spacer(Modifier.height(8.dp))

        SignInButton(onClickSignIn)
        Spacer(Modifier.height(48.dp))

        SectionDivider(
            textRes = R.string.no_account,
            textStyle = QuizzerTypographyDefaults.quizzerBodyMediumNormal
        )
        Spacer(Modifier.height(16.dp))

        RegisterButton(onClickRegister)
        Spacer(Modifier.height(32.dp))

        VersionText(version)
    }
}

@Composable
private fun AppLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "App Icon",
        modifier = modifier
            .fillMaxWidth(0.8f)
            .widthIn(max = 600.dp),
        contentScale = ContentScale.FillWidth,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
    )
}

@Composable
private fun SectionDivider(
    @StringRes textRes: Int,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    TextDivider(
        modifier = modifier.padding(horizontal = 16.dp),
        text = {
            Text(
                text = stringResource(textRes),
                style = textStyle
            )
        }
    )
}

@Composable
private fun SignInButton(onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.android_neutral_rd_si),
        contentDescription = "Sign in with Google",
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun RegisterButton(onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.android_neutral_rd_na),
        contentDescription = "Continue with Google",
        modifier = Modifier
            .size(45.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun VersionText(version: String) {
    Text(
        text = version,
        style = QuizzerTypographyDefaults.quizzerLabelSmallLight
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginBody(
        onClickRegister = {},
        onClickSignIn = {},
    )
}
