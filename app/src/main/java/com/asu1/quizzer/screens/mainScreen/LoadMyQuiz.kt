package com.asu1.quizzer.screens.mainScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.quizcard.LazyColumnWithSwipeToDismiss
import com.asu1.quizcard.QuizCardHorizontal
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.quizzer.composables.QuizzerTopBarBase
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.quizModels.LoadMyQuizViewModel
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.map

@Composable
fun LoadMyQuizScreen(
    navController: NavController,
    loadMyQuizViewModel: LoadMyQuizViewModel = viewModel(),
    onLoadQuiz: (Int) -> Unit = {},
    email: String = "",
) {
    val quizList by loadMyQuizViewModel.myQuizList.map{
        it?.toPersistentList() ?: persistentListOf()
    }
        .collectAsStateWithLifecycle(persistentListOf())
    val quizLoadViewModelState by loadMyQuizViewModel.loadMyQuizViewModelState.observeAsState(
        ViewModelState.LOADING
    )

    LoadMyQuizBody(
        onMoveHome = {
            navController.popBackStack(
                Route.Home,
                inclusive = false
            )
        },
        quizLoadViewModelState = quizLoadViewModelState,
        quizList = quizList,
        deleteQuiz = {deleteUuid ->
            loadMyQuizViewModel.deleteMyQuiz(deleteUuid, email)
        },
        onLoadQuiz = {index ->
            onLoadQuiz(index)
        }
    )

}

@Composable
fun LoadMyQuizBody(
    onMoveHome: () -> Unit = {},
    quizLoadViewModelState: ViewModelState,
    quizList: PersistentList<QuizCard>,
    deleteQuiz: (String) -> Unit = {},
    onLoadQuiz: (Int) -> Unit = {},
){
    Scaffold(
        topBar = {
            QuizzerTopBarBase(
                header = @Composable {
                    IconButton(onClick = onMoveHome
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.move_back)
                        )
                    }
                },
                body = {
                    Text(
                        stringResource(R.string.my_quizzes),
                        style = QuizzerTypographyDefaults.quizzerHeadlineSmallNormal,
                    )
                },
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(top = 16.dp)
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
                            inputList = quizList.toPersistentList(),
                            deleteItemWithId = deleteQuiz,
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

@Preview(showBackground = true)
@Composable
fun LoadMyQuizListPreview(){
    QuizzerAndroidTheme {
        LoadMyQuizBody(
            quizLoadViewModelState = ViewModelState.IDLE,
            quizList = sampleQuizCardList.toPersistentList()
        )
    }
}
