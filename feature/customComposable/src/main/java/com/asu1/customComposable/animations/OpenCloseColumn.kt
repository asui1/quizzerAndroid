package com.asu1.customComposable.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OpenCloseColumn(
    modifier: Modifier = Modifier,
    isOpen: Boolean = true,
    onToggleOpen: () -> Unit = {},
    openWidth: Dp = 90.dp,
    closeWidth: Dp = 35.dp,
    height: Dp = 300.dp,
    content: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .animateContentSize()
            .height(height)
            .width(if(isOpen) openWidth else closeWidth)
    ) {
        IconButton(
            onClick = {onToggleOpen()},
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        bottomStart = 16.dp,
                    )
                )
                .size(width = closeWidth, height = closeWidth*2)
        ) {
            Icon(
                imageVector = if(isOpen) Icons.AutoMirrored.Filled.ArrowForwardIos else Icons.Default.ArrowBackIosNew,
                contentDescription = "Resize Handle",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        AnimatedVisibility(
            visible = isOpen,
            modifier = Modifier
                .wrapContentSize()
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OpenCloseColumnPreview() {
    OpenCloseColumn(
        isOpen = true,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            )
        }
    )
}
