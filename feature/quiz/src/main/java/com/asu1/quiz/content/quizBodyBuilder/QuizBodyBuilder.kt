package com.asu1.quiz.content.quizBodyBuilder

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.asu1.customComposable.button.IconButtonWithText
import com.asu1.customComposable.imageGetter.ImageGetter
import com.asu1.models.serializers.BodyType
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun QuizBodyBuilder(
    bodyState: BodyType,
    updateBody: (BodyType) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    // 1) show body-type picker dialog
    if (showDialog) {
        BodyTypeDialog(
            onDismissRequest = { showDialog = false },
            onBodyTypeSelected = { type ->
                updateBody(type)
                showDialog = false
            }
        )
    }

    // 2) main content container
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // 2a) the content for each body type
        Box(modifier = Modifier.matchParentSize()) {
            BodyTypeContent(
                bodyState = bodyState,
                updateBody = updateBody,
                onRequestDialog = { showDialog = true }
            )
        }
        // 2b) delete button overlay
        if (bodyState != BodyType.NONE) {
            DeleteBodyButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onDelete = { updateBody(BodyType.NONE) })
        }
    }
}

@Composable
private fun BodyTypeContent(
    bodyState: BodyType,
    updateBody: (BodyType) -> Unit,
    onRequestDialog: () -> Unit
) {
    when (bodyState) {
        is BodyType.TEXT -> TextBodyField(bodyState.bodyText) { updateBody(BodyType.TEXT(it)) }
        is BodyType.CODE -> TextBodyField(bodyState.code)     { updateBody(BodyType.CODE(it)) }
        is BodyType.IMAGE -> ImageBodyField(bodyState.bodyImage) { updateBody(BodyType.IMAGE(it)) }
        is BodyType.YOUTUBE -> YouTubeBodyField(
            id = bodyState.youtubeId,
            start = bodyState.youtubeStartTime
        ) { id, start -> updateBody(BodyType.YOUTUBE(id, start)) }
        BodyType.NONE       -> AddBodyButton(onClick = onRequestDialog)
    }
}

@Composable
private fun TextBodyField(
    text: String,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("QuizCreatorBodyTextField"),
        label = { Text(stringResource(R.string.body_text)) }
    )
}

@Composable
private fun ImageBodyField(
    image: Bitmap,
    onImageUpdate: (Bitmap) -> Unit
) {
    ImageGetter(
        image = image,
        onImageUpdate = onImageUpdate
    )
}

@Composable
private fun YouTubeBodyField(
    id: String,
    start: Int,
    onUpdate: (String, Int) -> Unit
) {
    YoutubeLinkInput(
        youtubeId = id,
        startTime = start,
        onYoutubeUpdate = { newId, newStart -> onUpdate(newId, newStart) }
    )
}

@Composable
private fun AddBodyButton(onClick: () -> Unit) {
    TextButton(
        modifier = Modifier.testTag("QuizCreatorAddBodyButton"),
        onClick = onClick
    ) {
        Text(stringResource(R.string.add_body))
    }
}

@Composable
private fun DeleteBodyButton(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit
) {
    IconButton(
        modifier = modifier
            .testTag("QuizCreatorDeleteBodyButton"),
        onClick = onDelete
    ) {
        Icon(
            imageVector = Icons.Default.RemoveCircleOutline,
            contentDescription = stringResource(R.string.delete_body)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BodyPreviews() {
    QuizzerAndroidTheme {
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
    Dialog(
        onDismissRequest = onDismissRequest
    ){
        Surface(
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 4.dp,
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    stringResource(R.string.add_body),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    stringResource(R.string.select_body_type),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    state = rememberLazyGridState(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(BodyType.values) { bodyType ->
                        IconButtonWithText(
                            imageVector = bodyType.icon,
                            text = stringResource(bodyType.labelRes),
                            onClick = { onBodyTypeSelected(bodyType) },
                            modifier = Modifier
                                .testTag("BodyTypeDialog${bodyType::class.simpleName}Button")
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .padding(vertical = 8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            description = stringResource(bodyType.labelRes),
                            iconSize = 48.dp,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BodyDialogPreview() {
    QuizzerAndroidTheme {
        BodyTypeDialog(
            onDismissRequest = {},
            onBodyTypeSelected = {},
        )
    }
}
