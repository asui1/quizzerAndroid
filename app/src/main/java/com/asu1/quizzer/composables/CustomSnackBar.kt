package com.asu1.quizzer.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun CustomSnackbarInfo(
    message: String,
) {
    val backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
    val contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    val borderColor = MaterialTheme.colorScheme.outline

    DrawSnackbar(backgroundColor, borderColor, message, contentColor)
}

@Composable
fun CustomSnackbarError(
    message: String,
) {
    val backgroundColor = MaterialTheme.colorScheme.errorContainer
    val contentColor = MaterialTheme.colorScheme.onErrorContainer
    val borderColor = MaterialTheme.colorScheme.error

    DrawSnackbar(backgroundColor, borderColor, message, contentColor)
}

@Composable
fun CustomSnackbarSuccess(
    message: String,
) {
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    val borderColor = MaterialTheme.colorScheme.outline

    DrawSnackbar(backgroundColor, borderColor, message, contentColor)
}
@Composable
private fun DrawSnackbar(
    backgroundColor: Color,
    borderColor: Color,
    message: String,
    contentColor: Color
) {
    Snackbar(
        containerColor = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp),
            ),
        action = {
        }
    ) {
        Text(
            text = message,
        )
    }
}

@Composable
fun CustomSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 0.2f * screenHeight, start = 32.dp, end = 32.dp)
            .imePadding(),
    ) {
        SnackbarHost(
            hostState = hostState,
            modifier = modifier,
            snackbar = { snackbarData ->
                val option = snackbarData.visuals.message[0]
                val message = snackbarData.visuals.message.substring(1)
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    when (option) {
                        'S' -> CustomSnackbarSuccess(message = message)
                        'E' -> CustomSnackbarError(message = message)
                        'I' -> CustomSnackbarInfo(message = message)
                        else -> CustomSnackbarInfo(message = message)
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomSnackbarInfoPreview() {
    CustomSnackbarInfo("This is a preview")
}

@Preview(showBackground = true)
@Composable
fun CustomSnackbarErrorPreview() {
    CustomSnackbarError("This is a preview")
}

@Preview(showBackground = true)
@Composable
fun CustomSnackbarSuccessPreview() {
    CustomSnackbarSuccess("This is a preview")
}
