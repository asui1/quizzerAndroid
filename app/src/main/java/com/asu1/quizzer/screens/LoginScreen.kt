package com.asu1.quizzer.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.network.SecurePreferences
import com.asu1.quizzer.states.LoginActivityState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch

fun handleSignIn(result: GetCredentialResponse, login: (String, String) -> Unit) {
    // Handle the successfully returned credential.
    when (val credential = result.credential) {
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val email = googleIdTokenCredential.data.getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID")
                    Logger().debug("Email: $email")
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
fun LoginScreen(navController: NavController, loginActivityState: LoginActivityState) {
    val context = LocalContext.current
    val isUserLoggedIn by loginActivityState.isUserLoggedIn

    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.popBackStack()
        }
    }

    val credentialManager = CredentialManager.create(context)
    val coroutineScope = rememberCoroutineScope()

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(SecurePreferences.GOOGLE_CLIENT_ID)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

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
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = "Sign in",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            coroutineScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                    handleSignIn(result, loginActivityState.login)

                } catch (e: GetCredentialException) {
                    Log.e("Quizzer", "Error getting credential", e)
                }
            }
        },
            modifier = Modifier.width(250.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.android_neutral_rd_si), // Replace with your drawable resource
                contentDescription = "Sign in with Google",
                modifier = Modifier.width(250.dp)
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            coroutineScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(result.credential.data)

                    val email = googleIdTokenCredential.data.getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID")
                    if(email == null) {
                        Log.e("Quizzer", "Received an invalid google id token response")

                        return@launch
                    }
                    val profileUri = googleIdTokenCredential.profilePictureUri

                    NavMultiClickPreventer.navigate(navController, Route.RegisterPolicyAgreement(email,
                        profileUri.toString()
                    ))
                } catch (e: GetCredentialException) {
                    Log.e("Quizzer", "Error getting credential", e)
                }
            }
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.android_neutral_sq_ctn), // Replace with your drawable resource
                contentDescription = "Continue with Google",
                modifier = Modifier.width(130.dp),
                contentScale = ContentScale.Fit
            )
        }
    }


}

@Composable
fun getLoginActivityState(): LoginActivityState {
    return LoginActivityState(
        isUserLoggedIn = rememberSaveable { mutableStateOf(true) },
        userData = rememberSaveable()  { mutableStateOf(userDataTest) },
        login = { _, _ -> },
        logout = { },
        signout = { },
    )
}

@Preview
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    val loginActivityState = getLoginActivityState()
    QuizzerAndroidTheme {
        LoginScreen(
            navController = navController,
            loginActivityState = loginActivityState
        )
    }
}