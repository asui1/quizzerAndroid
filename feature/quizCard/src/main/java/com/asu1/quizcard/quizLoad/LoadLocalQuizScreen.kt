package com.asu1.quizcard.quizLoad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.customComposable.topBar.RowWithAppIconAndName
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.quizcard.cardBase.LazyColumnWithSwipeToDismiss
import com.asu1.quizcard.cardBase.QuizCardHorizontal
import com.asu1.quizcardmodel.QuizCard
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.resources.ViewModelState
import kotlinx.collections.immutable.toPersistentList
import java.util.Base64

@Composable
fun LoadLocalQuizScreen(
    navController: NavController,
) {
    val quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel()
    val loadLocalQuizViewModel: LoadLocalQuizViewModel = viewModel()

    val serializerList by loadLocalQuizViewModel.localQuizList.collectAsStateWithLifecycle()
    val loadComplete by loadLocalQuizViewModel.loadLocalQuizViewModelState.observeAsState()
    val context = LocalContext.current

    val quizCards = rememberQuizCards(serializerList)

    LaunchedEffect(loadComplete) {
        if (loadComplete == ViewModelState.SUCCESS) {
            navController.popBackStack()
            loadLocalQuizViewModel.reset()
        }
    }

    Scaffold(
        topBar = {
            LoadLocalTopBar(onBack = { navController.popBackStack() })
        }
    ) { padding ->
        LoadLocalContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 16.dp)
                .background(MaterialTheme.colorScheme.background),
            serializerList = serializerList,
            quizCards = quizCards,
            onDelete = { id -> loadLocalQuizViewModel.deleteLocalQuiz(context, id) },
            onPick = { index ->
                // index는 quizCards 인덱스와 serializerList 인덱스가 동일하다는 전제
                val layout = serializerList!![index]
                quizCoordinatorViewModel.loadQuiz(
                    quizData = layout.quizData,
                    quizTheme = layout.quizTheme,
                    scoreCard = layout.scoreCard
                )
                loadLocalQuizViewModel.loadComplete()
            }
        )
    }
}

/* ---------------- UI pieces ---------------- */

@Composable
private fun LoadLocalTopBar(onBack: () -> Unit) {
    RowWithAppIconAndName(
        header = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.move_back_home)
                )
            }
        }
    )
}

@Composable
private fun LoadLocalContent(
    modifier: Modifier,
    serializerList: List<QuizLayoutSerializer>?, // 타입 이름은 실제에 맞게 변경
    quizCards: List<QuizCard>,
    onDelete: (String) -> Unit,
    onPick: (Int) -> Unit,
) {
    Box(modifier) {
        if (serializerList == null) {
            Column {
                Text(
                    stringResource(R.string.searching_for_quizzes),
                    style = QuizzerTypographyDefaults.quizzerBodyMediumNormal,
                )
                LoadingAnimation()
            }
        } else {
            LazyColumnWithSwipeToDismiss(
                inputList = quizCards.toPersistentList(),
                deleteItemWithId = onDelete,
            ) { quizCard, index ->
                QuizCardHorizontal(
                    quizCard = quizCard,
                    onClick = { onPick(index) }
                )
            }
        }
    }
}

@Composable
private fun rememberQuizCards(
    serializerList: List<QuizLayoutSerializer>?
): List<QuizCard> {
    return remember(serializerList) {
        serializerList?.map { s ->
            QuizCard(
                id = s.quizData.uuid,
                title = s.quizData.title,
                creator = s.quizData.creator,
                tags = s.quizData.tags.toList(),
                image = runCatching { Base64.getDecoder().decode(s.quizData.titleImage) }
                    .getOrNull(),
                count = 0,
            )
        } ?: emptyList()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadItemsPreview(){
    val navController = rememberNavController()
    QuizzerAndroidTheme {
        LoadLocalQuizScreen(
            navController
        )
    }
}
