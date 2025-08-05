package com.asu1.quiz.content.dateSelectionQuiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.asu1.quiz.content.quizCommonBuilder.QuizCreatorBase
import com.asu1.quiz.viewmodel.quiz.DateSelectionQuizViewModel
import com.asu1.resources.Months
import com.asu1.resources.QuizzerAndroidTheme
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
    val quizState by quiz.quizState.collectAsStateWithLifecycle()
    var currentMonth by remember { mutableStateOf(quizState.centerDate) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(quizState.centerDate){
        currentMonth = quizState.centerDate
    }

    QuizCreatorBase(
        quiz = quizState,
        testTag = "DateSelectionQuizCreatorLazyColumn",
        onSave = { onSave(quizState) },
        focusManager = focusManager,
        updateQuestion = { it -> quiz.updateQuestion(it) },
        updateBodyState = { it -> quiz.updateBodyState(it) },
    ) {
        item{
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                buildString {
                    append(stringResource(R.string.current_start))
                    append("${quizState.centerDate.format(monthYearFormatter)}")
                },
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp)
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
                yearMonth = quizState.centerDate.yearMonth,
                onYearMonthChange = {
                    quiz.updateCenterDate(it)
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            CalendarWithFocusDates(
                focusDates = quizState.answerDate,
                onDateClick = { date ->
                    quiz.updateDate(date)
                    currentMonth = date
                },
                currentMonth = currentMonth.yearMonth
            )
        }
        item{
            SelectedDatesColumn(
                answerDate = quizState.answerDate,
                updateDate = { date ->
                    quiz.updateDate(date)
                }
            )
        }
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
            expanded = expanded,
            labelText = "${stringResource(R.string.month)}: ${yearMonth.monthValue}",
            onItemSelected = { month ->
                onYearMonthChange(yearMonth.withMonth(month + 1))
                expanded = false
            },
            onToggleExpanded = { expanded = it },
            itemResIds = Months.entries.map { it.monthStringResource },
            selectedIndex = yearMonth.monthValue-1,
            testTag = "YearMonthDropDownMonth",
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDateSelectionQuizCreator() {
    QuizzerAndroidTheme {
        val quizViewModel: DateSelectionQuizViewModel = viewModel()
        DateSelectionQuizCreator(
            quiz = quizViewModel,
            onSave = {}
        )
    }
}
