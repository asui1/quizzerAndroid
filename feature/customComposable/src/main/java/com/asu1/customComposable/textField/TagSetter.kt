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
    var tag by remember { mutableStateOf("") }

    fun onAddTag() {
        if (tag.isNotEmpty()) {
            onClick(tag)
            tag = ""
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.select_tags),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp),
        ) {
            tags?.forEach { tag ->
                key(tag) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Button(
                            onClick = {
                                onClick(tag)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), // 살짝 강조된 배경
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(15), // pill shape
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
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tag,
            onValueChange = {
                if(it.length <= 20)  tag = it
            },
            label = { Text(stringResource(R.string.enter_tag)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .testTag("TagSetterTextField"),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddTag()
                }
            ),
            trailingIcon = {
                TextButton(onClick = {
                    if (tag.isNotEmpty()) {
                        onClick(tag)
                        tag = ""
                    }
                }) {
                    Text("+")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TagSetterPreview(){
    TagSetter(
        tags = setOf("tag1", "tag2", "tag3"),
        onClick = {},
        focusRequester = FocusRequester()
    )
}