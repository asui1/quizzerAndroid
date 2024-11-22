package com.asu1.quizzer.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R

@Composable
fun SaveButton(onSave: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                onSave()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .testTag("QuizCreatorSaveButton")
        ) {
            Text(stringResource(R.string.save))
        }
    }
}