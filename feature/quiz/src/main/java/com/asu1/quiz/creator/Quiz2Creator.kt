package com.asu1.quiz.creator

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz2
import com.asu1.quiz.ui.CalendarWithFocusDates
import com.asu1.quiz.ui.QuestionTextField
import com.asu1.quiz.viewmodel.quiz.Quiz2ViewModel
import com.asu1.resources.R
import java.time.YearMonth

@Composable
fun Quiz2Creator(
    quiz: Quiz2ViewModel = viewModel(),
    onSave: (Quiz<Quiz2>) -> Unit
){
    val quiz2State by quiz.quizState.collectAsStateWithLifecycle()
    var currentMonth by remember { mutableStateOf(quiz2State.centerDate) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(quiz2State.centerDate){
        currentMonth = quiz2State.centerDate
    }

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
                    question = quiz2State.question,
                    onQuestionChange =  {quiz.updateQuestion(it)},
                    focusManager = focusManager,
                )
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    buildString {
                        append(stringResource(R.string.current_start))
                        append("${quiz2State.centerDate}, ±20Y")
                    },
                )
                YearMonthDropDown(
                    yearMonth = quiz2State.centerDate,
                    onYearMonthChange = {
                        quiz.updateCenterDate(it)
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                CalendarWithFocusDates(
                    focusDates = quiz2State.answerDate,
                    onDateClick = { date ->
                        quiz.updateDate(date)
                        currentMonth = YearMonth.of(date.year, date.month)
                    },
                    currentMonth = currentMonth,
                    colorScheme = MaterialTheme.colorScheme
                )
            }
            item{
                Text(
                    "Answers"
                )
                Spacer(modifier = Modifier.height(4.dp))
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
fun YearMonthDropDown(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth, onYearMonthChange: (YearMonth) -> Unit = {},
                      key: String = "YearMonthDropDown"
){
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
            modifier = modifier
                .weight(1f)
                .testTag(key + "YearTextField")
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
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
            Text(text = "Month: ${yearMonth.monthValue}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .border(
                        1.dp,
                        Color.Black,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .fillMaxWidth()
                    .testTag(key + "MonthText")
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
                                    .border(
                                        1.dp,
                                        Color.Black,
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            8.dp
                                        )
                                    )
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .testTag(key + "Month${month}")
                            ) },
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Quiz2CreatorPreview() {
    com.asu1.resources.QuizzerAndroidTheme {
        Quiz2Creator(
            quiz = Quiz2ViewModel(),
            onSave = {}
        )
    }
}