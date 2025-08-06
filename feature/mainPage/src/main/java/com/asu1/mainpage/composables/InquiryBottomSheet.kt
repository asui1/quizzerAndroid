package com.asu1.mainpage.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.mainpage.viewModels.sampleUserData
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R

@Composable
fun InquiryBottomSheetContent(
    onDismissRequest: () -> Unit,
    userData: UserViewModel.UserData?,
    isDone: Boolean,
    onSendInquiry: (String, String, String) -> Unit
) {
    val email = userData?.email ?: "GUEST"
    val options = listOf(
        R.string.user_request,
        R.string.bug_report,
        R.string.quiz_report,
        R.string.others,
    ).map { stringResource(it) }

    var selectedOption by remember { mutableStateOf(options[0]) }
    var inquiryText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(isDone) {
        if (isDone) onDismissRequest()
    }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        InquiryHeader()
        Spacer(Modifier.height(8.dp))

        InquiryTypeSelector(
            selected = selectedOption,
            options = options,
            expanded = expanded,
            onExpandChange = { expanded = it },
            onOptionSelected = {
                selectedOption = it
                expanded = false
            }
        )
        Spacer(Modifier.height(8.dp))

        InquiryTextInput(
            text = inquiryText,
            onTextChange = { inquiryText = it },
            onSend = { onSendInquiry(email, selectedOption, inquiryText) }
        )
        Spacer(Modifier.height(16.dp))

        InquirySubmitButton(
            onClick = { onSendInquiry(email, selectedOption, inquiryText) }
        )
    }
}

@Composable
private fun InquiryHeader() {
    Text(
        text = stringResource(R.string.inquiry),
        style = QuizzerTypographyDefaults.quizzerHeadlineSmallBold
    )
    Spacer(Modifier.height(4.dp))
    Text(
        text = stringResource(R.string.inquiry_body),
        style = QuizzerTypographyDefaults.quizzerBodySmallNormal
    )
}

@Composable
private fun InquiryTypeSelector(
    selected: String,
    options: List<String>,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { onExpandChange(true) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selected, style = QuizzerTypographyDefaults.quizzerBodySmallBold)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandChange(false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onOptionSelected(option) }
                )
            }
        }
    }
}

@Composable
private fun InquiryTextInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(stringResource(R.string.enter_inquiry)) },
        modifier = Modifier
            .fillMaxWidth(),
        minLines = 1,
        maxLines = 5,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = { onSend() })
    )
}

@Composable
private fun InquirySubmitButton(
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .navigationBarsPadding(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = stringResource(R.string.submit),
            style = QuizzerTypographyDefaults.quizzerLabelSmallMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InquiryBottomSheetContentPreview() {
    QuizzerAndroidTheme {
        InquiryBottomSheetContent(
            onDismissRequest = { },
            userData = sampleUserData,
            isDone = false,
            onSendInquiry = { _, _, _ -> }
        )
    }
}
