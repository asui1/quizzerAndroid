package com.asu1.quizzer.screens.quizlayout

import android.graphics.BitmapFactory
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.util.launchPhotoPicker
import com.asu1.quizzer.screens.getQuizLayoutState
import com.asu1.quizzer.states.QuizLayoutState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import loadImageAsByteArray

@Composable
fun QuizLayoutSetTitleImage(quizLayoutState: QuizLayoutState, proceed: () -> Unit) {
    val context = LocalContext.current
    val titleImage by quizLayoutState.quizImage
    val imageBitmap: ImageBitmap = titleImage?.let { byteArray ->
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    } ?: loadImageAsByteArray(context, R.drawable.question2).let { byteArray ->
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    }
    val photoPickerLauncher = launchPhotoPicker(context) { byteArray ->
        quizLayoutState.setQuizImage(byteArray)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    })
            }
    ) {
        Text(
            text = "Set Quiz Image",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.size(16.dp))
        if(titleImage != null){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Quiz Title Image",
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                )
            }
        } else {
            // Display the image picker

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Add Photo Icon",
                    modifier = Modifier.size(48.dp)
                )
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Sample Image",
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutSetTitleImagePreview() {
    QuizzerAndroidTheme {
        QuizLayoutSetTitleImage(
            quizLayoutState = getQuizLayoutState(),
            proceed = {},
        )
    }
}
