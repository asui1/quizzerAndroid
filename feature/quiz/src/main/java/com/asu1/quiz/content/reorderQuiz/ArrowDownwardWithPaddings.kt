package com.asu1.quiz.content.reorderQuiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
