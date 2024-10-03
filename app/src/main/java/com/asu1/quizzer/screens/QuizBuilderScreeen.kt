package com.asu1.quizzer.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.composables.RowWithAppIconAndName
import com.asu1.quizzer.states.QuizLayoutState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.UserViewModel

@Composable
fun QuizBuilderScreen(navController: NavController,
                      quizLayoutState: QuizLayoutState,
) {
    val quizzes by quizLayoutState.quizzes

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
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyRow(){
                item{}
                items(quizzes.size){

                }
            }
            QuizEditIconsRow()
        }
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