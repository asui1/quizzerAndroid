package com.asu1.quizzer.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.viewModels.UserViewModel

data class LoginActivityState(
    val isUserLoggedIn : State<Boolean>,
    val userData: State<UserViewModel.UserDatas?>,
    val login: (String, String) -> Unit,
    val logout: () -> Unit,
    val signout: (String) -> Unit,
)

//KEEP STATE FOR LOGIN ACTIVITY TO USE USERVIEWMODEL AS ANDROIDVIEWMODEL
//
@Composable
fun rememberLoginActivityState(
    userViewModel: UserViewModel = viewModel(),
): LoginActivityState {
    val isUserLoggedIn by userViewModel.isUserLoggedIn.observeAsState(false)
    val userData by userViewModel.userData.observeAsState()
    return LoginActivityState(
        isUserLoggedIn = rememberUpdatedState(isUserLoggedIn),
        userData = rememberUpdatedState(userData),
        login = { email, photoUri -> userViewModel.logIn(email, photoUri) },
        logout = { userViewModel.logOut() },
        signout = {email -> userViewModel.signout(email) },
    )
}