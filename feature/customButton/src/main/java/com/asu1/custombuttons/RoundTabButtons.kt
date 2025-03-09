package com.asu1.custombuttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults

@Composable
fun RoundTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = title,
        style = QuizzerTypographyDefaults.quizzerRoundTab,
        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        overflow = TextOverflow.Ellipsis, // Prevents text from being clipped
        maxLines = 1,
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .wrapContentWidth()
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun RoundTabPreview(){
    QuizzerAndroidTheme {
        Row(){
            RoundTab(
                "History",
                isSelected = true
            ) { }
            RoundTab(
                "History",
                isSelected = false
            ) { }
        }
    }
}