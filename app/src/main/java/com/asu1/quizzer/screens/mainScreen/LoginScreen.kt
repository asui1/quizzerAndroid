package com.asu1.quizzer.screens.mainScreen

import ToastManager
import ToastType
import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavController
import com.asu1.custombuttons.TextDivider
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.UserViewModel
import com.asu1.resources.R
import com.asu1.utils.Logger
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch

fun handleSignIn(result: GetCredentialResponse, login: (email: String, profileUri: String) -> Unit) {
    // Handle the successfully returned credential.
    when (val credential = result.credential) {
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    Logger.debug("Received google id token credential")
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val email = googleIdTokenCredential.data.getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID")
                    Logger.debug("Received email: $email")
                    if(email == null) {
                        Log.e("Quizzer", "Received an invalid google id token response")
                        return
                    }
                    val profileUri = googleIdTokenCredential.profilePictureUri
                    login(email, profileUri.toString())



                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("Quizzer", "Received an invalid google id token response", e)
                }
            } else {
                // Catch any unrecognized custom credential type here.
                Log.e("Quizzer", "Unexpected type of credential")
            }
        }

        else -> {
            // Catch any unrecognized credential type here.
            Log.e("Quizzer", "Unexpected type of credential")
        }
    }
}

@Composable
fun LoginScreen(navController: NavController,
                userViewModel: UserViewModel
) {
    val context = LocalContext.current
    val isUserLoggedIn by userViewModel.isUserLoggedIn.observeAsState(false)

    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.popBackStack()
        }
    }

    val credentialManager = CredentialManager.create(context)
    val coroutineScope = rememberCoroutineScope()

    val loginGoogleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setAutoSelectEnabled(true)
        .setServerClientId(com.asu1.network.SecurePreferences.GOOGLE_CLIENT_ID)
        .build()

    val loginRequest: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(loginGoogleIdOption)
        .build()

    val registerGoogleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(true)
        .setServerClientId(com.asu1.network.SecurePreferences.GOOGLE_CLIENT_ID)
        .build()

    val registerRequest: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(registerGoogleIdOption)
        .build()

    LoginBody(
        onClickSignin = {
            coroutineScope.launch {
                try {
                    Logger.debug("Getting Login credential")
                    val result = credentialManager.getCredential(
                        request = loginRequest,
                        context = context,
                    )
                    Logger.debug("Received credential: $result")
                    handleSignIn(result, {email, profileUri ->  userViewModel.login(email)})

                } catch (e: GetCredentialException) {
                    ToastManager.showToast(R.string.failed_login, ToastType.ERROR)
                    Log.e("Quizzer", "Error getting credential", e)
                }
            }
        },
        onClickRegister = {
            coroutineScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = registerRequest,
                        context = context,
                    )
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(result.credential.data)

                    val email =
                        googleIdTokenCredential.data.getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID")
                    if (email == null) {
                        ToastManager.showToast(R.string.failed_login, ToastType.ERROR)

                        return@launch
                    }
                    val profileUri = googleIdTokenCredential.profilePictureUri

                    navController.navigate(
                        Route.Register(
                            email,
                            profileUri.toString()
                        )
                    ){
                        launchSingleTop = true
                    }
                } catch (e: GetCredentialException) {
                    ToastManager.showToast(R.string.failed_login, ToastType.ERROR)
                    Log.e("Quizzer", "Error getting credential", e)
                }
            }
        }
    )
}

@Composable
private fun LoginBody(
    onClickSignin: () -> Unit = {},
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
            modifier = Modifier.fillMaxWidth(0.8f),
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        TextDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.android_neutral_rd_si),
            contentDescription = "Sign in with Google",
            modifier = Modifier
                .clickable{
                    onClickSignin()
                },
        )
        Spacer(modifier = Modifier.height(32.dp))
        TextDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = {
                Text(
                    text = stringResource(R.string.register),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.android_neutral_rd_na), // Replace with your drawable resource
            contentDescription = "Continue with Google",
            modifier = Modifier
                .size(30.dp)
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