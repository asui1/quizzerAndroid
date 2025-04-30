package com.asu1.mainpage.composables

import SnackBarManager
import ToastType
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.asu1.resources.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleCredentialManager(
    val context: Context
) {
    private val credentialManager : CredentialManager = CredentialManager.create(context)
    private val loginGoogleIdOption : GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setAutoSelectEnabled(true)
//        .setServerClientId("1057835741492-4kv4svps22604n2nv0k9dj1m0vf7dokf.apps.googleusercontent.com")
        .setServerClientId(com.asu1.network.SecurePreferences.GOOGLE_CLIENT_ID)
        .build()
    private val loginRequest : GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(loginGoogleIdOption)
        .build()
    private val registerGoogleIdOption : GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(true)
//        .setServerClientId("1057835741492-4kv4svps22604n2nv0k9dj1m0vf7dokf.apps.googleusercontent.com")
        .setServerClientId(com.asu1.network.SecurePreferences.GOOGLE_CLIENT_ID)
        .build()
    private val registerRequest : GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(registerGoogleIdOption)
        .build()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    fun requestLogin(login: (email: String, profileUri: String) -> Unit) {
        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = loginRequest,
                    context = context,
                )
                handleSignIn(result) { email, profileUri -> login(email, profileUri) }
            } catch (e: GetCredentialException) {
                SnackBarManager.showSnackBar(R.string.failed_login, ToastType.ERROR)
                Log.e("Quizzer", "Error getting credential", e)
            }
        }
    }

    fun requestRegister(register: (email: String, profileUri: String) -> Unit) {
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
                    SnackBarManager.showSnackBar(R.string.failed_login, ToastType.ERROR)

                    return@launch
                }
                val profileUri = googleIdTokenCredential.profilePictureUri
                register(email, profileUri.toString())
            } catch (e: GetCredentialException) {
                SnackBarManager.showSnackBar(R.string.failed_login, ToastType.ERROR)
                Log.e("Quizzer", "Error getting credential", e)
            }
        }
    }

    fun handleSignIn(result: GetCredentialResponse, login: (email: String, profileUri: String) -> Unit) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)

                        val email = googleIdTokenCredential.data.getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID")
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
}