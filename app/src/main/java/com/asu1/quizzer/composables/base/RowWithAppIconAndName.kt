package com.asu1.quizzer.composables.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.quizzer.composables.QuizzerTopBarBase
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun RowWithAppIconAndName(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit = {},
    text: String = "Quizzer",
) {
    QuizzerTopBarBase(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        header = header,
        body = {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall
            )
        },
    )
}

@Preview
@Composable
fun PreviewRowWithAppIconAndName(){
    QuizzerAndroidTheme {
        RowWithAppIconAndName(
            header = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.move_back_home)
                    )
                }
            }
        )
    }
}
