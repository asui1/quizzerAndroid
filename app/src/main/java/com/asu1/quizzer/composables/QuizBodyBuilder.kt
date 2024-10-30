package com.asu1.quizzer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme

@Composable
fun QuizBodyBuilder(
    bodyState: BodyType,
    updateBody: (BodyType) -> Unit,
    bodyText: String,
    onBodyTextChange: (String) -> Unit,
    imageBytes: ByteArray?,
    onImageSelected: (ByteArray) -> Unit,
    youtubeId: String,
    youtubeStartTime: Int,
    onYoutubeUpdate: (String, Int) -> Unit,
    onRemoveBody: () -> Unit = {},
){
    var showBodyDialog by remember { mutableStateOf(false) }

    if (showBodyDialog) {
        BodyTypeDialog(
            onDismissRequest = { showBodyDialog = false },
            onTextSelected = {
                updateBody(BodyType.TEXT)
                showBodyDialog = false
            },
            onImageSelected = {
                updateBody(BodyType.IMAGE)
                showBodyDialog = false
            },
            onYoutubeSelected = {
                updateBody(BodyType.YOUTUBE)
                showBodyDialog = false
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if(bodyState != BodyType.NONE) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onRemoveBody,
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircleOutline,
                        contentDescription = "Delete Body"
                    )
                }
            }
        }
        when(bodyState){
            BodyType.NONE -> {
                TextButton(
                    onClick = {showBodyDialog = true},
                ) {
                    Text("Add Body")
                }
            }
            BodyType.TEXT -> {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = bodyText,
                    onValueChange = {it ->
                        onBodyTextChange(it)
                    },
                    label = { Text("Body Text") }
                )
            }
            BodyType.IMAGE -> {
                ImageGetter(
                    image = imageBytes ?: ByteArray(0),
                    onImageUpdate = onImageSelected,
                    onImageDelete = { onImageSelected(ByteArray(0)) },
                    width = 200.dp,
                    height = 200.dp
                )
            }
            BodyType.YOUTUBE -> {
                YoutubeLinkInput(
                    youtubeId = youtubeId,
                    startTime = youtubeStartTime,
                    onYoutubeUpdate = onYoutubeUpdate
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BodyPreviews() {
    QuizzerAndroidTheme {
        Column(){
            QuizBodyBuilder(
                bodyState = BodyType.TEXT,
                updateBody = {},
                bodyText = "Body Text",
                onBodyTextChange = {},
                imageBytes = null,
                onImageSelected = {},
                youtubeId = "",
                youtubeStartTime = 0,
                onYoutubeUpdate = { _, _ -> }
            )
            Spacer(modifier = Modifier.height(8.dp).background(color = Color.Black))
            QuizBodyBuilder(
                bodyState = BodyType.IMAGE,
                updateBody = {},
                bodyText = "Body Text",
                onBodyTextChange = {},
                imageBytes = null,
                onImageSelected = {},
                youtubeId = "",
                youtubeStartTime = 0,
                onYoutubeUpdate = { _, _ -> }
            )
            Spacer(modifier = Modifier.height(8.dp).background(color = Color.Black))
            QuizBodyBuilder(
                bodyState = BodyType.YOUTUBE,
                updateBody = {},
                bodyText = "Body Text",
                onBodyTextChange = {},
                imageBytes = null,
                onImageSelected = {},
                youtubeId = "",
                youtubeStartTime = 0,
                onYoutubeUpdate = { _, _ -> }
            )
        }

    }
}

@Composable
fun BodyTypeDialog(
    onDismissRequest: () -> Unit,
    onTextSelected: () -> Unit,
    onImageSelected: () -> Unit,
    onYoutubeSelected: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Body Type") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(onClick = onTextSelected) {
                    Text("Text")
                }
                TextButton(onClick = onImageSelected) {
                    Text("Image")
                }
                TextButton(onClick = onYoutubeSelected) {
                    Text("Youtube")
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}
@Preview(showBackground = true)
@Composable
fun BodyDialogPreview() {
    QuizzerAndroidTheme {
        BodyTypeDialog(
            onDismissRequest = {},
            onTextSelected = {},
            onImageSelected = {},
            onYoutubeSelected = {}
        )
    }
}