package com.asu1.quiz.checker

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
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.models.quiz.Quiz2
import com.asu1.models.quiz.Quiz3
import com.asu1.models.quiz.Quiz4
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.sampleQuiz1
import com.asu1.quiz.ui.ImageColorBackground
import com.asu1.quiz.viewmodel.quiz.Quiz4ViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.utils.setTopBarColor

@Composable
fun QuizChecker(
    quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel()
){
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
                    quizTheme = quizTheme,
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
    quizzes: List<Quiz<*>>,
    quizTheme: QuizTheme = QuizTheme(),
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
                quizTheme = quizTheme,
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
    quiz: Quiz<*>,
    quizTheme: QuizTheme = QuizTheme(),
) {
    when(quiz){
        is Quiz1 -> {
            Quiz1Checker(
                quiz = quiz,
            )
        }
        is Quiz2 -> {
            Quiz2Checker(
                quiz = quiz,
                quizTheme = quizTheme,
            )
        }
        is Quiz3 -> {
            Quiz3Checker(
                quiz = quiz,
            )
        }
        is Quiz4 -> {
            val quiz4ViewModel: Quiz4ViewModel = viewModel(
                key = quiz.uuid
            )
            quiz4ViewModel.loadQuiz(quiz)
            Quiz4Checker(
                quiz = quiz4ViewModel,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCheckerPreview(){
    val quizTheme = QuizTheme()
    Column(){
        QuizCheckerBody(
            quiz = sampleQuiz1,
            quizTheme = quizTheme,
        )
    }
}