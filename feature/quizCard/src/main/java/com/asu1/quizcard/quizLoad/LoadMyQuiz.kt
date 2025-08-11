package com.asu1.quizcard.quizLoad

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
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quizcard.cardBase.LazyColumnWithSwipeToDismiss
import com.asu1.quizcard.cardBase.QuizCardHorizontal
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun LoadMyQuizScreen(
    navController: NavController,
) {
    val loadMyQuizViewModel: LoadMyQuizViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel()
    val email: String = userViewModel.userData.value?.email ?: ""
    val quizList by loadMyQuizViewModel.myQuizList
        .collectAsStateWithLifecycle(persistentListOf())
    val quizLoadViewModelState by loadMyQuizViewModel.loadMyQuizViewModelState.observeAsState(
        ViewModelState.LOADING
    )
    fun onLoadQuiz(index: Int){
        quizCoordinatorViewModel.loadQuiz(
            quizList[index].id
        )
    }

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
) {
    Scaffold(
        topBar = { MyQuizTopBar(onBack = onMoveHome) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            MyQuizAnimatedContent(
                state = quizLoadViewModelState,
                loading = { LoadingSection() },
                content = {
                    if (quizList.isEmpty()) EmptyMyQuizMessage()
                    else MyQuizList(
                        quizList = quizList,
                        deleteQuiz = deleteQuiz,
                        onLoadQuiz = onLoadQuiz
                    )
                }
            )
        }
    }
}

/* ---------- Top bar ---------- */

@Composable
private fun MyQuizTopBar(onBack: () -> Unit) {
    QuizzerTopBarBase(
        header = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.move_back)
                )
            }
        },
        body = {
            Text(
                text = stringResource(R.string.my_quizzes),
                style = QuizzerTypographyDefaults.quizzerHeadlineSmallNormal
            )
        }
    )
}

/* ---------- Content switch ---------- */

@Composable
private fun MyQuizAnimatedContent(
    state: ViewModelState,
    loading: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "Load My Quiz Screen"
    ) { target ->
        when (target) {
            ViewModelState.LOADING -> loading()
            else -> content
        }
    }
}

/* ---------- Sections ---------- */

@Composable
private fun LoadingSection() {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.searching_for_quizzes),
            style = MaterialTheme.typography.bodyMedium
        )
        LoadingAnimation(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        )
    }
}

@Composable
private fun EmptyMyQuizMessage() {
    Text(
        text = stringResource(R.string.my_quizzes_empty),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun MyQuizList(
    quizList: PersistentList<QuizCard>,
    deleteQuiz: (String) -> Unit,
    onLoadQuiz: (Int) -> Unit
) {
    if (quizList.isEmpty()) {
        EmptyMyQuizMessage()
        return
    }
    LazyColumnWithSwipeToDismiss(
        inputList = quizList,
        deleteItemWithId = deleteQuiz
    ) { quizCard, index ->
        QuizCardHorizontal(
            quizCard = quizCard,
            onClick = { onLoadQuiz(index) }
        )
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
