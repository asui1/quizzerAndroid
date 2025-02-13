package com.asu1.quizzer.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.viewModels.UserViewModel

@Composable
fun MyActivitiesScreen(
    userViewModel: UserViewModel = viewModel()
) {

}

@Composable
fun MyActivitiesBody(){

}

@Preview(showBackground = true)
@Composable
fun MyActivitiesScreenPreview() {
    MyActivitiesBody()
}