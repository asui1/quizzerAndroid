package com.asu1.quizzer.composables

import android.content.Context
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme

@Composable
fun DialogComposable(titleResource: Int, messageResource: Int, onContinue: (Context) -> Unit, onContinueResource: Int, onCancel: () -> Unit, onCancelResource: Int){
    val context = LocalContext.current
    val width = 100.dp
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = stringResource(titleResource),
            style = MaterialTheme.typography.headlineSmall,
            ) },
        text = { Text(stringResource(messageResource),
            style  = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            Button(
                onClick = { onContinue(context) },
                modifier = Modifier.width(width * 1.5f),
            ) {
                Text(stringResource(onContinueResource))
            }
        },
        dismissButton ={
            Button(
                onClick = { onCancel() },
                modifier = Modifier.width(width),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            ) {
                Text(stringResource(onCancelResource),
                    color = MaterialTheme.colorScheme.error,
                )
            }

        }
    )
}

@Composable
fun NoInternetDialog(onRetry: (Context) -> Unit, onExit: () -> Unit) {
    DialogComposable(
        titleResource = R.string.no_internet_connection,
        messageResource = R.string.please_check_your_internet_connection_and_try_again,
        onContinue = onRetry,
        onContinueResource = R.string.retry,
        onCancel = onExit,
        onCancelResource = R.string.exit
    )
}

@Composable
fun UpdateDialog(onUpdate: (Context) -> Unit, onCancel: () -> Unit) {
    DialogComposable(
        titleResource = R.string.update_available,
        messageResource = R.string.a_new_update_is_available_would_you_like_to_update_now,
        onContinue = onUpdate,
        onContinueResource = R.string.update,
        onCancel = onCancel,
        onCancelResource = R.string.exit
    )
}

@Preview
@Composable
fun NoInternetDialogPreview() {
    QuizzerAndroidTheme {
        NoInternetDialog(
            onRetry = {},
            onExit = {}
        )
    }
}

@Preview
@Composable
fun UpdateDialogPreview() {
    QuizzerAndroidTheme {
        UpdateDialog(
            onUpdate = {},
            onCancel = {}
        )
    }
}