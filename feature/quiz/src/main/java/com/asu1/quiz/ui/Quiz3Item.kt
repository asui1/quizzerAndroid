package com.asu1.quiz.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle

@Composable
fun ArrowDownwardWithPaddings(){
    Spacer(modifier = Modifier.height(4.dp))
    Icon(
        imageVector = Icons.Default.ArrowDownward,
        contentDescription = "Reorder",
        modifier = Modifier.padding(horizontal = 8.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun SurfaceWithAnswerComposable(
    modifier: Modifier = Modifier,
    item: String,
    shadowElevation: Dp,
){
    Surface(
        color = Color.Transparent,
        shadowElevation = shadowElevation,
        modifier = modifier
            .fillMaxWidth()
    ) {
        AnswerTextStyle.GetTextComposable(item,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}