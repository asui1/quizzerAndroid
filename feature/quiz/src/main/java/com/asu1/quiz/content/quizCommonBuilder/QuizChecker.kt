package com.asu1.quiz.content.quizCommonBuilder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.content.connectItemQuiz.ConnectItemsQuizChecker
import com.asu1.quiz.content.dateSelectionQuiz.DateSelectionQuizChecker
import com.asu1.quiz.content.multipleChoiceQuiz.MultipleChoiceQuizChecker
import com.asu1.quiz.content.reorderQuiz.ReorderQuizChecker
import com.asu1.quiz.content.shortAnswerQuiz.ShortAnswerQuizChecker
import com.asu1.quiz.ui.ImageColorBackground
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.utils.setTopBarColor

@Composable
fun QuizChecker(
){
    val quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel()
    val quizState by quizCoordinatorViewModel.quizUIState.collectAsStateWithLifecycle()
    val quizzes = quizState.quizContentState.quizzes
    val quizTheme = quizState.quizTheme
    val colorScheme = quizTheme.colorScheme
    val view = LocalView.current
    val pagerState = rememberPagerState(
        initialPage = 0,
    ){
        quizzes.size
    }

    LaunchedEffect(colorScheme.primaryContainer) {
        setTopBarColor(
            view = view,
            color = colorScheme.primaryContainer
        )
    }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                ImageColorBackground(
                    imageColor = quizTheme.backgroundImage,
                    modifier = Modifier.fillMaxSize()
                )
                QuizCheckerPager(
                    pagerState = pagerState,
                    quizzes = quizzes,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun QuizCheckerPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    quizzes: List<Quiz>,
) {
    HorizontalPager(
        state = pagerState,
        key = { index -> quizzes[index].uuid },
        modifier = modifier,
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            QuizCheckerBody(
                quiz = quizzes[page],
            )
            Text(
                text = "${page + 1}/${quizzes.size}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.align(Alignment.BottomEnd).padding(2.dp)
            )
        }
    }
}

@Composable
fun QuizCheckerBody(
    quiz: Quiz,
) {
    when(quiz){
        is MultipleChoiceQuiz -> {
            MultipleChoiceQuizChecker(
                quiz
            )
        }
        is DateSelectionQuiz -> {
            DateSelectionQuizChecker(quiz)
        }
        is ReorderQuiz -> {
            ReorderQuizChecker(quiz)
        }
        is ConnectItemsQuiz -> {
            ConnectItemsQuizChecker(
                quiz
            )
        }
        is ShortAnswerQuiz -> {
            ShortAnswerQuizChecker(quiz)
        }
        is FillInBlankQuiz -> {
            TODO()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCheckerPreview(){
    Column {
        QuizCheckerBody(
            quiz = sampleMultipleChoiceQuiz,
        )
    }
}
