package com.asu1.quizzer.screens.mainScreen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizcard.LazyColumnWithSwipeToDismiss
import com.asu1.quizcard.QuizCardHorizontal
import com.asu1.quizzer.composables.QuizzerTopBarBase
import com.asu1.quizzer.composables.animations.LoadingAnimation
import com.asu1.quizzer.composables.base.RowWithAppIconAndName
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.QuizLoadViewModel
import com.asu1.resources.R
import com.asu1.resources.ViewModelState

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
            QuizzerTopBarBase(
                header = @Composable {
                    IconButton(onClick = {
                        navController.popBackStack(
                            Route.Home,
                            inclusive = false
                        )
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.move_back)
                        )
                    }
                },
                body = {
                    Text(stringResource(R.string.my_quizzes))
                },
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
                        LoadingAnimation(
                            modifier = Modifier.align(Alignment.Center).fillMaxSize()
                        )
                    }
                    else ->{
                        LazyColumnWithSwipeToDismiss(
                            quizList = quizList ?: mutableListOf(),
                            deleteQuiz = {deleteUuid ->
                                quizLoadViewModel.deleteMyQuiz(deleteUuid, email)
                            },
                            content = { quizCard, index ->
                                QuizCardHorizontal(
                                    quizCard = quizCard,
                                    onClick = {
                                        onLoadQuiz(index)
                                    }
                                )
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
