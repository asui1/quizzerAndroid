package com.asu1.quizzer.composables.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.screens.mainScreen.LogoutConfirmationDialog
import com.asu1.quizzer.screens.mainScreen.UserProfilePic
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.constants.userDataTest
import com.asu1.quizzer.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    closeDrawer: () -> Unit = {}, userData: UserViewModel.UserDatas?,
    isLoggedIn: Boolean = false,
    onSendInquiry: (String, String, String) -> Unit = { _, _, _ -> },
    logOut: () -> Unit = { },
    signOut: (String) -> Unit = { },
    navigateToMyQuizzes: () -> Unit = {},
) {
    val nickname = userData?.nickname
    var showInquiry by remember { mutableStateOf(false) }
    var showSignOut by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        if(showInquiry) {
            ModalBottomSheet(onDismissRequest = {showInquiry = false },
                modifier = Modifier.imePadding()
            ) {
                InquiryBottomSheetContent(
                    onDismissRequest = { showInquiry = false
                        closeDrawer() },
                    userData = userData,
                    isDone = false,
                    onSendInquiry = { email, type, text ->
                        onSendInquiry(email, type, text)
                        showInquiry = false
                    }
                )
            }
        }
        else if(showSignOut && isLoggedIn) {
            ModalBottomSheet(onDismissRequest = {showSignOut = false },
                modifier = Modifier.imePadding()) {
                SignoutBottomSheetContent(
                    onDismissRequest = { showSignOut = false
                        logOut()
                        closeDrawer() },
                    userData = userData,
                    isDone = false,
                    onSendSignOut = { email ->
                        signOut(email)
                        showSignOut = false
                    }
                )
            }
        }
        else if(showLogoutDialog){
            LogoutConfirmationDialog(
                onConfirm = {
                    logOut()
                    showLogoutDialog = false
                    closeDrawer()
                },
                onDismiss = { showLogoutDialog = false }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(250.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ){
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserProfilePic(userData, onClick = {

                        })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(nickname ?: "Guest", modifier = Modifier.padding(16.dp))
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            enabled = isLoggedIn,
                            onClick = {
                                showLogoutDialog = true
                            }) {
                            Icon(
                                imageVector = if (isLoggedIn) Icons.AutoMirrored.Filled.Logout else Icons.AutoMirrored.Filled.Login,
                                contentDescription = if (isLoggedIn) "Logout" else "Login"
                            )
                        }

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
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navigateToMyQuizzes()
                            closeDrawer()
                        },
                    )
                    {
                        Text(
                            stringResource(R.string.my_quizzes),
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {},
                    ) {
                        Text(
                            stringResource(R.string.profile),
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            style = TextStyle(
                                textDecoration = TextDecoration.LineThrough,
                            )
                        )
                    }
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {},
                    ) {
                        Text(
                            stringResource(R.string.settings),
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            style = TextStyle(
                                textDecoration = TextDecoration.LineThrough,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showInquiry = true
                        },
                    ) {
                        Text(
                            stringResource(R.string.inquiry),
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    if (isLoggedIn) {
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                showSignOut = true
                            },
                        ) {
                            Text(
                                stringResource(R.string.sign_out),
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DrawerPreview(){
    val navController = rememberNavController()
    QuizzerAndroidTheme {
        DrawerContent(
            closeDrawer = {},
            userData = userDataTest,
        )
    }
}