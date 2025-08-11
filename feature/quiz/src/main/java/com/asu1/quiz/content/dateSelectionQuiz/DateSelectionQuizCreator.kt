package com.asu1.quiz.content.dateSelectionQuiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
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
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

val monthDateYearFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("yyyy . MM . dd")
val monthYearFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("yyyy / MMMM")

@Composable
fun DateSelectionQuizCreator(
    quizVm: DateSelectionQuizViewModel = viewModel(),
    onSave:  (DateSelectionQuiz) -> Unit
) {
    val quizState     by quizVm.quizState.collectAsStateWithLifecycle()
    var displayedDate by remember { mutableStateOf(quizState.centerDate) }

    LaunchedEffect(quizState.centerDate) {
        displayedDate = quizState.centerDate
    }

    QuizCreatorBase(
        quiz         = quizState,
        testTag      = "DateSelectionQuizCreatorLazyColumn",
        onSave       = { onSave(quizState) },
        updateQuiz = { action -> quizVm.onAction(action) },
    ) {
        // delegate all items to a dedicated editor
        dateSelectionEditor(
            quizState      = quizState,
            displayedDate  = displayedDate,
            onMonthChange  = { newMonth -> quizVm.updateCenterDate(newMonth) },
            onDateClick    = { date ->
                quizVm.updateDate(date)
                displayedDate = date
            }
        )
    }
}

/**
 * Lazily emits the three list items:
 * 1) header with month/year and dropdown
 * 2) calendar grid with clickable dates
 * 3) selected dates list
 */
private fun LazyListScope.dateSelectionEditor(
    quizState:      DateSelectionQuiz,
    displayedDate: LocalDate,
    onMonthChange:  (YearMonth) -> Unit,
    onDateClick:    (LocalDate) -> Unit
) {
    item {
        DateSelectionHeader(
            centerDate    = quizState.centerDate,
            onMonthChange = onMonthChange
        )
    }

    item {
        CalendarWithFocusDates(
            focusDates   = quizState.answerDate,
            onDateClick  = onDateClick,
            currentMonth = displayedDate.yearMonth
        )
    }

    item {
        SelectedDatesColumn(
            answerDate = quizState.answerDate,
            updateDate = onDateClick
        )
    }
}

@Composable
private fun DateSelectionHeader(
    centerDate:    LocalDate,
    onMonthChange: (YearMonth) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text       = stringResource(R.string.current_start)
                    + centerDate.format(monthYearFormatter),
            fontWeight = FontWeight.Bold,
            modifier   = Modifier.padding(start = 16.dp)
        )

        Text(
            text       = stringResource(R.string.year_range) + ": ±20Y",
            style      = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Light,
            modifier   = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        YearMonthDropDown(
            yearMonth        = centerDate.yearMonth,
            onYearMonthChange = onMonthChange
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
            keyboardOptions = KeyboardOptions.Default.copy
                (keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
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
            quizVm = quizViewModel,
            onSave = {}
        )
    }
}
