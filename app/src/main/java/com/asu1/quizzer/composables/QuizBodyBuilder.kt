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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VideoLibrary
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.serializers.BodyType
import com.asu1.quizzer.R
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun QuizBodyBuilder(
    bodyState: BodyType,
    updateBody: (BodyType) -> Unit,
    onBodyTextChange: (String) -> Unit,
    onImageSelected: (ByteArray) -> Unit,
    onYoutubeUpdate: (String, Int) -> Unit,
){
    var showBodyDialog by remember { mutableStateOf(false) }

    if (showBodyDialog) {
        BodyTypeDialog(
            onDismissRequest = { showBodyDialog = false },
            onTextSelected = {
                updateBody(BodyType.TEXT(""))
                showBodyDialog = false
            },
            onImageSelected = {
                updateBody(BodyType.IMAGE(ByteArray(0)))
                showBodyDialog = false
            },
            onYoutubeSelected = {
                updateBody(BodyType.YOUTUBE("", 0))
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
                    onClick = {
                        updateBody(BodyType.NONE)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircleOutline,
                        contentDescription = stringResource(R.string.delete_body)
                    )
                }
            }
        }
        when(bodyState){
            is BodyType.TEXT -> {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("QuizCreatorBodyTextField"),
                    value = bodyState.bodyText,
                    onValueChange = {it ->
                        onBodyTextChange(it)
                    },
                    label = { Text(stringResource(R.string.body_text)) }
                )
            }
            is BodyType.IMAGE -> {
                ImageGetter(
                    image = bodyState.bodyImage,
                    onImageUpdate = onImageSelected,
                    onImageDelete = { onImageSelected(ByteArray(0)) },
                )
            }
            is BodyType.YOUTUBE -> {
                YoutubeLinkInput(
                    youtubeId = bodyState.youtubeId,
                    startTime = bodyState.youtubeStartTime,
                    onYoutubeUpdate = onYoutubeUpdate
                )
            }
            else -> {
                TextButton(
                    modifier = Modifier.testTag("QuizCreatorAddBodyButton"),
                    onClick = {showBodyDialog = true},
                ) {
                    Text(stringResource(R.string.add_body))
                }
            }


        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BodyPreviews() {
    com.asu1.resources.QuizzerAndroidTheme {
        Column() {
            QuizBodyBuilder(
                bodyState = BodyType.TEXT(""),
                updateBody = {},
                onBodyTextChange = {},
                onImageSelected = {},
                onYoutubeUpdate = { _, _ -> }
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .background(color = Color.Black)
            )
            QuizBodyBuilder(
                bodyState = BodyType.IMAGE(ByteArray(0)),
                updateBody = {},
                onBodyTextChange = {},
                onImageSelected = {},
                onYoutubeUpdate = { _, _ -> }
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .background(color = Color.Black)
            )
            QuizBodyBuilder(
                bodyState = BodyType.YOUTUBE("", 0),
                updateBody = {},
                onBodyTextChange = {},
                onImageSelected = {},
                onYoutubeUpdate = { _, _ -> }
            )
        }

    }
}


val mapItemToVector = mapOf(
    1 to Icons.Default.TextFields,
    2 to Icons.Default.Image,
    3 to Icons.Default.VideoLibrary,
)

@Composable
fun BodyTypeDialog(
    onDismissRequest: () -> Unit,
    onTextSelected: () -> Unit,
    onImageSelected: () -> Unit,
    onYoutubeSelected: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.select_body_type)) },
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    modifier = Modifier.testTag("BodyTypeDialogTextButton"),
                    onClick = onTextSelected
                ) {
                    Icon(
                        imageVector = Icons.Default.TextFields, // Replace with appropriate icon
                        contentDescription = stringResource(R.string.body_text)
                    )
                }
                IconButton(
                    modifier = Modifier.testTag("BodyTypeDialogImageButton"),
                    onClick = onImageSelected
                ) {
                    Icon(
                        imageVector = Icons.Default.Image, // Replace with appropriate icon
                        contentDescription = stringResource(R.string.add_image)
                    )
                }
                IconButton(
                    modifier = Modifier.testTag("BodyTypeDialogYoutubeButton"),
                    onClick = onYoutubeSelected
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = "Youtube"
                    )
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
    com.asu1.resources.QuizzerAndroidTheme {
        BodyTypeDialog(
            onDismissRequest = {},
            onTextSelected = {},
            onImageSelected = {},
            onYoutubeSelected = {}
        )
    }
}