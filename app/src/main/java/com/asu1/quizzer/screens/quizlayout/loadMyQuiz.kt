package com.asu1.quizzer.screens.quizlayout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.base.RowWithAppIconAndName
import com.asu1.quizzer.composables.quizcards.LazyColumnWithSwipeToDismiss
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.viewModels.QuizLoadViewModel

@Composable
fun LoadMyQuiz(
    navController: NavController,
    quizLoadViewModel: QuizLoadViewModel = viewModel(),
    onLoadQuiz: (Int) -> Unit = {},
    email: String = "",
) {
    val quizList by quizLoadViewModel.myQuizList.collectAsStateWithLifecycle()
    val quizLoadViewModelState by quizLoadViewModel.loadComplete.observeAsState()

    Scaffold(
        topBar = {
            RowWithAppIconAndName(
                showBackButton = true,
                onBackPressed = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = quizLoadViewModelState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                },
                label = "Load My Quiz Screen",
            ) {targetState ->
                when(targetState){
                    ViewModelState.LOADING -> {
                        Text(
                            stringResource(R.string.searching_for_quizzes),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else ->{
                        LazyColumnWithSwipeToDismiss(
                            quizList = quizList ?: mutableListOf(),
                            onLoadQuiz = { index ->
                                onLoadQuiz(index)
                            },
                            deleteQuiz = {deleteUuid ->
                                quizLoadViewModel.deleteMyQuiz(deleteUuid, email)
                            }
                        )
                    }
                }
            }
        }
    }

}

fun getSampleMyQuizLoadViewModel(): QuizLoadViewModel{
    val quizLoadViewModel = QuizLoadViewModel()
    quizLoadViewModel.setTest()
    return quizLoadViewModel
}

@Preview(showBackground = true)
@Composable
fun LoadMyQuizPreview(){
    val quizLoadViewModel = getSampleMyQuizLoadViewModel()
    LoadMyQuiz(
        navController = rememberNavController(),
        quizLoadViewModel = quizLoadViewModel
    )
}
