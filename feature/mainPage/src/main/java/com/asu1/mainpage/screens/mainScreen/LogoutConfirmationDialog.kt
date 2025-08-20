package com.asu1.mainpage.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.customComposable.dialog.DialogComposable
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    DialogComposable(
        title = R.string.logout_confirmation,
        message = R.string.logout_confirmation_body,
        onContinue = { onConfirm() },
        onContinueText = R.string.logout_confirm,
        onCancel = onDismiss,
        onCancelText = R.string.logout_cancel
    )
}

@Preview
@Composable
fun LogoutConfirmationDialogPreview(
) {
    QuizzerAndroidTheme {
        LogoutConfirmationDialog(
            onConfirm = { },
            onDismiss = { }
        )
    }
}
