package com.asu1.mainpage.composables

import androidx.credentials.exceptions.*
import com.asu1.utils.Logger
import com.asu1.resources.R

suspend inline fun <T> runCredentialFlow(
    flow: String,
    crossinline block: suspend () -> T
): T? = try {
    block()
} catch (e: GetCredentialException) {
    handleCredentialException(flow, e)
    null
}

fun handleCredentialException(flow: String, e: GetCredentialException) {
    when (e) {
        is GetCredentialCancellationException -> {
            Logger.debug("Quizzer", "$flow canceled by user")
            // no snackbar
        }
        is GetCredentialInterruptedException -> {
            Logger.debug("Quizzer", "$flow interrupted: $e")
            SnackBarManager.showSnackBar(R.string.failed_login, ToastType.INFO)
        }
        is GetCredentialProviderConfigurationException -> {
            Logger.debug("Quizzer", "Credential provider misconfigured: $e")
            SnackBarManager.showSnackBar(R.string.failed_login, ToastType.ERROR)
        }
        else -> {
            Logger.debug("Quizzer", "$flow error (generic): $e")
            SnackBarManager.showSnackBar(R.string.failed_login, ToastType.ERROR)
        }
    }
}
