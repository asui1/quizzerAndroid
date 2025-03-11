package com.asu1.quizcard

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerTypographyDefaults

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsView(tags: List<String>, modifier: Modifier = Modifier, maxLines: Int = 2) {
    FlowRow(
        modifier = modifier,
        maxLines = maxLines,
    ) {
        tags.forEach { tag ->
            Text(text = "#$tag",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 4.dp),
                style = QuizzerTypographyDefaults.quizzerBodySmallBold,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
fun PreviewTagsView() {
    TagsView(tags = listOf("Tag 1", "Tag 2", "Tag 3"))
}

