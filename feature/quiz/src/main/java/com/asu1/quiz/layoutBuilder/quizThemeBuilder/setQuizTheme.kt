package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.QuizTheme
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.resources.LayoutSteps
import com.asu1.resources.QuizzerAndroidTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizLayoutSetQuizTheme(
    quizTheme: QuizTheme,
    isTitleImageSet: Boolean,
    updateQuizCoordinator: (QuizCoordinatorActions) -> Unit,
    step: LayoutSteps
) {
    // 1️⃣ State & helpers
    val listState      = rememberLazyListState()
    val scrollToIndex  = rememberScrollToIndex(listState)

    // 2️⃣ The content list
    LazyColumn(
        state    = listState,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item { ColorSchemeGenSection(isTitleImageSet, updateQuizCoordinator, scrollToIndex(0)) }
        item { Spacer(Modifier.height(8.dp)) }

        item { ThemeExampleSection(quizTheme) }
        item { Spacer(Modifier.height(8.dp)) }

        if (step == LayoutSteps.THEME) {
            item { BackgroundSection(quizTheme, updateQuizCoordinator, scrollToIndex(2)) }
            item { Spacer(Modifier.height(16.dp)) }

            item { ColorSchemeSetSection(quizTheme, updateQuizCoordinator, scrollToIndex(3)) }
            item { Spacer(Modifier.height(8.dp)) }
        }

        if (step == LayoutSteps.TEXTSTYLE) {
            item { TextStyleSection(quizTheme, updateQuizCoordinator, scrollToIndex(3)) }
        }
    }
}

@Composable
private fun rememberScrollToIndex(
    listState: LazyListState
): (Int) -> () -> Unit {
    val scope = rememberCoroutineScope()
    return { index ->
        {
            scope.launch {
                delay(300) // allow collapse/animation
                listState.animateScrollToItem(index)
            }
        }
    }
}

@Composable
private fun ColorSchemeGenSection(
    isTitleImageSet: Boolean,
    updateQuizCoordinator: (QuizCoordinatorActions) -> Unit,
    onExpand: () -> Unit
) {
    GenerateNewColorScheme(
        isTitleImageSet = isTitleImageSet,
        generateColorScheme = { quizCoordinatorAction ->
            updateQuizCoordinator(quizCoordinatorAction)
        },
        scrollTo = onExpand,
    )
}

@Composable
private fun ThemeExampleSection(quizTheme: QuizTheme) {
    QuizThemeExample(quizTheme = quizTheme)
}

@Composable
private fun BackgroundSection(
    quizTheme: QuizTheme,
    updateQuizCoordinator: (QuizCoordinatorActions) -> Unit,
    onExpand: () -> Unit
) {
    QuizLayoutSetBackground(
        backgroundImageColor = quizTheme.backgroundImage,
        updateQuizTheme      = { action -> updateQuizCoordinator(QuizCoordinatorActions.UpdateQuizTheme(action)) },
        scrollTo             = onExpand
    )
}

@Composable
private fun ColorSchemeSetSection(
    quizTheme: QuizTheme,
    updateQuizCoordinator: (QuizCoordinatorActions) -> Unit,
    onExpand: () -> Unit
) {
    SetColorScheme(
        currentColors     = quizTheme.colorScheme,
        updateQuizTheme   = { action -> updateQuizCoordinator(QuizCoordinatorActions.UpdateQuizTheme(action)) },
        scrollTo          = onExpand
    )
}

@Composable
private fun TextStyleSection(
    quizTheme: QuizTheme,
    updateQuizCoordinator: (QuizCoordinatorActions) -> Unit,
    onExpand: () -> Unit
) {
    QuizLayoutSetTextStyle(
        questionStyle       = quizTheme.questionTextStyle,
        bodyStyle           = quizTheme.bodyTextStyle,
        answerStyle         = quizTheme.answerTextStyle,
        updateQuizTheme     = { action -> updateQuizCoordinator(QuizCoordinatorActions.UpdateQuizTheme(action)) },
        scrollTo            = onExpand
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizLayoutSetQuizTheme(){
    QuizzerAndroidTheme {
        QuizLayoutSetQuizTheme(
            quizTheme = QuizTheme(),
            isTitleImageSet = false,
            updateQuizCoordinator = {},
            step = LayoutSteps.THEME
        )
    }
}

