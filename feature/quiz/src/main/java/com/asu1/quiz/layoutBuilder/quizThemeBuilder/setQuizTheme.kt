package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
    quizTheme: QuizTheme = QuizTheme(),
    isTitleImageSet: Boolean = false,
    updateQuizCoordinator: (QuizCoordinatorActions) -> Unit = {},
    step: LayoutSteps = LayoutSteps.THEME,
){
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    fun scrollTo(index: Int){
        coroutineScope.launch {
            delay(300)
            listState.animateScrollToItem(index, scrollOffset = 0) // For QuizThemeExample
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item("ColorSchemeGen") {
            GenerateNewColorScheme(
                isTitleImageSet = isTitleImageSet,
                generateColorScheme = { quizCoordinatorAction ->
                    updateQuizCoordinator(quizCoordinatorAction)
                },
                scrollTo = { scrollTo(0) },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item("Example") {
            QuizThemeExample(
                quizTheme = quizTheme,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (step == LayoutSteps.THEME) {
            item("Background") {
                QuizLayoutSetBackground(
                    modifier = Modifier,
                    backgroundImageColor = quizTheme.backgroundImage,
                    updateQuizTheme = { quizThemeAction ->
                        updateQuizCoordinator(
                            QuizCoordinatorActions.UpdateQuizTheme(
                                quizThemeAction
                            )
                        )
                    },
                    scrollTo = { scrollTo(2) },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item("ColorSchemeSet") {
                SetColorScheme(
                    modifier = Modifier,
                    currentColors = quizTheme.colorScheme,
                    updateQuizTheme = { quizThemeAction ->
                        updateQuizCoordinator(
                            QuizCoordinatorActions.UpdateQuizTheme(
                                quizThemeAction
                            )
                        )
                    },
                    scrollTo = { scrollTo(3) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (step == LayoutSteps.TEXTSTYLE) {
            item("SetTextStyle") {
                QuizLayoutSetTextStyle(
                    modifier = Modifier,
                    questionStyle = quizTheme.questionTextStyle,
                    bodyStyle = quizTheme.bodyTextStyle,
                    answerStyle = quizTheme.answerTextStyle,
                    updateQuizTheme = { quizThemeAction ->
                        updateQuizCoordinator(
                            QuizCoordinatorActions.UpdateQuizTheme(
                                quizThemeAction))
                    },
                    scrollTo = { scrollTo(3) },
                )
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewQuizLayoutSetQuizTheme(){
    QuizzerAndroidTheme {
        QuizLayoutSetQuizTheme()
    }
}

