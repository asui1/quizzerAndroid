package com.asu1.quiz.content.dateSelectionQuiz

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.asu1.quiz.content.AnswerShower
import com.asu1.quiz.creator.monthDateYearFormatter
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.resources.R
import java.time.LocalDate

@Composable
fun SelectedDatesColumn(
    answerDate: Set<LocalDate>,
    updateDate: (LocalDate) -> Unit,
    markAnswers: Boolean = false,
    correctAnswers: Set<LocalDate> = emptySet(),
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.selected_dates),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 12.dp)
        )
        answerDate.forEachIndexed { index, date ->
            Spacer(modifier = Modifier.height(4.dp))
            if (markAnswers) {
                val isCorrect = correctAnswers.contains(date)
                AnswerShower(
                    isCorrect = isCorrect,
                    contentAlignment = Alignment.CenterStart
                ) {
                    AnswerTextStyle.GetTextComposable(
                        buildString {
                            append("${index + 1}.    ")
                            append(date.format(monthDateYearFormatter))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                updateDate(date)
                            }
                    )
                }
            } else {
                AnswerTextStyle.GetTextComposable(
                    buildString {
                        append("${index + 1}.    ")
                        append(date.format(monthDateYearFormatter))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            updateDate(date)
                        }
                )
            }
        }
        if(markAnswers){
            correctAnswers.forEach{localDate ->
                if(!answerDate.contains(localDate)){
                    Spacer(modifier = Modifier.height(4.dp))
                    AnswerShower(
                        isCorrect = false,
                        contentAlignment = Alignment.CenterStart,
                    ){
                        AnswerTextStyle.GetTextComposable(localDate.toString(), modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}