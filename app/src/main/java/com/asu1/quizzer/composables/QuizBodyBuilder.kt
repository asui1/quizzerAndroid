package com.asu1.quizzer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.custombuttons.IconButtonWithText
import com.asu1.models.serializers.BodyType
import com.asu1.resources.R
import com.asu1.utils.Logger

@Composable
fun QuizBodyBuilder(
    bodyState: BodyType,
    updateBody: (BodyType) -> Unit,
){
    var showBodyDialog by remember { mutableStateOf(false) }

    if (showBodyDialog) {
        BodyTypeDialog(
            onDismissRequest = { showBodyDialog = false },
            onBodyTypeSelected = {bodyState ->
                updateBody(bodyState)
                showBodyDialog = false
            },
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
                        updateBody(
                            BodyType.TEXT(it)
                        )
                    },
                    label = { Text(stringResource(R.string.body_text)) }
                )
            }
            is BodyType.IMAGE -> {
                ImageGetter(
                    image = bodyState.bodyImage,
                    onImageUpdate = { bitmap ->
                        updateBody(
                            BodyType.IMAGE(bitmap)
                        )
                    },
                )
            }
            is BodyType.YOUTUBE -> {
                YoutubeLinkInput(
                    youtubeId = bodyState.youtubeId,
                    startTime = bodyState.youtubeStartTime,
                    onYoutubeUpdate = { youtubeId, startTime ->
                        updateBody(
                            BodyType.YOUTUBE(youtubeId, startTime)
                        )
                    }
                )
            }
            is BodyType.CODE -> {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("QuizCreatorBodyTextField"),
                    value = bodyState.code,
                    onValueChange = {it ->
                        updateBody(
                            BodyType.CODE(it)
                        )
                    },
                    label = { Text(stringResource(R.string.code)) }
                )
            }
            is BodyType.NONE -> {
                TextButton(
                    modifier = Modifier.testTag("QuizCreatorAddBodyButton"),
                    onClick = {showBodyDialog = true},
                ) {
                    Text(stringResource(R.string.add_body))
                }
            }
            else ->{
                Logger.debug("QUIZBODYBUILDER", "NOT IMPLEMENTED BODY TYPE UI")
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
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .background(color = Color.Black)
            )
            QuizBodyBuilder(
                bodyState = BodyType.IMAGE(),
                updateBody = {},
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .background(color = Color.Black)
            )
            QuizBodyBuilder(
                bodyState = BodyType.YOUTUBE("", 0),
                updateBody = {},
            )
        }

    }
}

@Composable
fun BodyTypeDialog(
    onDismissRequest: () -> Unit,
    onBodyTypeSelected: (BodyType) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.select_body_type)) },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Ensures 3 items per row
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                state = rememberLazyGridState(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(BodyType.values) { bodyType ->
                    IconButtonWithText(
                        imageVector = bodyType.icon,
                        text = stringResource(bodyType.labelRes),
                        onClick = { onBodyTypeSelected(bodyType) },
                        modifier = Modifier.testTag("BodyTypeDialog${bodyType::class.simpleName}Button"),
                        description = stringResource(bodyType.labelRes),
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
            onBodyTypeSelected = {},
        )
    }
}