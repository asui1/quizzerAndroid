package com.asu1.quiz.content.dateSelectionQuiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.asu1.quiz.ui.textStyleManager.BodyTextStyle
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

@Composable
fun CalendarWithFocusDates(
    focusDates: Set<LocalDate>,
    onDateClick: (LocalDate) -> Unit,
    currentMonth: YearMonth = YearMonth.now(),
) {
    val state = rememberCalendarState(
        startMonth = currentMonth.minusYears(10),
        endMonth = currentMonth.plusYears(10),
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.SUNDAY
    )
    HorizontalCalendar(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .then(BodyTextStyle.borderModifier),
        state = state,
        dayContent = {
            val isSelected = focusDates.contains(it.date)
            key(isSelected) {
                Day(it, state.firstVisibleMonth.yearMonth, isSelected, onDateClick)
            }
        },
        monthHeader = { month ->
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text =
                        month.yearMonth.format(monthYearFormatter)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(){
                    daysOfWeek.forEach { day ->
                        Text(
                            color = when(day){
                                daysOfWeek[0] -> BodyTextStyle.contentColorToRed
                                daysOfWeek[6] -> BodyTextStyle.contentColorToBlue
                                else -> BodyTextStyle.contentColor
                            },
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
    )
}

@Composable
fun Day(day: CalendarDay, currentMonth: YearMonth, isSelected: Boolean, onDateClick: (LocalDate) -> Unit,
) {
    val isInCurrentMonth = day.date.month == currentMonth.month
    val alpha = if (isInCurrentMonth) 1f else 0.5f
    val color = when (day.date.dayOfWeek) {
        DayOfWeek.SATURDAY -> BodyTextStyle.contentColorToBlue
        DayOfWeek.SUNDAY -> BodyTextStyle.contentColorToRed
        else -> BodyTextStyle.contentColor
    }
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val blendFraction = 0.3f
    Box(
        modifier = Modifier
            .testTag(day.date.toString())
            .aspectRatio(1f)
            .alpha(alpha)
            .then(
                if (isSelected) Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
                else Modifier
            )
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .clickable {
                onDateClick(day.date)
            }
            .semantics {
                contentDescription = "Day ${day.date}"
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if(isSelected) lerp(color, MaterialTheme.colorScheme.onPrimaryContainer, blendFraction) else color
        )
    }
}
