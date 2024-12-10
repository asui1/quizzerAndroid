package com.asu1.quizzer.composables.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.userDataTest
import com.asu1.quizzer.viewModels.UserViewModel

@Composable
fun SignoutBottomSheetContent(
    onDismissRequest: () -> Unit,
    userData: UserViewModel.UserDatas?,
    isDone: Boolean = false,
    onSendSignOut: (String) -> Unit = { },
){
    var textFieldValue by remember { mutableStateOf("") }
    val email = userData?.email ?: "GUEST"

    LaunchedEffect(isDone) {
        if (isDone) {
            onDismissRequest()
        }
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(stringResource(id = R.string.sign_out), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text("If you sign out, all your activities and records will be deleted. Even if you re-register, data won't be restored", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text(stringResource(R.string.enter_sign_out)) },
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = {
            if(textFieldValue == "Sign Out" || textFieldValue == "회원 탈퇴") {
                onSendSignOut(email)
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignoutBottomSheetContentPreview() {
    QuizzerAndroidTheme {
        SignoutBottomSheetContent(
            onDismissRequest = { },
            userData = userDataTest,
            isDone = false,
            onSendSignOut = { },
        )
    }
}