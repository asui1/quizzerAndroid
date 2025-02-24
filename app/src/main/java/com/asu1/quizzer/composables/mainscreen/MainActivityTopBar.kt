package com.asu1.quizzer.composables.mainscreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.screens.mainScreen.UserProfilePic
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.constants.userDataTest
import com.asu1.quizzer.viewModels.UserViewModel
import com.asu1.resources.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityTopbar(navController: NavController, onTopbarProfileClick: () -> Unit = {},
                       userData: UserViewModel.UserDatas?,
                       resetHome: () -> Unit = {}) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
        ){
        IconButton(onClick = {  resetHome()}) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Icon"
            )
        }
        Spacer(
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { navController.navigate(Route.Search("")){
            launchSingleTop = true
        } }) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
        UserProfilePic(userData, onClick = {
            scope.launch {
                onTopbarProfileClick()
            }
        })
    }
}

@Preview
@Composable
fun MainActivityTopbarPreview(){
    com.asu1.resources.QuizzerAndroidTheme {
        MainActivityTopbar(
            navController = rememberNavController(),
            userData = userDataTest,
        )
    }
}
