package com.asu1.quiz.content.multipleChoiceQuiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle

@Composable
fun MultipleChoiceOptionRow(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    enabled: Boolean = false,
    onChecked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (enabled) Modifier.clickable(onClick = onChecked)
                else Modifier
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onChecked() },
            enabled = enabled,
            modifier = Modifier
                .scale(1.5f)
                .padding(end = 8.dp)
                .semantics {
                    contentDescription = text
                    stateDescription = if (checked) "Checked" else "Unchecked"
                }
        )
        AnswerTextStyle.GetTextComposable(text)
    }
}