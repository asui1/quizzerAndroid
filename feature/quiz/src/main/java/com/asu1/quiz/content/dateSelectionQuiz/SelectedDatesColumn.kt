package com.asu1.quiz.content.dateSelectionQuiz

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.asu1.quiz.content.quizCommonBuilder.AnswerShower
import com.asu1.resources.R
import java.time.LocalDate

@Composable
fun SelectedDatesColumn(
    answerDate:      Set<LocalDate>,
    updateDate:      (LocalDate) -> Unit,
    markAnswers:     Boolean = false,
    correctAnswers:  Set<LocalDate> = emptySet()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        SectionTitle(textRes = R.string.selected_dates)
        answerDate.sorted().forEachIndexed { idx, date ->
            Spacer(modifier = Modifier.height(4.dp))
            DateItem(
                index       = idx,
                date        = date,
                onClick     = { updateDate(date) },
                markCorrect = markAnswers && correctAnswers.contains(date)
            )
        }
        if (markAnswers) {
            correctAnswers
                .minus(answerDate)
                .sorted()
                .forEach { date ->
                    Spacer(modifier = Modifier.height(4.dp))
                    DateItem(
                        index       = null,
                        date        = date,
                        onClick     = null,
                        markCorrect = false
                    )
                }
        }
    }
}

@Composable
private fun SectionTitle(@StringRes textRes: Int) {
    Text(
        text       = stringResource(textRes),
        style      = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier   = Modifier.padding(start = 12.dp)
    )
}

@Composable
private fun DateItem(
    index:       Int?,
    date:        LocalDate,
    onClick:     ((LocalDate) -> Unit)?,
    markCorrect: Boolean
) {
    val label = buildString {
        index?.let { append("${it + 1}.") }
        append("  ")
        append(date.format(monthDateYearFormatter))
    }

    val textModifier = Modifier
        .fillMaxWidth()
        .let { if (onClick != null) it.clickable { onClick(date) } else it }

    if (onClick != null) {
        Text(
            text     = label,
            modifier = textModifier,
            color    = if (markCorrect) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    } else {
        AnswerShower(isCorrect = markCorrect, contentAlignment = Alignment.CenterStart) {
            Text(
                text     = label,
                modifier = textModifier,
                color    = LocalContentColor.current
            )
        }
    }
}
