package com.asu1.quizzer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.composables.RowWithAppIconAndName
import com.asu1.quizzer.states.QuizLayoutState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.UserViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.asu1.quizzer.R
import com.asu1.quizzer.model.QuizType
import com.asu1.quizzer.util.Route


@Composable
fun QuizBuilderScreen(navController: NavController,
                      quizLayoutState: QuizLayoutState,
) {
    val quizzes by quizLayoutState.quizzes
    val colorScheme by quizLayoutState.colorScheme
    var showNewQuizDialog by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Scaffold(
            topBar = {
                RowWithAppIconAndName(
                    showBackButton = true,
                    onBackPressed = {
                        navController.popBackStack()
                    }
                )
            },
            bottomBar = {
                QuizBuilderBottomBar()
            },

            ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyRow(
                    modifier = Modifier
                        .size(width = 300.dp, height = 550.dp)
                        .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(quizzes.size) {
                        // Your item content here
                    }
                    item {
                        NewQuizAdd(
                            moveToQuizCaller = {loadIndex, quizType ->
                                navController.navigate(
                                    Route.QuizCaller(
                                        loadIndex = loadIndex,
                                        quizType = quizType
                                    )
                                )
                            }
                        )
                    }
                }
                QuizEditIconsRow()
            }
        }
    }

}

@Composable
fun NewQuizAdd(
    moveToQuizCaller: (Int, QuizType) -> Unit
){
    var showDialog by remember { mutableStateOf(true) }
    Box(
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(onClick = { showDialog = true }) {
            Text("+")
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Question Type") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(4) { index ->
                        val imageRes = when (index) {
                            0 -> R.drawable.questiontype1
                            1 -> R.drawable.questiontype2
                            2 -> R.drawable.questiontype3
                            3 -> R.drawable.questiontype4
                            else -> R.drawable.questiontype1
                        }
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Question Type ${index + 1}",
                            modifier = Modifier
                                .size(width = 100.dp, height = 300.dp)
                                .clickable{
                                    showDialog = false
                                    //TODO MOVE TO QuizCaller
                                }
                        )
                    }
                }
            },
            confirmButton = {
            }
        )
    }
}

@Composable
fun QuizEditIconsRow(){

}

@Composable
fun QuizBuilderBottomBar(){

}

@Preview
@Composable
fun PreviewQuizBuilderScreen(){
    QuizzerAndroidTheme {

        QuizBuilderScreen(
            navController = rememberNavController(),
            quizLayoutState = getQuizLayoutState(),
        )
    }
}