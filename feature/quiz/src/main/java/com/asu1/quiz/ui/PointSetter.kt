package com.asu1.quiz.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.resources.R

@Composable
fun PointSetter(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(stringResource(R.string.points))
        TextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.points)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PointSetterPreview() {
    com.asu1.resources.QuizzerAndroidTheme {
        PointSetter()
    }
}