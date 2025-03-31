package com.asu1.mainpage.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.mainpage.screens.UserProfilePic
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.mainpage.viewModels.sampleUserData
import com.asu1.resources.QuizzerAndroidTheme
import kotlinx.coroutines.launch

@Composable
fun MainActivityTopbar(
    navController: NavController,
    onTopBarProfileClick: () -> Unit = {},
    userData: UserViewModel.UserData?,
    resetHome: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    QuizzerTopBarBase(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        onClickAppIcon = { resetHome() },
        body = {
        },
        actions = {
            IconButton(onClick = { navController.navigate(Route.Search("")){
                launchSingleTop = true
            } }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            UserProfilePic(userData, onClick = {
                scope.launch {
                    onTopBarProfileClick()
                }
            })
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainActivityTopbarPreview(){
    QuizzerAndroidTheme {
        MainActivityTopbar(
            navController = rememberNavController(),
            userData = sampleUserData,
        )
    }
}
