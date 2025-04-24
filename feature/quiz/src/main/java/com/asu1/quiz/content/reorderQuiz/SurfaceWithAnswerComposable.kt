package com.asu1.quiz.content.reorderQuiz

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle

@Composable
fun SurfaceWithAnswerComposable(
    modifier: Modifier = Modifier,
    item: String,
    shadowElevation: Dp,
){
    Surface(
        color = Color.Transparent,
        shadowElevation = shadowElevation,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        AnswerTextStyle.GetTextComposable(item,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}