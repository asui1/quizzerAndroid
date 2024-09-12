package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.screens.getQuizLayoutState
import com.asu1.quizzer.states.QuizLayoutState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Logger

@Composable
fun QuizLayoutTitle(quizLayoutState: QuizLayoutState, proceed: () -> Unit,
                    ) {
    var title by remember { mutableStateOf("") }
    val focusRequester = remember{ FocusRequester() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Enter Quiz Title",
            style = MaterialTheme.typography.titleMedium,
        )
        TextField(
            value = title,
            onValueChange = {title = it},
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    proceed()
                }
            )
        )
    }

    LaunchedEffect(Unit) {
        Logger().debug("QuizLayoutTitle: LaunchedEffect ${quizLayoutState.quizTitle.value}")
        focusRequester.requestFocus()
        title = quizLayoutState.quizTitle.value
    }
    DisposableEffect(Unit){
        onDispose {
            Logger().debug("QuizLayoutTitle: DisposableEffect $title")
            quizLayoutState.setQuizTitle(title)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutTitlePreview() {
    QuizzerAndroidTheme {
        QuizLayoutTitle(
            quizLayoutState = getQuizLayoutState(),
            proceed = {},
        )
    }
}