package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.ImageGetter
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme

@Composable
fun QuizLayoutSetTitleImage(quizTitleImage: ByteArray? = byteArrayOf(), onImageChange: (ByteArray) -> Unit =  {}) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.set_quiz_image_80_kb),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.size(16.dp))
        ImageGetter(
            image = quizTitleImage ?: ByteArray(0),
            onImageUpdate = { image ->
                onImageChange(image)
            },
            onImageDelete = {
                onImageChange(ByteArray(0))
            },
            width = 200.dp,
            height = 200.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutSetTitleImagePreview() {
    QuizzerAndroidTheme {
        QuizLayoutSetTitleImage(
        )
    }
}
