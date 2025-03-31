package com.asu1.mainpage.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.appdatamodels.SettingItems
import com.asu1.appdatamodels.sampleSettingItems
import com.asu1.mainpage.composables.SettingsGroupCard
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.mainpage.viewModels.sampleUserData
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.utils.getAppVersion
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun UserSettingsScreen(
    userData: UserViewModel.UserData?,
    settingItems: PersistentList<SettingItems>,
    isLoggedIn: Boolean = false,
    logOut: () -> Unit = { },
    onSignOut: () -> Unit = {},
    ) {
    val context = LocalContext.current
    val version = remember { context.getAppVersion() }
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
    val logOutItems = remember{
        persistentListOf(
            SettingItems(
                stringResourceId = R.string.logout,
                vectorIcon = Icons.AutoMirrored.Filled.Logout,
                onClick = {showLogoutDialog = true}
            ),
        )
    }
    val signOutItems = remember {
        persistentListOf(
            SettingItems(
                stringResourceId = R.string.sign_out,
                vectorIcon = Icons.Default.DeleteForever,
                onClick = onSignOut
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        ProfileCardSection(userData)

        if(isLoggedIn) {
            SettingsGroupCard(
                modifier = Modifier.padding(bottom = 16.dp),
                items = logOutItems
            )
        }

        SettingsGroupCard(
            modifier = Modifier.padding(bottom = 16.dp),
            items = settingItems,
        )

        Text(
            "${stringResource(R.string.app_version)}: $version",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
        )

        if(isLoggedIn) {
            SettingsGroupCard(
                modifier = Modifier.padding(vertical = 16.dp),
                items = signOutItems
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerPreview(){
    com.asu1.resources.QuizzerAndroidTheme {
        UserSettingsScreen(
            userData = sampleUserData,
            isLoggedIn = true,
            settingItems = listOfNotNull(
                sampleSettingItems,
                sampleSettingItems,
                sampleSettingItems,
                sampleSettingItems,
            ).toPersistentList()
        )
    }
}

@Composable
private fun ProfileCardSection(userData: UserViewModel.UserData?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            UserProfilePic(
                userData = userData,
                onClick = { /* 프로필 편집 등 */ },
                modifier = Modifier.semantics {
                    contentDescription = "User Profile Image"
                },
                iconSIze = 56.dp
            )

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = userData?.nickname ?: "Guest",
                    style = QuizzerTypographyDefaults.quizzerTitleMediumBold,
                )
                Text(
                    text = userData?.email ?: "",
                    style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
                )
            }
        }
    }
}
