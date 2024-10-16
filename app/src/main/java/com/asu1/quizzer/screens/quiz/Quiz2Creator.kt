package com.asu1.quizzer.screens.quiz

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.SaveButton
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.quizModels.Quiz2ViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun Quiz2Creator(
    quiz: Quiz2ViewModel = viewModel(),
    onSave: (Quiz) -> Unit
){
    val quiz2State by quiz.quiz2State.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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
                MyTextField(
                    value = quiz2State.question,
                    onValueChange = { quiz.updateQuestion(it) },
                    imeAction = ImeAction.Done,
                    focusRequester = focusRequester,
                    onNext = {
                        focusManager.clearFocus()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                key(quiz2State.centerDate, quiz2State.uuid, "creator"){
                    CalendarWithFocusDates(
                        focusDates = quiz2State.answerDate,
                        onDateClick = { date ->
                            quiz.updateDate(date)
                        },
                        currentMonth = quiz2State.centerDate,
                    )
                }
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Current Start: ${quiz2State.centerDate}, ±20Y",
                )
                YearMonthDropDown(
                    yearMonth = quiz2State.centerDate,
                    onYearMonthChange = {
                        quiz.updateCenterDate(it)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(quiz2State.answerDate.size){index ->
                Text(text = quiz2State.answerDate.elementAt(index).toString())
            }
        }
        SaveButton(
            onSave = {
                onSave(
                    quiz2State
                )
            }
        )
    }
}

@Composable
fun YearMonthDropDown(yearMonth: YearMonth, onYearMonthChange: (YearMonth) -> Unit = {}){
    val months = 1..12
    var expanded by remember { mutableStateOf(false) }
    var year by remember { mutableStateOf(yearMonth.year.toString()) }
    val focusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = year,
            onValueChange = {
                year = it
            },
            label = { Text("Year : ") },
            modifier = Modifier.weight(1f)
                .onFocusChanged { focusState ->
                    if(!focusState.isFocused){
                        onYearMonthChange(YearMonth.of(year.toInt(), yearMonth.month))
                    }
                },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
        ){
            Text(text = "Month: ${yearMonth.month}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .border(1.dp, androidx.compose.ui.graphics.Color.Black, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
            ) {
                months.forEach { month ->
                    DropdownMenuItem(
                        onClick = {
                            onYearMonthChange(YearMonth.of(year.toInt(), month))
                            expanded = false
                        },
                        text = {
                            Text(
                                text = month.toString(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .border(1.dp, androidx.compose.ui.graphics.Color.Black, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                                    .fillMaxWidth()
                        ) },
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
    currentMonth: YearMonth = YearMonth.now(),
    yearRange: Int = 10,
) {
    val startMonth = currentMonth.minusYears(yearRange.toLong()) // Adjust as needed
    val endMonth = currentMonth.plusYears(yearRange.toLong()) // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        state = state,
        dayContent = {it ->
            val isSelected = focusDates.contains(it.date)
            Day(it, state.firstVisibleMonth.yearMonth, isSelected, onDateClick)
        },
        monthHeader = { month ->
            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = month.yearMonth.year.toString() + ",  " + month.yearMonth.month.name)
                }
                Spacer(modifier = Modifier.height(8.dp))
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
            quiz = Quiz2ViewModel(),
            onSave = {}
        )
    }
}