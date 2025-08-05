package com.asu1.customComposable.topBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.resources.R

@Composable
fun QuizzerTopBarBase(
    modifier: Modifier = Modifier,
    onClickAppIcon: () -> Unit = {},
    iconSize: Dp = 50.dp,
    header: @Composable () -> Unit = {},
    body: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        header()
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(iconSize)
                .clickable(onClick = onClickAppIcon)
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            body()
        }
        actions()
    }
}

@Preview(showBackground = true)
@Composable
fun QuizzerTopBarBasePreview() {
    QuizzerTopBarBase(
        header = {
            Text("HEAD")
        },
        body = {
            Text("BODY")
        },
        actions = {
            Text("ACTION")
        }
    )
}
