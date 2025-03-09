package com.asu1.quizzer.composables.mainscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.composables.QuizzerTopBarBase
import com.asu1.quizzer.screens.mainScreen.UserProfilePic
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.constants.userDataTest
import com.asu1.quizzer.viewModels.UserViewModel
import com.asu1.resources.QuizzerTypographyDefaults
import kotlinx.coroutines.launch

@Composable
fun MainActivityTopbar(
    navController: NavController,
    onTopbarProfileClick: () -> Unit = {},
    userData: UserViewModel.UserData?,
    resetHome: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    QuizzerTopBarBase(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        onClickAppIcon = { resetHome() },
        body = {
            Text(
                "Quizzer",
                style = QuizzerTypographyDefaults.quizzerTopBarTitle
            )
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
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainActivityTopbarPreview(){
    com.asu1.resources.QuizzerAndroidTheme {
        MainActivityTopbar(
            navController = rememberNavController(),
            userData = userDataTest,
        )
    }
}
