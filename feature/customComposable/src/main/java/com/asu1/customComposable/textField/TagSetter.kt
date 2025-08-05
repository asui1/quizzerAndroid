package com.asu1.customComposable.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSetter(
    modifier: Modifier = Modifier,
    tags: Set<String>?,
    onClick: (String) -> Unit,
    focusRequester: FocusRequester = FocusRequester(),
) {
    var currentTag by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TagSetterHeader()

        TagList(
            tags = tags,
            onTagClicked = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )

        Spacer(Modifier.height(8.dp))

        TagInputField(
            tagValue = currentTag,
            onValueChange = { if (it.length <= 20) currentTag = it },
            onAddTag = {
                if (currentTag.isNotEmpty()) {
                    onClick(currentTag)
                    currentTag = ""
                }
            },
            focusRequester = focusRequester
        )
    }
}

@Composable
private fun TagSetterHeader() {
    Text(
        text = stringResource(R.string.select_tags),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagList(
    tags: Set<String>?,
    onTagClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(modifier = modifier) {
        tags.orEmpty().forEach { tag ->
            key(tag) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    TagChip(tag = tag, onClick = { onTagClicked(tag) })
                }
            }
        }
    }
}

@Composable
private fun TagChip(
    tag: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = tag,
            style = QuizzerTypographyDefaults.quizzerBodyMediumBold,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun TagInputField(
    tagValue: String,
    onValueChange: (String) -> Unit,
    onAddTag: () -> Unit,
    focusRequester: FocusRequester
) {
    TextField(
        value = tagValue,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.enter_tag)) },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .testTag("TagSetterTextField"),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onAddTag() }),
        trailingIcon = {
            TextButton(onClick = onAddTag) {
                Text("+")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TagSetterPreview(){
    val focusRequester = remember { FocusRequester() }
    TagSetter(
        tags = setOf("tag1", "tag2", "tag3"),
        onClick = {},
        focusRequester = focusRequester
    )
}
