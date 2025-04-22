package com.asu1.quiz.creator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.customComposable.dropdown.FastCreateDropDownWithTextButton
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.quiz.ui.CalendarWithFocusDates
import com.asu1.quiz.ui.QuestionTextField
import com.asu1.quiz.ui.Quiz2SelectionViewer
import com.asu1.quiz.viewmodel.quiz.DateSelectionQuizViewModel
import com.asu1.resources.Months
import com.asu1.resources.R
import com.kizitonwose.calendar.core.yearMonth
import java.time.YearMonth
import java.time.format.DateTimeFormatter

val monthDateYearFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("yyyy . MM . dd")
val monthYearFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("yyyy / MMMM")

@Composable
fun DateSelectionQuizCreator(
    quiz: DateSelectionQuizViewModel = viewModel(),
    onSave: (DateSelectionQuiz) -> Unit
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
                        append("${quiz2State.centerDate.format(monthYearFormatter)}")
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    buildString {
                        append(stringResource(R.string.year_range))
                        append(": ±20Y")
                    },
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                YearMonthDropDown(
                    yearMonth = quiz2State.centerDate.yearMonth,
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
                        currentMonth = date
                    },
                    currentMonth = currentMonth.yearMonth,
                    colorScheme = MaterialTheme.colorScheme
                )
            }
            item{
                Quiz2SelectionViewer(
                    answerDate = quiz2State.answerDate,
                    updateDate = { date ->
                        quiz.updateDate(date)
                    }
                )
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
    yearMonth: YearMonth,
    onYearMonthChange: (YearMonth) -> Unit = {},
    key: String = "YearMonthDropDown"
){
    var expanded by remember { mutableStateOf(false) }
    var year by remember { mutableStateOf(yearMonth.year.toString()) }
    var yearTextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = yearMonth.year.toString(),
                selection = TextRange(yearMonth.year.toString().length) // move cursor to end
            )
        )
    }
    val focusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = yearTextFieldValue,
            onValueChange = { newTextFieldValue ->
                // Always keep the cursor at the end
                val newText = newTextFieldValue.text
                yearTextFieldValue = newTextFieldValue.copy(
                    text = newText,
                    selection = TextRange(newText.length)
                )
            },
            label = { Text(stringResource(R.string.year)) },
            modifier = modifier
                .weight(1f)
                .testTag(key + "YearTextField")
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        val yearInt = yearTextFieldValue.text.toIntOrNull() ?: yearMonth.year
                        onYearMonthChange(YearMonth.of(yearInt, yearMonth.month))
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
        FastCreateDropDownWithTextButton(
            modifier = Modifier.weight(1f),
            showDropdownMenu = expanded,
            labelText = "${stringResource(R.string.month)}: ${yearMonth.monthValue}",
            onClick = { month ->
                onYearMonthChange(YearMonth.of(year.toInt(), month+1))
                expanded = false
            },
            onChangeDropDown = { expanded = it },
            inputStringResourceItems = Months.entries.map { it.monthStringResource },
            currentSelection = yearMonth.monthValue-1,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDateSelectionQuizCreator() {
    com.asu1.resources.QuizzerAndroidTheme {
        DateSelectionQuizCreator(
            quiz = DateSelectionQuizViewModel(),
            onSave = {}
        )
    }
}