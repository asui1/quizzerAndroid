package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.base.RowWithAppIconAndName
import com.asu1.quizzer.composables.quizcards.LazyColumnSwipeToDismissDialog
import com.asu1.quizzer.composables.quizcards.LazyColumnWithSwipeToDismiss
import com.asu1.quizzer.viewModels.QuizLoadViewModel
import kotlinx.coroutines.launch

@Composable
fun LoadMyQuiz(
    navController: NavController,
    quizLoadViewModel: QuizLoadViewModel = viewModel(),
    onLoadQuiz: (Int) -> Unit = {},
    email: String = "",
) {
    val quizList by quizLoadViewModel.myQuizList.observeAsState()

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
            if (quizList == null) {
                Text(
                    stringResource(R.string.searching_for_quizzes),
                    style = MaterialTheme.typography.bodyMedium,
                )
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumnWithSwipeToDismiss(
                    quizList = quizList ?: mutableListOf(),
                    onLoadQuiz = { index ->
                        onLoadQuiz(index)
                    },
                    deleteQuiz = {deleteIndex ->
                        quizLoadViewModel.deleteMyQuiz(deleteIndex, email)
                    }
                )
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
