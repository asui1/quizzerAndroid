package com.asu1.quizzer.screens.mainScreen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.asu1.custombuttons.TextDivider
import com.asu1.quizzer.util.GoogleCredentialManager
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.UserViewModel
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R


@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current
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
                            profileUri.toString()
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
private fun LoginBody(
    onClickSignIn: () -> Unit = {},
    onClickRegister: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
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
        TextDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = {
                Text(
                    text = stringResource(R.string.login),
                    style = QuizzerTypographyDefaults.quizzerLabelMediumMedium,
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.android_neutral_rd_si),
            contentDescription = "Sign in with Google",
            modifier = Modifier
                .clickable{
                    onClickSignIn()
                },
        )
        Spacer(modifier = Modifier.height(32.dp))
        TextDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = {
                Text(
                    text = stringResource(R.string.register),
                    style = QuizzerTypographyDefaults.quizzerLabelMediumMedium,
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.android_neutral_rd_na), // Replace with your drawable resource
            contentDescription = "Continue with Google",
            modifier = Modifier
                .size(45.dp)
                .clickable {
                    onClickRegister()
                },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginBody(
    )
}