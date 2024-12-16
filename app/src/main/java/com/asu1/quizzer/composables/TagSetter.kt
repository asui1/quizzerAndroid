package com.asu1.quizzer.composables

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onInterceptKeyBeforeSoftKeyboard
import androidx.compose.ui.input.key.onPreInterceptKeyBeforeSoftKeyboard
import androidx.compose.ui.input.key.onPreviewKeyEvent
import com.asu1.quizzer.util.Logger
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.asu1.quizzer.util.keyboardAsState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSetter(
    tags: Set<String>?,
    onClick: (String) -> Unit,
    focusRequester: androidx.compose.ui.focus.FocusRequester = androidx.compose.ui.focus.FocusRequester(),
    modifier: Modifier = Modifier
) {
    var tag by remember { mutableStateOf("") }

    fun onAddTag() {
        if (tag.isNotEmpty()) {
            onClick(tag)
            tag = ""
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.select_tags_that_interest_you),
            style = MaterialTheme.typography.titleMedium,
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
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                        ) {
                            Text(text = tag)
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
        focusRequester = androidx.compose.ui.focus.FocusRequester()
    )
}