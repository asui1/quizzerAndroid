package com.asu1.quizzer.composables.mainscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.screens.mainScreen.UserProfilePic
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.constants.userDataTest
import com.asu1.quizzer.viewModels.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityTopbar(navController: NavController, onTopbarProfileClick: () -> Unit = {},
                       userData: UserViewModel.UserDatas?,
                       resetHome: () -> Unit = {}) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = { Text("Quizzer") },
        navigationIcon = {
            IconButton(onClick = {  resetHome()}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "App Icon"
                )
            }
        },
        actions = {
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
        },
    )
}

@Preview
@Composable
fun MainActivityTopbarPreview(){
    QuizzerAndroidTheme {
        MainActivityTopbar(
            navController = rememberNavController(),
            userData = userDataTest,
        )
    }
}
