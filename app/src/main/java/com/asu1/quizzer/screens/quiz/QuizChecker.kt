package com.asu1.quizzer.screens.quiz

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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
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
import com.asu1.quizzer.model.ImageColorBackground
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.quizzer.util.setTopBarColor
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.quizModels.Quiz4ViewModel
import com.asu1.resources.R

@Composable
fun QuizChecker(
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
){
    val quizzes by quizLayoutViewModel.quizzes.collectAsStateWithLifecycle()
    val quizTheme by quizLayoutViewModel.quizTheme.collectAsStateWithLifecycle()
    val colorScheme = quizTheme.colorScheme
    val view = LocalView.current
    val pagerState = rememberPagerState(
        initialPage = 0,
    ){
        quizzes.size
    }
    val textStyleManager by rememberUpdatedState(quizLayoutViewModel.getTextStyleManager())
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
                    textStyleManager = textStyleManager,
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
    textStyleManager: TextStyleManager,
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
                quizStyleManager = textStyleManager,
            )
            Text(
                text = buildString {
                    append(quizzes[page].point)
                    append(stringResource(R.string.pt))
                },
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.align(Alignment.TopEnd).padding(2.dp)
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
    quizStyleManager: TextStyleManager,
) {
    when(quiz){
        is Quiz1 -> {
            Quiz1Checker(
                quiz = quiz,
                quizStyleManager = quizStyleManager,
            )
        }
        is Quiz2 -> {
            Quiz2Checker(
                quiz = quiz,
                quizTheme = quizTheme,
                quizStyleManager = quizStyleManager,
            )
        }
        is Quiz3 -> {
            Quiz3Checker(
                quiz = quiz,
                quizStyleManager = quizStyleManager
            )
        }
        is Quiz4 -> {
            val quiz4ViewModel: Quiz4ViewModel = viewModel(
                key = quiz.uuid
            )
            quiz4ViewModel.loadQuiz(quiz)
            Quiz4Checker(
                quiz = quiz4ViewModel,
                quizStyleManager = quizStyleManager,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCheckerPreview(){
    val quizTheme = QuizTheme()
    val textStyleManager = TextStyleManager()
    Column(){
        QuizCheckerBody(
            quiz = sampleQuiz1,
            quizTheme = quizTheme,
            quizStyleManager = textStyleManager
        )

    }
}