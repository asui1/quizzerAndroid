package com.asu1.customdialogs

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerTypographyDefaults

@Composable
fun DialogComposable(title: Int, message: Int, onContinue: (Context) -> Unit,
                     onContinueText: Int, onCancel: () -> Unit, onCancelText: Int,
                     width: Dp = 100.dp) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(
            text = stringResource(title),
            style = QuizzerTypographyDefaults.quizzerTopBarTitle,
        ) },
        text = {
            Text(
                stringResource(message),
                style  = QuizzerTypographyDefaults.quizzerBodyMedium,
            )
        },
        confirmButton = {
            Button(
                onClick = { onContinue(context) },
                modifier = Modifier.width(width * 1.5f),
            ) {
                Text(
                    stringResource(onContinueText),
                    style  = QuizzerTypographyDefaults.quizzerQuizCardDescription,
                )
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
                Text(
                    stringResource(onCancelText),
                    style  = QuizzerTypographyDefaults.quizzerQuizCardDescription,
                    color = MaterialTheme.colorScheme.error,
                )
            }

        }
    )
}