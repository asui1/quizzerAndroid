package com.asu1.quizzer.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.viewModels.InquiryViewModel
import com.asu1.quizzer.viewModels.MainViewModel
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.SignOutViewModel
import com.asu1.quizzer.viewModels.UserViewModel

data class MainActivityState(
    val bottomBarSelection: State<Int>,
    val userData: State<UserViewModel.UserDatas?>,
    val quizCards: State<QuizCardMainViewModel.QuizCards?>,
    val isSignOutDone: State<Boolean>,
    val isInquiryDone: State<Boolean>,
    val isLoggedIn: State<Boolean>,
    val logOut: () -> Unit,
    val onSendSignOut: (String) -> Unit,
    val onSendInquiry: (String, String, String) -> Unit,
    val updateQuizCards: () -> Unit,
)

@Composable
fun rememberMainActivityState(
    quizCardMainViewModel: QuizCardMainViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    signOutViewModel: SignOutViewModel = viewModel(),
    inquiryViewModel: InquiryViewModel = viewModel()
): MainActivityState {
    val bottomBarSelection by userViewModel.bottomBarSelection.observeAsState(initial = 0)
    val userData by userViewModel.userData.observeAsState()
    val quizCards by quizCardMainViewModel.quizCards.observeAsState(initial = null)
    val isSignOutDone by signOutViewModel.isDone.observeAsState(initial = false)
    val isInquiryDone by inquiryViewModel.isDone.observeAsState(initial = false)

    return MainActivityState(
        bottomBarSelection = rememberUpdatedState(bottomBarSelection),
        userData = rememberUpdatedState(userData),
        quizCards = rememberUpdatedState(quizCards),
        isSignOutDone = rememberUpdatedState(isSignOutDone),
        isInquiryDone = rememberUpdatedState(isInquiryDone),
        isLoggedIn = rememberUpdatedState(userData != null),
        logOut = { userViewModel.logOut() },
        onSendSignOut = { email -> signOutViewModel.sendSignout(email) },
        onSendInquiry = { email, subject, body -> inquiryViewModel.sendInquiry(email, subject, body) },
        updateQuizCards = { quizCardMainViewModel.fetchQuizCards("ko") },
    )
}