package com.asu1.quizzer.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSetter(
    tags: List<String>?,
    onClickRemove: (String) -> Unit,
    onClickAdd: (String) -> Unit,
    focusRequester: androidx.compose.ui.focus.FocusRequester
) {
    var tag by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Text(
            text = "Select tags that interest you",
            style = MaterialTheme.typography.titleMedium,
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            tags?.forEach { tag ->
                Button(
                    onClick = {
                        onClickRemove(tag)
                              },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                ) {
                    Text(text = tag)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tag,
            onValueChange = { tag = it },
            label = { Text("Enter Tag") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (tag.isNotEmpty()) {
                        onClickAdd(tag)
                        tag = ""
                    }
                }
            )
        )
    }
}