package com.asu1.mainpage.composables

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
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
        .setServerClientId(com.asu1.network.SecurePreferences.GOOGLE_CLIENT_ID)
        .build()
    private val loginRequest : GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(loginGoogleIdOption)
        .build()
    private val registerGoogleIdOption : GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(true)
        .setServerClientId(com.asu1.network.SecurePreferences.GOOGLE_CLIENT_ID)
        .build()
    private val registerRequest : GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(registerGoogleIdOption)
        .build()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    fun requestLogin(login: (email: String, profileUri: String) -> Unit) {
        coroutineScope.launch {
            runCredentialFlow(flow = "Login") {
                val result = credentialManager.getCredential(request = loginRequest, context = context)
                handleSignIn(result) { email, profileUri -> login(email, profileUri) }
            }
        }
    }

    fun requestRegister(register: (email: String, profileUri: String) -> Unit) {
        coroutineScope.launch {
            runCredentialFlow(flow = "Register") {
                val result = credentialManager.getCredential(
                    request = registerRequest,
                    context = context,
                )
                val cred = GoogleIdTokenCredential.createFrom(result.credential.data)
                val accountId = cred.id
                val profileUri = cred.profilePictureUri?.toString().orEmpty()
                register(accountId, profileUri)
            }
        }
    }

    fun handleSignIn(
        result: GetCredentialResponse,
        login: (email: String, profileUri: String) -> Unit
    ) {
        val raw = result.credential
        if (raw is CustomCredential &&
            raw.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            // 1) Parse token (logs & yields null on failure)
            val idToken = try {
                GoogleIdTokenCredential.createFrom(raw.data)
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("Quizzer", "Failed to parse Google ID token", e)
                null
            }

            if (idToken != null) {
                // 2) Extract & validate email
                val email = idToken.data
                    .getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID")

                if (!email.isNullOrBlank()) {
                    // 3) Happy path
                    val profileUri = idToken.profilePictureUri?.toString().orEmpty()
                    login(email, profileUri)
                } else {
                    Log.e(
                        "Quizzer",
                        "Received invalid Google ID token response: missing or blank email"
                    )
                }
            }
        } else {
            Log.e("Quizzer", "Unexpected credential or type: $raw")
        }
    }
}
