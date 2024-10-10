package com.asu1.quizzer.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.viewModels.InquiryViewModel
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.SignOutViewModel
import com.asu1.quizzer.viewModels.UserViewModel

data class LoginActivityState(
    val isUserLoggedIn : State<Boolean>,
    val login: (String, String) -> Unit,
)

//KEEP STATE FOR LOGIN ACTIVITY TO USE USERVIEWMODEL AS ANDROIDVIEWMODEL
@Composable
fun rememberLoginActivityState(
    userViewModel: UserViewModel = viewModel(),
): LoginActivityState {
    val isUserLoggedIn by userViewModel.isUserLoggedIn.observeAsState(false)

    return LoginActivityState(
        isUserLoggedIn = rememberUpdatedState(isUserLoggedIn),
        login = { email, photoUri -> userViewModel.logIn(email, photoUri) },
    )
}