package com.asu1.mainpage.screens

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.activityNavigation.Route
import com.asu1.appdatamodels.SettingItems
import com.asu1.mainpage.composables.InquiryBottomSheetContent
import com.asu1.mainpage.composables.SignoutBottomSheetContent
import com.asu1.mainpage.viewModels.InquiryViewModel
import com.asu1.quiz.viewmodel.UserViewModel
import com.asu1.resources.R
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    userData:    UserViewModel.UserData?,
    isLoggedIn:  Boolean,
    navigateTo:  (Route) -> Unit
) {
    val inquiryVm: InquiryViewModel = viewModel()
    val userVm:    UserViewModel    = viewModel()

    var showInquiry  by remember { mutableStateOf(false) }
    var showSignOut  by remember { mutableStateOf(false) }

    // 1) modal sheets
    SettingsModals(
        showInquiry   = showInquiry,
        onDismissInquiry = { showInquiry = false },
        inquiryVm     = inquiryVm,
        userData      = userData,
        showSignOut   = showSignOut && isLoggedIn,
        onDismissSignOut = {
            showSignOut = false
            userVm.logOut()
        },
        userVm        = userVm
    )

    // 2) main content
    SettingsContent(
        userData     = userData,
        isLoggedIn   = isLoggedIn,
        navigateTo   = navigateTo,
        onInquiry    = { showInquiry = true },
        onLogOut     = { userVm.logOut() },
        onSignOut    = { showSignOut = true }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsModals(
    showInquiry:      Boolean,
    onDismissInquiry: () -> Unit,
    inquiryVm:        InquiryViewModel,
    userData:         UserViewModel.UserData?,
    showSignOut:      Boolean,
    onDismissSignOut: () -> Unit,
    userVm:           UserViewModel
) {
    if (showInquiry) {
        ModalBottomSheet(
            onDismissRequest = onDismissInquiry,
            modifier = Modifier.imePadding()
        ) {
            InquiryBottomSheetContent(
                onDismissRequest = onDismissInquiry,
                userData = userData,
                isDone = false,
                onSendInquiry = { email, type, text ->
                    inquiryVm.sendInquiry(email, type, text)
                    onDismissInquiry()
                }
            )
        }
    }
    else if (showSignOut) {
        ModalBottomSheet(
            onDismissRequest = onDismissSignOut,
            modifier = Modifier.imePadding()
        ) {
            SignoutBottomSheetContent(
                onDismissRequest = onDismissSignOut,
                userData = userData,
                isDone = false,
                onSendSignOut = { email ->
                    userVm.signOut(email)
                    onDismissSignOut()
                }
            )
        }
    }
}

@Composable
private fun SettingsContent(
    userData:    UserViewModel.UserData?,
    isLoggedIn:  Boolean,
    navigateTo:  (Route) -> Unit,
    onInquiry:   () -> Unit,
    onLogOut:    () -> Unit,
    onSignOut:   () -> Unit
) {
    val items = remember {
        persistentListOf(
            SettingItems(
                stringResourceId = R.string.my_quizzes,
                vectorIcon      = Icons.AutoMirrored.Filled.List,
                onClick         = { navigateTo(Route.LoadUserQuiz) }
            ),
            SettingItems(
                stringResourceId = R.string.my_activities,
                vectorIcon      = Icons.Default.BarChart,
                onClick         = {
                    // if you need to fetch before navigating,
                    // you can move that here or hoist it further up
                    navigateTo(Route.MyActivities)
                }
            ),
            SettingItems(
                stringResourceId = R.string.notification,
                vectorIcon      = Icons.Default.Notifications,
                onClick         = { navigateTo(Route.Notifications) }
            ),
            SettingItems(
                stringResourceId = R.string.inquiry,
                vectorIcon      = Icons.AutoMirrored.Filled.HelpOutline,
                onClick         = onInquiry
            )
        )
    }

    UserSettingsScreen(
        settingItems = items,
        isLoggedIn   = isLoggedIn,
        userData     = userData,
        onLogOut     = onLogOut,
        onSignOut    = onSignOut
    )
}
