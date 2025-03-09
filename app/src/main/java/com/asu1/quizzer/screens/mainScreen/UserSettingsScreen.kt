package com.asu1.quizzer.screens.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.asu1.quizzer.util.constants.userDataTest
import com.asu1.quizzer.viewModels.UserViewModel
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun UserSettingsScreen(
    userData: UserViewModel.UserData?,
    settingItems: PersistentList<Pair<Int, () -> Unit>>,
    isLoggedIn: Boolean = false,
    logOut: () -> Unit = { },
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    if(showLogoutDialog){
        LogoutConfirmationDialog(
            onConfirm = {
                logOut()
                showLogoutDialog = false
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                val (icon, nickname, email, logout) = createRefs()
                UserProfilePic(userData, onClick = {
                }, modifier = Modifier
                    .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                    .semantics {
                        contentDescription = "User Profile Image"
                    },
                    iconSIze = 72.dp,
                )
                Text(
                    userData?.nickname ?: "Guest",
                    style = QuizzerTypographyDefaults.quizzerQuizCardTitle,
                    modifier = Modifier.constrainAs(nickname) {
                    top.linkTo(icon.top)
                    start.linkTo(icon.end, margin = 8.dp)
                    bottom.linkTo(email.top)
                })
                Text(
                    userData?.email ?: "",
                    style = QuizzerTypographyDefaults.quizzerQuizCardCreator,
                    maxLines = 1,
                    modifier = Modifier.constrainAs(email) {
                    top.linkTo(nickname.bottom)
                    start.linkTo(icon.end, margin = 8.dp)
                    bottom.linkTo(icon.bottom)
                })
                IconButton(
                    enabled = isLoggedIn,
                    onClick = {
                        showLogoutDialog = true
                    },
                    modifier = Modifier.constrainAs(logout) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                ) {
                    Icon(
                        imageVector = if (isLoggedIn) Icons.AutoMirrored.Filled.Logout else Icons.AutoMirrored.Filled.Login,
                        contentDescription = if (isLoggedIn) "Logout" else "Login"
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(4f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
            ) {
                settingItems.forEachIndexed { index, item ->
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = item.second,
                    )
                    {
                        Text(
                            stringResource(item.first),
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            style = QuizzerTypographyDefaults.quizzerUI
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DrawerPreview(){
    com.asu1.resources.QuizzerAndroidTheme {
        UserSettingsScreen(
            userData = userDataTest,
            settingItems = listOfNotNull(
                Pair(R.string.my_quizzes, {}),
                Pair(R.string.my_activities, {}),
                Pair(R.string.notification, {}),
                Pair(R.string.inquiry, {}),
                Pair(R.string.sign_out, {})
            ).toPersistentList()
        )
    }
}