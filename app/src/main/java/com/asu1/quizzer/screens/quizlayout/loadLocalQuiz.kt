package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.animations.LoadingAnimation
import com.asu1.quizzer.composables.base.RowWithAppIconAndName
import com.asu1.quizzer.composables.quizcards.LazyColumnWithSwipeToDismiss
import com.asu1.quizzer.data.QuizDataSerializer
import com.asu1.quizzer.data.ViewModelState
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.QuizLoadViewModel
import com.asu1.quizzer.viewModels.QuizTheme
import java.util.Base64

@Composable
fun LoadItems(
    navController: NavController,
    quizLoadViewModel: QuizLoadViewModel = viewModel(),
    onClickLoad: (quizData: QuizDataSerializer, quizTheme: QuizTheme, scoreCard: ScoreCard) -> Unit = { _, _, _ -> },
) {
    val quizSerializerList by quizLoadViewModel.quizList.collectAsStateWithLifecycle()
    val quizList = remember(quizSerializerList?.size ?: 0){quizSerializerList?.map{
        QuizCard(
            id = it.quizData.uuid,
            title = it.quizData.title,
            creator = it.quizData.creator,
            tags = it.quizData.tags.toList(),
            image = Base64.getDecoder().decode(it.quizData.titleImage),
            count = 0,
        )
    } ?: emptyList()}
    val loadComplete by quizLoadViewModel.loadComplete.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(loadComplete) {
        if (loadComplete == ViewModelState.SUCCESS) {
            navController.popBackStack()
            quizLoadViewModel.reset()
        }
    }

    Scaffold(
        topBar = {
            RowWithAppIconAndName(
                showBackButton = true,
                onBackPressed = {
                    navController.popBackStack(
                        Route.CreateQuizLayout,
                        inclusive = false
                    )
                }
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (quizSerializerList == null) {
                Text(
                    stringResource(R.string.searching_for_quizzes),
                    style = MaterialTheme.typography.bodyMedium,
                )
                LoadingAnimation()
            } else {
                LazyColumnWithSwipeToDismiss(
                    quizList = quizList,
                    onLoadQuiz = { index ->
                        val quizLayout = quizSerializerList!![index]
                        onClickLoad(
                            quizLayout.quizData,
                            quizLayout.quizTheme,
                            quizLayout.scoreCard
                        )
                    },
                    deleteQuiz = { deleteUuid ->
                        quizLoadViewModel.deleteLocalQuiz(context, deleteUuid)
                    },
                )
            }
        }
    }
}


fun getSampleQuizLoadViewModel(): QuizLoadViewModel{
    val quizLoadViewModel = QuizLoadViewModel()
    quizLoadViewModel.setTest()
    return quizLoadViewModel
}

@Preview(showBackground = true)
@Composable
fun LoadItemsPreview(){
    val quizLoadViewModel = getSampleQuizLoadViewModel()
    LoadItems(
        navController = rememberNavController(),
        quizLoadViewModel = quizLoadViewModel
    )
}
