package com.asu1.customComposable.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults

@Composable
fun IconButtonWithText(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    description: String? = null,
    iconSize: Dp = 32.dp
) {
    IconWithTextBody(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        iconSize = iconSize,
    ){
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun IconButtonWithText(
    resourceId: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    description: String? = null,
    iconSize: Dp = 32.dp
) {
    IconWithTextBody(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        iconSize = iconSize,
    ){
        Icon(
            painter = painterResource(id = resourceId),
            contentDescription = description,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun IconWithTextBody(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp = 32.dp,
    icon: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable {
            if (enabled) onClick()
        }
            .wrapContentHeight()
            .width(iconSize * 2.5f)
    ) {
        icon()
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = QuizzerTypographyDefaults.quizzerLabelSmallMedium,
            maxLines = 2,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IconButtonWithTextPreview() {
    QuizzerAndroidTheme {
        IconButtonWithText(
            imageVector = Icons.Default.Add,
            text = "그라데이션",
            onClick = { },
        )
    }
}