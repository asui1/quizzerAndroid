package com.asu1.quizzer.composables.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.screens.mainScreen.userDataTest
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.UserViewModel

@Composable
fun InquiryBottomSheetContent(
    onDismissRequest: () -> Unit,
    userData: UserViewModel.UserDatas?,
    isDone: Boolean,
    onSendInquiry: (String, String, String) -> Unit
) {
    val email = userData?.email ?: "GUEST"
    val options = listOf(stringResource(R.string.bug_report),
        stringResource(R.string.quiz_report), stringResource(R.string.user_request), stringResource(
            R.string.others
        )
    )
    var selectedOption by remember { mutableStateOf(options[0]) }
    var textFieldValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(isDone) {
        if (isDone) {
            onDismissRequest()
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Text(stringResource(id = R.string.inquiry), style = MaterialTheme.typography.headlineSmall)
        Text(stringResource(R.string.inquiry_body), style = MaterialTheme.typography.bodySmall)
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedOption)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.inquiry_dropdown)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(text = option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    },
                )
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text(stringResource(R.string.enter_text)) },
            minLines = 1,
            maxLines = 5,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = {
                onSendInquiry(email, selectedOption, textFieldValue)
            })
        )
        TextButton(onClick = {
            onSendInquiry(email, selectedOption, textFieldValue) },
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InquiryBottomSheetContentPreview() {
    QuizzerAndroidTheme {
        InquiryBottomSheetContent(
            onDismissRequest = { },
            userData = userDataTest,
            isDone = false,
            onSendInquiry = { _, _, _ -> }
        )
    }
}