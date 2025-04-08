package com.asu1.quiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.button.IconButtonWithText
import com.asu1.resources.R

@Composable
fun QuizLayoutBottomBar(
    moveBack: () -> Unit,
    moveForward: () -> Unit,
    content: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    )
    {
        IconButtonWithText(
            imageVector = Icons.Default.ArrowBackIosNew,
            text = stringResource(R.string.back),
            onClick = {
                moveBack()
            },
            description = "Move back",
            iconSize = 24.dp
        )
        content()
        IconButtonWithText(
            modifier = Modifier.testTag("QuizLayoutBuilderProceedButton"),
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            text = stringResource(R.string.next),
            onClick = moveForward,
            description = "Move Forward",
            iconSize = 24.dp
        )
    }
}