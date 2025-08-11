package com.asu1.quiz.content.reorderQuiz

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle

@Composable
fun SurfaceWithAnswerComposable(
    modifier: Modifier = Modifier,
    item: String,
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {        AnswerTextStyle.GetTextComposable(item,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
