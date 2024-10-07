package com.asu1.quizzer.screens.quiz

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@Composable
fun Quiz2Creator(
    quiz: Quiz2 = Quiz2(),
    onSave: (Quiz) -> Unit
){
    var questionState by remember { mutableStateOf(TextFieldValue(quiz.question)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                QuestionTextField(
                    value = questionState,
                    onValueChange = { questionState = TextFieldValue(it.text) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                CalendarWithFocusDates(
                    focusDates = quiz.answerDate,
                    onDateClick = { date ->
                        quiz.update(date)
                    },
                    currentMonth = quiz.centerDate,
                )
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "CurrentMonth: ${quiz.centerDate}, ±20Y",
                )
                YearMonthDropDown(
                    yearMonth = quiz.centerDate
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(quiz.answerDate.size){index ->
                Text(text = quiz.answerDate.elementAt(index).toString())
            }
        }
    }
}

@Composable
fun YearMonthDropDown(yearMonth: YearMonth){
    val years = (yearMonth.year - 20)..(yearMonth.year + 20)
    val months = 1..12

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(){
            Text(text = "Year: ${yearMonth.year}",
                modifier = Modifier
                    .border(1.dp, androidx.compose.ui.graphics.Color.Black, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
            DropdownMenu(
                expanded = false,
                onDismissRequest = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                years.forEach { year ->
                    DropdownMenuItem(
                        onClick = {
                            yearMonth.plusYears(year.toLong())
                        },
                        text = { Text(year.toString()) }
                    )

                }
            }
        }
        Column(){
            Text(text = "Month: ${yearMonth.month}",
                modifier = Modifier
                    .border(1.dp, androidx.compose.ui.graphics.Color.Black, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
            DropdownMenu(
                expanded = false,
                onDismissRequest = {

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                months.forEach { month ->
                    DropdownMenuItem(
                        onClick = {
                            yearMonth.plusMonths(month.toLong())
                        },
                        text = { Text(month.toString()) },
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarWithFocusDates(
    focusDates: Set<LocalDate>,
    onDateClick: (LocalDate) -> Unit,
    currentMonth: YearMonth = YearMonth.now()
) {
    val selectedDates by remember { mutableStateOf(focusDates) }
    val startMonth = remember { currentMonth.minusYears(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusYears(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        state = state,
        dayContent = {it ->
            val isSelected = selectedDates.contains(it.date)
            Day(it, currentMonth, isSelected, onDateClick)
        },
        monthHeader = { month ->
            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {
                            currentMonth.minusMonths(1)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Previous Month"
                        )
                    }
                    Text(text = month.yearMonth.year.toString() + ",  " + month.yearMonth.month.name)
                    IconButton(
                        onClick = {
                            currentMonth.plusMonths(1)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Previous Month"
                        )
                    }
                }
                Row(){
                    daysOfWeek.forEach { day ->
                        Text(
                            color = when(day){
                                daysOfWeek[0] -> androidx.compose.ui.graphics.Color.Red
                                daysOfWeek[6] -> androidx.compose.ui.graphics.Color.Blue
                                else -> androidx.compose.ui.graphics.Color.Black
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
fun Day(day: CalendarDay, currentMonth: YearMonth, isSelected: Boolean, onDateClick: (LocalDate) -> Unit) {
    val isInCurrentMonth = day.date.month == currentMonth.month
    val alpha = if (isInCurrentMonth) 1f else 0.5f
    val color = when (day.date.dayOfWeek) {
        java.time.DayOfWeek.SATURDAY -> androidx.compose.ui.graphics.Color.Blue
        java.time.DayOfWeek.SUNDAY -> androidx.compose.ui.graphics.Color.Red
        else -> androidx.compose.ui.graphics.Color.Black
    }
    val backgroundColor = if (isSelected) androidx.compose.ui.graphics.Color.Yellow else androidx.compose.ui.graphics.Color.Transparent

    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square sizing!
            .alpha(alpha)
            .background(color = backgroundColor, shape = androidx.compose.foundation.shape.CircleShape)
            .clickable {
                onDateClick(day.date)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz2CreatorPreview() {
    QuizzerAndroidTheme {
        Quiz2Creator(
            quiz = Quiz2()
        ) {}
    }
}